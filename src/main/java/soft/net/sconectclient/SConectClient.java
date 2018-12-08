package soft.net.sconectclient;

import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import soft.common.conf.ConfException;
import soft.common.exception.DataIsNullException;
import soft.common.log.IWriteLog;
import soft.common.log.Log4j2Writer;
import soft.net.conf.ConfigClient;
import soft.net.exception.ConectSeverException;
import soft.net.ifs.IBytesBuild;
import soft.net.ifs.IClientNet;
import soft.net.ifs.IListenerCreator;
import soft.net.model.ClientChanel;
import soft.net.model.NetEventListener;

public class SConectClient implements IClientNet {

	private static final IWriteLog log = new Log4j2Writer(SConectClient.class);

	private static IListenerCreator creator = null;
	/**
	 * 心跳包数据
	 */
	protected static IBytesBuild heartData;// 客户端检测服务端是否断开用

	public static IBytesBuild getHeartData() {
		return heartData;
	}

	private ClientCheckConTdStore longConTdStore;
	// private TdCachePoolExctor connectPool;// 固定线程池执行器
	private EventLoopGroup group;
	private Bootstrap bstrap;
	private ClientChannelStore store;

	/**
	 * 初始化
	 * 
	 * @param creator
	 * @throws DataIsNullException
	 * @throws IOException
	 * @throws ConfException
	 */
	public static void init(IListenerCreator creator, IBytesBuild heartData)
			throws DataIsNullException, ConfException, IOException {
		if (creator == null)
			throw new DataIsNullException("the creator is null");
		if (heartData == null)
			throw new DataIsNullException("the heartdata is null");

		SConectClient.creator = creator;
		SConectClient.heartData = heartData;
		ConfigClient.init();

	}

	public SConectClient() {
		store = new ClientChannelStore();
		// connectPool = new TdCachePoolExctor();// 执行重连
		longConTdStore = new ClientCheckConTdStore();
		group = new NioEventLoopGroup(1);
		bstrap = new Bootstrap();
		// bstrap.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 128 *
		// 1024);
		// bstrap.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 64 * 1024);
		bstrap.group(group).channel(NioSocketChannel.class);
		bstrap.option(ChannelOption.TCP_NODELAY, true); // 设置立即发送;
		bstrap.option(ChannelOption.AUTO_READ, true);
		bstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
		bstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bstrap.handler(new MyClientInittializer());
	}

	@Override
	public void close() {
		try {
			// 循环遍历关闭所有长连接
			store.closeAllConnect();
			longConTdStore.shutdownNow();
		} catch (Exception e) {
			if (group != null)
				group.shutdownGracefully();
		}
	}

	@Override
	public void sendDataToSvr(String ip, int port, IBytesBuild data, int timeout) throws Exception {
		buildConnect(ip, port, false, timeout, new ISendDataToServer() {

			@Override
			public void sendData(ClientHandler handler) throws Exception {
				try {
					handler.getChannel().sendData(data, true);
				} finally {
					handler.getChannel().close();
				}
			}
		}, false);
	}

	@Override
	public ClientChanel connectServer(String ip, int port, int timeout) throws Exception {
		return buildConnect(ip, port, true, timeout, null, false);
	}

	@Override
	public void sendDataToSvr(NetEventListener listener, IBytesBuild data) throws Exception {
		listener.getNetSource().sendData(data);
	}

	/**
	 * handler初始化器
	 * 
	 * @author fanpei
	 *
	 */
	private class MyClientInittializer extends ChannelInitializer<NioSocketChannel> {

		@Override
		protected void initChannel(NioSocketChannel ch) throws Exception {

			ChannelPipeline pipeline = ch.pipeline();
			NetEventListener listener = creator.getListener(ch);
			ClientHandler handler = new ClientHandler(listener, store);
			pipeline.addLast(ch.id().asLongText(), handler);
		}
	}

	/**
	 * @param ip
	 * @param port
	 * @param keep    true 长连接 false 短连接
	 * @param timeout 建立连接超时时间
	 * 
	 * @param isend   发送数据操作 可为空
	 * @param isRecon 是否是重连操作
	 * @return
	 * @throws Exception
	 */
	private ClientChanel buildConnect(String ip, int port, boolean keep, int timeout, ISendDataToServer isend,
			boolean isRecon) throws Exception {

		ChannelFuture f = bstrap.connect(ip, port);// 连接服务端
		// ClientConectMonitor monitor = new ClientConectMonitor();
		f.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				// monitor.notifyAll();
			}
		});
		f.await();
		// monitor.connectWait(timeout);
		ChannelPipeline pipeline = f.channel().pipeline();
		ClientHandler handler = (ClientHandler) pipeline.get(f.channel().id().asLongText());
		ClientChanel chanel = new ClientChanel(handler.getListener());
		if (keep && !isRecon) {// 长连接只建立一次
			ClientCheckConTd tdThread = new ClientCheckConTd(bstrap, chanel, ip, port);
			longConTdStore.run(tdThread);
		}

		if (f.isSuccess()) {
			log.info("与服务器{}:{} 连接建立成功...", ip, port);
			if (isend != null) {
				isend.sendData(handler);
				// if (handler.getListener().reciveWait(timeout)) // 3.等待数据发送后的反馈
				// throw new TimeoutException("未收到服务" + ip + ":" + port +
				// "反馈结果，数据接收超时,请检查网络连接或服务是否开启");
			}
		} else {
			log.info("与服务器{}:{} 连接建立失败...", ip, port);
			throw new ConectSeverException("建立连接超时,请检查网络连接或服务是否开启", f.cause());
		}

		return chanel;
	}

	private interface ISendDataToServer {
		void sendData(ClientHandler handler) throws Exception;
	}

}

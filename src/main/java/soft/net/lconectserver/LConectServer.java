package soft.net.lconectserver;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import io.netty.util.concurrent.DefaultThreadFactory;
import soft.common.conf.ConfException;
import soft.common.log.IWriteLog;
import soft.common.log.LogWriter;
import soft.common.tdPool.TdFixedPoolExcCenter;
import soft.net.conf.CongfigServer;
import soft.net.exception.ConectClientsFullException;
import soft.net.exception.NoCurrentPortConnectException;
import soft.net.ifs.IByteBuff;
import soft.net.ifs.IBytesBuild;
import soft.net.ifs.IListenerCreator;
import soft.net.ifs.INetChanel;
import soft.net.ifs.ISvrNet;
import soft.net.model.CusHostAndPort;
import soft.net.model.NetBase;
import soft.net.model.NetByteBuff;
import soft.net.model.NetEventListener;

/**
 * 网络服务端类
 * 
 * @author fanpei
 *
 */
public class LConectServer extends NetBase implements ISvrNet {
	private static final IWriteLog log = new LogWriter(LConectServer.class);
	private static IListenerCreator creator = null;
	private ServerBootstrap bootstrap;
	private EventLoopGroup workergroup;
	private EventLoopGroup bossGroup;

	private ServerConMap store = null;// 连接仓库
	private CountDownLatch latch = null;
	private TdFixedPoolExcCenter threadSver = null;

	public static void init(IListenerCreator creator) throws ConfException, IOException {
		LConectServer.creator = creator;
		CongfigServer.init();
	}

	/**
	 * 网络服务端
	 * 
	 * @param ip
	 *            监听地址
	 * @param port
	 *            监听端口
	 */

	public LConectServer() {
		ResourceLeakDetector.setLevel(Level.ADVANCED);
		hosts = CongfigServer.HOSTS;
		store = new ServerConMap(hosts);
		int processorsNumber = Runtime.getRuntime().availableProcessors();
		bootstrap = new ServerBootstrap();// 引导辅助程序
		bossGroup = new NioEventLoopGroup(1);
		ThreadFactory boosstf = new DefaultThreadFactory("Netty-Worker");
		workergroup = new NioEventLoopGroup(processorsNumber, boosstf, SelectorProvider.provider());
		bootstrap.group(bossGroup, workergroup);

		// 设置nio类型的channel
		bootstrap.channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 2048);
		bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 32).option(ChannelOption.SO_SNDBUF, 1024 * 32)
				.option(ChannelOption.TCP_NODELAY, false).option(ChannelOption.ALLOW_HALF_CLOSURE, true)// 半关闭
				// 设置立即发送;

				// .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 512 *
				// 1024).option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 384 *
				// 1024)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000)
				// 最大空闲连接时间
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childOption(ChannelOption.TCP_NODELAY, false);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {// 有连接到达时会创建一个channel
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				if (closed) {// 关闭不接受新的连接
					ch.close();
					return;
				}
				NetEventListener listener = creator.getListener(ch);
				LConectServerHandler shEchoServerHandler = new LConectServerHandler(listener);
				ch.pipeline().addLast(new ReadTimeoutHandler(CongfigServer.CHANLEDATARECVINTERVAL));// 未收到数据间隔断开
				ch.pipeline().addLast("MyServerHandler", shEchoServerHandler);

			}
		});
	}

	@Override
	public void start() throws Exception {
		try {
			latch = new CountDownLatch(hosts.size());
			threadSver = new TdFixedPoolExcCenter(hosts.size());
			for (CusHostAndPort ip_port : hosts) {
				PortInstance pi = new PortInstance(latch, ip_port.getIP(), ip_port.getPort());
				threadSver.execute(pi);
			}

			latch.await();
			throw new Exception("network closed");
		} catch (Exception e) {
			if (!closed)
				throw e;
		} finally {
			if (threadSver != null)
				threadSver.shutdownNow();
		}

	}

	private boolean closed = false;

	@Override
	public void close() {
		closed = true;
		// 关闭所有连接
		store.closeAllConnect();

		// 关闭每个通道
		if (bossGroup != null)
			bossGroup.shutdownGracefully();
		if (workergroup != null)
			workergroup.shutdownGracefully();// 关闭EventLoopGroup，释放掉所有资源包括创建的线程

	}

	@Override
	public void sendDataToAllClient(IBytesBuild data) throws Exception {
		validate(data);
		Collection<INetChanel> channels = store.getAllChanels();
		if (channels != null && !channels.isEmpty()) {
			for (INetChanel ch : channels) {
				ch.sendData(data);
			}
		}
	}

	@Override
	public void sendDataToAllClient(int localPort, IBytesBuild data) throws Exception {
		validate(data);
		Collection<INetChanel> channels = store.getAllChanels(localPort);
		if (channels != null && !channels.isEmpty()) {
			for (INetChanel ch : channels) {
				ch.sendData(data);
			}
		}
	}

	@Override
	public void sendDataToAllClient(Collection<INetChanel> channels, IBytesBuild data) throws Exception {
		validate(data);
		if (channels != null && !channels.isEmpty()) {
			for (INetChanel ch : channels) {
				ch.sendData(data);
			}
		}
	}

	@Override
	public int getAllConnectNum(int localPort) {
		return store.getConnectNum(localPort);
	}

	@Override
	public int getAllConnectNum() {
		return store.getAllConectNum();
	}

	private static void validate(IBytesBuild data) throws Exception {
		if (data == null) {
			throw new Exception("待发送数据为空，请检查数据完整性");
		}
	}

	/**
	 * Sharable表示此对象在channel间共享 handler类是我们的具体业务类
	 * 
	 * @param <IBytesBuild>
	 */
	@Sharable
	// 注解@Sharable可以让它在channels间共享
	public class LConectServerHandler extends ChannelInboundHandlerAdapter {

		private NetEventListener listener;

		public LConectServerHandler(NetEventListener listener) {
			this.listener = listener;
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) {
			closeConnect(ctx);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);

			boolean error = false;

			try {
				if (store.getAllConectNum() >= CongfigServer.MAXCLIENTS) {
					throw new ConectClientsFullException();
				}
				log.info("client has conected success:{}", listener.getNetSource().getRIpPort());
				store.addChannel(listener.getNetSource());
				listener.chanelConect();
			} catch (ConectClientsFullException e) {
				error = true;
				log.warn(e.getMessage());
			} catch (Exception e) {
				error = true;
				log.error(e);
			} finally {
				if (error) {
					listener.release();
				}
			}
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object obj) {
			ByteBuf in = null;
			try {
				if (obj instanceof ByteBuf) {
					in = (ByteBuf) obj;
					IByteBuff inbuff = new NetByteBuff(in);
					in.retain();
					listener.dataReciveEvent(inbuff);
				}

			} catch (Exception e2) {
				log.error(e2);
			} finally {
				if (in != null && in.refCnt() > 0)// 大于0才释放
					ReferenceCountUtil.release(in);
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			try {
				log.warn("server network exception {}", ctx.channel().remoteAddress().toString(), cause);
				closeConnect(ctx);
			} catch (Exception e) {
			}

		}

		private boolean closed = false;

		private void closeConnect(ChannelHandlerContext ctx) {
			try {
				if (!closed) {
					closed = true;
					log.info("client has disconected:{}", listener.getNetSource().getRIpPort());
					store.removeChannel(listener.getNetSource());
					listener.closeEvent();
					listener.release();
				}

			} catch (NoCurrentPortConnectException e) {
				log.warn("server network exception {} {}", ctx.channel().remoteAddress().toString(), e);
			} finally {
				ctx.close();// 出现异常时关闭channel
			}
		}
	}

	/**
	 * 端口实例
	 * 
	 * @author fanpei
	 *
	 */
	class PortInstance implements Runnable {

		private CountDownLatch latch;
		private String ip;
		private int port;
		private ChannelFuture f;

		public PortInstance(CountDownLatch latch, String ip, int port) {
			this.latch = latch;
			this.ip = ip;
			this.port = port;
		}

		@Override
		public void run() {
			String ip_port = null;
			try {
				ip_port = String.format("%s:%d", ip, port);
				Thread td = Thread.currentThread();
				td.setName(String.format("网络监听 [%s]", ip_port));

				log.info("server Attemping to listenning on " + ip_port);
				f = bootstrap.bind(ip, port).sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
				log.info("server started and listen on " + ip_port);
				f.channel().closeFuture().sync();
			} catch (Exception e) {
				log.info("server will close the: " + ip_port, e);
			} finally {
				latch.countDown();
			}

		}
	}

}

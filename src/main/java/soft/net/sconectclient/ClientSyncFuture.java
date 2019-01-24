package soft.net.sconectclient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import soft.common.log.IWriteLog;
import soft.common.log.Log4j2Writer;
import soft.net.model.ClientChanel;

public class ClientSyncFuture implements GenericFutureListener<Future<? super Void>> {
	private static final IWriteLog log = new Log4j2Writer(ClientSyncFuture.class);

	// 因为请求和响应是一一对应的，因此初始化CountDownLatch值为1。
	private CountDownLatch latch = new CountDownLatch(1);

	private ClientChanel response;

	private String ip;
	private int port;
	private boolean keep;// 是否长连接
	private Bootstrap bstrap;
	private ClientCheckConTdStore longConTdStore;
	private IClientSendData isend;
	private ChannelFuture f;

	public ClientSyncFuture(String ip, int port, boolean keep, Bootstrap bstrap, ChannelFuture f,
			ClientCheckConTdStore longConTdStore, IClientSendData isend) {
		this.ip = ip;
		this.port = port;
		this.keep = keep;
		this.bstrap = bstrap;
		this.f = f;
		this.longConTdStore = longConTdStore;
		this.isend = isend;
	}

	/**
	 * 直到有结果才返回。
	 * 
	 * @throws InterruptedException
	 */
	public ClientChanel syncwait() throws InterruptedException {
		latch.await();
		return response;
	}

	/**
	 * ，直到有结果或者超过指定时间就返回。
	 * 
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 */
	public ClientChanel syncwait(long timeout, TimeUnit unit) throws InterruptedException {
		latch.await(timeout, unit);
		return response;
	}

	/**
	 * 用于设置响应结果，并且做countDown操作，通知请求线程
	 * 
	 */
	private void setResponse(ClientChanel response) {
		this.response = response;
		latch.countDown();
	}

	@Override
	public void operationComplete(Future<? super Void> future) throws Exception {
		ClientChanel clientChanel = null;
		try {
			ChannelPipeline pipeline = f.channel().pipeline();
			ClientHandler handler = (ClientHandler) pipeline.get(SConectClient.READERHANDLER);
			clientChanel = new ClientChanel(handler.getListener());
			if (keep) {// 长连接只建立一次
				ClientCheckConTd tdThread = new ClientCheckConTd(bstrap, clientChanel, ip, port);
				longConTdStore.excute(tdThread);
			}
			if (isend != null) {
				isend.sendData(clientChanel);
			}

			if (future.isSuccess()) {
				log.info("与服务器{}:{} 连接建立成功...", ip, port);
			} else {
				log.info("与服务器{}:{} 连接建立失败...", ip, port);
			}
		} finally {
			setResponse(clientChanel);
		}

	}

}

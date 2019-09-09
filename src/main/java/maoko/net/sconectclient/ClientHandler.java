package maoko.net.sconectclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.net.exception.ReadbleException;
import maoko.net.ifs.IByteBuff;
import maoko.net.model.CusNetSource;
import maoko.net.model.NetByteBuff;
import maoko.net.model.NetEventListener;

/**
 * 数据处理handle
 * 
 * @author fanpei
 * @date 2018-09-10 01:30
 *
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	private static final IWriteLog log = new Log4j2Writer(ClientHandler.class);

	private ClientChannelStore store;
	private NetEventListener listener;

	public NetEventListener getListener() {
		return listener;
	}

	public CusNetSource getChannel() {
		return listener.getNetSource();
	}

	public ClientHandler(NetEventListener listener, ClientChannelStore store) {
		this.listener = listener;
		this.store = store;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		try {
			log.info("client has conected success:{}", getChannel().getRIpPort());
			store.addChanel(getChannel());
			listener.chanelConect();
		} catch (Exception e) {
			log.error("连接初始化异常", e);
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("client has disconected server:{}", getChannel().getRIpPort());
		closeConect(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		try {
			if (in.isReadable()) {
				IByteBuff inbuff = new NetByteBuff(in);
				listener.decode(inbuff);
			} else
				throw new ReadbleException(listener.getNetSource().getRIpPort() + " can not readble");
		} catch (Exception e) {
			log.error("读取数据执行操作时发生异常", e);
		} finally {

			// 2018.12.29 去掉此行，SimpleChannelInboundHandler默认会释放数据
			/*
			 * if (in != null && in.refCnt() > 0)// 大于0才释放 ReferenceCountUtil.release(in);
			 */
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// String err = cause.getMessage();
		log.warn("client network exception {}", ctx.channel().remoteAddress().toString(), cause);
		closeConect(ctx);
	}

	private void closeConect(ChannelHandlerContext ctx) {
		try {
			store.removeChannel(getChannel());
			listener.release();
			listener.closeEvent();
		} finally {
			ctx.close();
		}
	}
}
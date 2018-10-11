package soft.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;
import soft.common.StringUtil;
import soft.net.exception.NetBuildBytesNullException;
import soft.net.exception.NetSendDataIsNull;
import soft.net.ifs.IBytesBuild;

/**
 * 数据发送助手
 * 
 * @author fanpei
 * @date 2018-09-09 23:03
 *
 */
public class SendDataUtil {
	// private static final IWriteLog log = new LogWriter(SendDataUtil.class);

	static ByteBufAllocator allocator = null;

	static {
		allocator = PooledByteBufAllocator.DEFAULT;
	}

	/**
	 * send message
	 * 
	 * @param ch
	 * @param datas
	 * @param waitWriteble 是否等待可写发送，true 等待 false：直接返回
	 * @throws Exception
	 */
	public static boolean sendData(Channel ch, IBytesBuild netData, boolean waitWriteble) throws Exception {
		boolean result = false;
		if (netData == null)
			throw new NetSendDataIsNull();
		byte[] datas = netData.buildBytes();
		if (datas == null)
			throw new NetBuildBytesNullException();

		ByteBuf buff = null;
		try {
			if (ch != null) {
				if (!waitWriteble && !ch.isWritable()) {
					String portStr = Integer.toString(((InetSocketAddress) ch.localAddress()).getPort());
					throw new IOException(StringUtil.getMsgStr("当前[{}]-[{}]连接发送IO不可写", portStr, ch.remoteAddress()));
				}
				buff = allocator.directBuffer(datas.length);
				buff.writeBytes(datas);
				buff.retain();
				ch.writeAndFlush(buff);
				result = true;
			}
		} finally {
			if (buff != null && buff.refCnt() > 0)// 大于0才释放
				ReferenceCountUtil.release(buff);
		}
		return result;
	}
}

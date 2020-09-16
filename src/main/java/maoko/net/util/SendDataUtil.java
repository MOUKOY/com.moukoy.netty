package maoko.net.util;

import java.io.IOException;
import java.io.NotActiveException;
import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import maoko.common.StringUtil;
import maoko.common.exception.DataIsNullException;
import maoko.net.exception.NetSendDataIsNull;
import maoko.net.ifs.IBytesBuild;

/**
 * 数据发送助手
 *
 * @author fanpei
 * @date 2018-09-09 23:03
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
     * @param netData
     * @param waitWriteble 是否等待可写发送，true 等待 false：直接返回
     * @throws Exception
     */
    public static boolean sendData(Channel ch, IBytesBuild netData, boolean waitWriteble) throws Exception {
        if (netData == null)
            throw new NetSendDataIsNull();
        byte[] datas = netData.buildBytes();
        return sendData(ch, datas, waitWriteble);

    }

    /**
     * send message
     *
     * @param ch
     * @param datas
     * @param waitWriteble 是否等待可写发送，true 等待 false：直接返回
     * @throws Exception
     */
    public static boolean sendData(Channel ch, byte[] datas, boolean waitWriteble) throws Exception {
        boolean result = false;
        if (ch != null) {
            if (!ch.isActive())
                throw new NotActiveException("当前连接不活跃或未连接,不可用");

            if (datas == null)
                throw new DataIsNullException("发送byte[]数据为空");

            ByteBuf buff = null;
            try {
                if (!waitWriteble && !ch.isWritable()) {
                    String portStr = Integer.toString(((InetSocketAddress) ch.localAddress()).getPort());
                    throw new IOException(StringUtil.getMsgStr("当前[{}]-[{}]连接发送IO不可写", portStr, ch.remoteAddress()));
                }
                //buff = allocator.directBuffer(datas.length);
                buff = ch.alloc().buffer(datas.length);
                buff.writeBytes(datas);
                buff.retain();
                ch.writeAndFlush(buff);
                result = true;

            } finally {
                NetBuffRealse.realse(buff);
            }
        }
        return result;
    }

}

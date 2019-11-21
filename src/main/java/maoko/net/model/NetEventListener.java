package maoko.net.model;

import java.util.EventListener;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.common.tdPool.TdFixedPoolExcCenter;
import maoko.net.conf.CongfigServer;
import maoko.net.ifs.IByteBuff;
import maoko.net.ifs.IBytesBuild;
import maoko.net.ifs.ISendData;
import maoko.net.protocol.IDecoder;
import maoko.net.protocol.IProtocol;
import maoko.net.protocol.MyDecoder;
import maoko.net.sconectclient.ShortConectCallback;

/**
 * 网络事件监视器
 *
 * @author fanpei
 */
public abstract class NetEventListener<Protocol extends IProtocol> implements EventListener, ISendData {
    private static final IWriteLog log = new Log4j2Writer(NetEventListener.class);
    private static final TdFixedPoolExcCenter TD_FIXED_POOL_EXC_CENTER = new TdFixedPoolExcCenter(CongfigServer.RECVHANDLETDCOUNT);
    protected IDecoder<Protocol> decoder;
    protected ShortConectCallback<Protocol> callback;
    protected CusNetSource channel;// 连接链路

/*    public NetEventListener() {
    }*/

    public NetEventListener(SocketChannel ch) {
        super();
        this.channel = new CusNetSource(ch);
        this.decoder = new MyDecoder(this);
    }

    public void updateChanel(Channel ch) {
        this.channel.setNettyChanel(ch);
    }

    public void setCallback(ShortConectCallback callback) {
        this.callback = callback;
    }

    public CusNetSource getNetSource() {
        return channel;
    }


    public void release() {
        if (checkChannel())
            channel.close();
    }

    public void decode(IByteBuff in) {
        log.debug("[REMOTE ADDR]:{}  RECV_DATA: {}", channel.getRIpPort(),
                in.printHex());
        decoder.deCode(in);
        TD_FIXED_POOL_EXC_CENTER.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (decoder.hasDecDatas()) {
                        Protocol protocol = null;
                        //fanpei 2019.11.21重大更新
                        //接收数据处理逻辑每次只取一个数据，导致数据堆积问题;
                        // 将if（取数据不为空）改为while(取数据不为空)
                        while ((protocol = decoder.popData()) != null) {
                            if (null != callback)
                                callback.dataReciveEvent(protocol);
                            else
                                dataReciveEvent(protocol);
                        }
                    }
                } catch (Exception e) {
                    log.error("出数据队列发生错误", e);
                }
            }
        });

    }

    /**
     * 获取一个协议对象
     *
     * @return
     */
    public abstract Protocol getProtocol();

    /**
     * 获取监听类型(字符串形式)
     *
     * @return
     */
    public abstract String getListenerTypeStr();

    /**
     * 网络连接建立
     */
    public abstract void chanelConect();

    /**
     * 接收数据处理
     *
     * @param iprotocol
     */
    public abstract void dataReciveEvent(Protocol iprotocol);

    /**
     * 连接断开
     */
    public abstract void closeEvent();


    @Override
    public boolean sendData(byte[] datas) throws Exception {
        if (checkChannel())
            return channel.sendData(datas);
        return false;
    }

    @Override
    public boolean sendData(byte[] netdatas, boolean isWait) throws Exception {
        if (checkChannel())
            return channel.sendData(netdatas, isWait);
        return false;
    }

    @Override
    public boolean sendData(IBytesBuild data) throws Exception {
        if (checkChannel())
            return channel.sendData(data);
        return false;
    }

    @Override
    public boolean sendData(IBytesBuild netdata, boolean isWait) throws Exception {
        if (checkChannel())
            return channel.sendData(netdata, isWait);
        return false;
    }

    private boolean checkChannel() {
        return channel != null;
    }

}

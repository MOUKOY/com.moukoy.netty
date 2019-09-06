package maoko.net.model;

import java.util.EventListener;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import maoko.net.ifs.IByteBuff;
import maoko.net.ifs.IBytesBuild;
import maoko.net.ifs.ISendData;
import maoko.net.protocol.IDecoder;
import maoko.net.protocol.IProtocol;
import maoko.net.protocol.MyDecoder;

/**
 * 网络事件监视器
 *
 * @author fanpei
 */
public abstract class NetEventListener implements EventListener, ISendData {
    protected IDecoder decoder;
    protected CusNetSource channel;// 连接链路

    public NetEventListener() {
    }

    public NetEventListener(SocketChannel ch) {
        super();
        this.channel = new CusNetSource(ch);
        this.decoder = new MyDecoder(this);
    }

    public CusNetSource getNetSource() {
        return channel;
    }

    public void updateChanel(Channel ch) {
        this.channel.setNettyChanel(ch);
    }

    public void release() {
        if (checkChannel())
            channel.close();
    }

    /**
     * 获取一个协议对象
     *
     * @return
     */
    public abstract IProtocol getProtocol();

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
     * @param buff
     */
    public abstract void dataReciveEvent(IByteBuff buff);

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

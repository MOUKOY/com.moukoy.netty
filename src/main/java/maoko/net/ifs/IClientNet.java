package maoko.net.ifs;

import maoko.net.model.ClientChanel;
import maoko.net.model.NetEventListener;
import maoko.net.sconectclient.ShortConectCallback;

public interface IClientNet {

    /**
     * 启动网络监听服务
     *
     * @return
     */
    // void start();

    /**
     * 关闭网络服务和连接
     *
     * @return
     */
    void close() /* throws CusException */;

    /**
     * 向服务端发起长连接连接请求
     *
     * @param ip
     * @param port
     * @return
     */
    void connectServer(String ip, int port);

    /**
     * 向长连接发送数据
     *
     * @param data
     * @throws Exception
     */
    boolean sendDataToSvr(IBytesBuild data) throws Exception;

    /**
     * 向服务端发送数据-短连接
     *
     * @param ip
     * @param port
     * @param data
     * @param callback 短连接回调
     * @throws Exception
     */
    boolean sendDataToSvr(String ip, int port, IBytesBuild data, ShortConectCallback callback) throws Exception;


    boolean isConected();
}

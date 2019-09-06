package maoko.netty.frame;

import maoko.net.conf.IPAddrCallback;
import maoko.net.ifs.IByteBuff;
import maoko.net.model.CusHostAndPort;
import maoko.net.model.NetEventListener;
import maoko.net.protocol.IProtocol;
import org.junit.Before;
import org.junit.Test;

import maoko.net.NettyFrame;
import maoko.net.lconectserver.LConectServer;

/**
 * 网络客户端程序
 *
 * @author fanpei
 */
public class NetSvrApp {
    private LConectServer server;

    @Before
    public void before() {
        try {
            NettyFrame.initSysArgs();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void start() {
        try {
            CusHostAndPort host = new CusHostAndPort("127.0.0.1", 8188);
            NetEventListener listener = new MyNetEventListener();
            IPAddrCallback[] callbacks = {new IPAddrCallback(host, listener)};
            server = new LConectServer(callbacks);
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static class MyNetEventListener extends NetEventListener {
        @Override
        public IProtocol getProtocol() {
            return null;
        }

        @Override
        public String getListenerTypeStr() {
            return null;
        }

        @Override
        public void chanelConect() {

        }

        @Override
        public void dataReciveEvent(IByteBuff buff) {

        }

        @Override
        public void closeEvent() {

        }
    }

}

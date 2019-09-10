package maoko.netty.frame;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import maoko.MyProtocol;
import maoko.net.conf.IPAddrCallback;
import maoko.net.ifs.IByteBuff;
import maoko.net.ifs.IListenerCreator;
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
public class LConnectServerTest {
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
            CusHostAndPort[] hosts = new CusHostAndPort[1];
            hosts[0] = new CusHostAndPort("127.0.0.1", 8888);
            server = new LConectServer(hosts, new MyCreator());
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static class MyCreator implements IListenerCreator {
        @Override
        public NetEventListener getListener(NioSocketChannel ch) {
            return new MyNetEventListener(ch);
        }
    }

    private static class MyNetEventListener extends NetEventListener<MyProtocol> {

        public MyNetEventListener(SocketChannel ch) {
            super(ch);
        }

        @Override
        public MyProtocol getProtocol() {
            return new MyProtocol();
        }

        @Override
        public String getListenerTypeStr() {
            return null;
        }

        @Override
        public void chanelConect() {

        }

        @Override
        public void dataReciveEvent(MyProtocol iprotocol) {

        }

        @Override
        public void closeEvent() {

        }
    }

}

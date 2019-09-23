package maoko.netty.frame;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import maoko.MyProtocol;
import maoko.common.exception.NotContainException;
import maoko.net.NettyFrame;
import maoko.net.conf.IPAddrCallback;
import maoko.net.ifs.IByteBuff;
import maoko.net.ifs.IListenerCreator;
import maoko.net.lconectserver.LConectServer;
import maoko.net.model.CusHostAndPort;
import maoko.net.model.NetEventListener;
import maoko.net.protocol.IProtocol;
import maoko.net.sconectclient.SConectClient;
import org.junit.Before;
import org.junit.Test;

/**
 * 网络客户端程序
 *
 * @author fanpei
 */
public class SConClientTest {
    private SConectClient client;

    //@Before
    public void before() {
        try {
            NettyFrame.initSysArgs();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //@Test
    public void start() {
        try {
            client = new SConectClient(new MyCreator());
            client.connectServer("127.0.0.1", 8888);
            Thread.sleep(100000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            NettyFrame.initSysArgs();
            SConectClient client = new SConectClient(new MyCreator());
            client.connectServer("127.0.0.1", 8888);
            Thread.sleep(100000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static class MyNetEventListener extends NetEventListener {
        public MyNetEventListener(SocketChannel ch) {
            super(ch);
        }

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
        public void dataReciveEvent(IProtocol iprotocol) {

        }

        @Override
        public void closeEvent() {

        }
    }

    private static class MyCreator implements IListenerCreator {
        @Override
        public NetEventListener getListener(NioSocketChannel ch) {
            return new  MyListener(ch);
        }
    }

    private static class MyListener extends NetEventListener<MyProtocol> {

        public MyListener(SocketChannel ch) {
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

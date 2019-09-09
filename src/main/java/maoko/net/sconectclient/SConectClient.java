package maoko.net.sconectclient;

import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.DefaultThreadFactory;
import maoko.common.conf.ConfException;
import maoko.common.exception.DataIsNullException;
import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.net.conf.Conf;
import maoko.net.conf.ConfigClient;
import maoko.net.exception.ConectSeverException;
import maoko.net.ifs.IBytesBuild;
import maoko.net.ifs.IClientNet;
import maoko.net.ifs.IListenerCreator;
import maoko.net.model.ClientChanel;
import maoko.net.model.NetEventListener;

public class SConectClient implements IClientNet {

    private static final IWriteLog log = new Log4j2Writer(SConectClient.class);

    private static IListenerCreator creator = null;
    /**
     * 心跳包数据
     */
    protected static IBytesBuild heartData;// 客户端检测服务端是否断开用

    public static IBytesBuild getHeartData() {
        return heartData;
    }

    private ClientCheckConTdStore longConTdStore;
    // private TdCachePoolExctor connectPool;// 固定线程池执行器
    private EventLoopGroup workgroup;
    private Bootstrap bstrap;
    private ClientChannelStore store;

    /**
     * 初始化
     *
     * @param creator
     * @throws DataIsNullException
     * @throws IOException
     * @throws ConfException
     */
    public static void init(IListenerCreator creator)
            throws DataIsNullException, ConfException, IOException {
        if (creator == null)
            throw new DataIsNullException("the creator is null");

        SConectClient.creator = creator;
        ConfigClient.init();
        Conf.nettySetting(Conf.BUFFCHECKLEVEL);
    }

    /**
     * 设置心跳数据，设置后重连检测会定时发送此数据到服务端，若不设置心跳数据，则不进行发送。此心跳数据指的是业务心跳数据
     *
     * @param heartData
     * @throws DataIsNullException
     */
    public static void setHeartData(IBytesBuild heartData) throws DataIsNullException {
        if (heartData == null)
            throw new DataIsNullException("the heartdata is null");
        SConectClient.heartData = heartData;
    }

    public SConectClient() {
        System.setProperty("io.netty.leakDetection.maxRecords", "100");
        System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);// 测试级别

        store = new ClientChannelStore();
        // connectPool = new TdCachePoolExctor();// 执行重连
        longConTdStore = new ClientCheckConTdStore();
        bstrap = new Bootstrap();
        // workgroup = new NioEventLoopGroup(1);
        workgroup = new NioEventLoopGroup(4, new DefaultThreadFactory("client", true));
        // bstrap.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 128 *
        // 1024);
        // bstrap.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 64 * 1024);
        bstrap.group(workgroup).channel(NioSocketChannel.class);
        bstrap.option(ChannelOption.TCP_NODELAY, true); // 设置立即发送;
        bstrap.option(ChannelOption.AUTO_READ, true);
        bstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        bstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bstrap.handler(new MyClientInittializer());
    }

    @Override
    public ClientChanel connectServer(String ip, int port) {
        ClientChanel ch = null;
        try {
            ch = buildConnect(ip, port, true, null);
        } catch (Exception e) {
            log.warn("向[{}:{}]建立长连接发生错误", ip, port, e);
        }
        return ch;
    }

    @Override
    public void close() {
        try {
            longConTdStore.shutdownNow();
            // 循环遍历关闭所有长连接
            store.closeAllConnect();
        } catch (Exception e) {
            if (workgroup != null)
                workgroup.shutdownGracefully();
        }
    }

    @Override
    public boolean sendDataToSvr(String ip, int port, IBytesBuild data) throws Exception {
        try {
            buildConnect(ip, port, false, new ClientSendDataImp(data));
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean sendDataToSvr(NetEventListener listener, IBytesBuild data) throws Exception {
        return listener.sendData(data);
    }

    public static final String READERHANDLER = "readerHandler";

    /**
     * handler初始化器
     *
     * @author fanpei
     */
    private class MyClientInittializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {

            ChannelPipeline pipeline = ch.pipeline();
            NetEventListener listener = creator.getListener(ch);
            ClientHandler handler = new ClientHandler(listener, store);
            pipeline.addFirst(READERHANDLER, handler);
        }
    }

    /**
     * @param ip
     * @param port
     * @param keep      true 长连接 false 短连接
     * @param isend     发送数据操作 可为空
     * @return 长连接时向外返回的channel
     * @throws Exception
     */
    private ClientChanel buildConnect(String ip, int port, boolean keep, IClientSendData isend)
            throws Exception {
        ChannelFuture f = bstrap.connect(ip, port);// 连接服务端
        // ClientConectMonitor monitor = new ClientConectMonitor();
        ClientSyncFuture clFuture = new ClientSyncFuture(ip, port, keep, bstrap, f, longConTdStore, isend);
        f.addListener(clFuture);
        f.await();
        final ClientChanel outchanle = clFuture.syncwait();
        // monitor.connectWait(timeout);

        if (!f.isSuccess() && !keep)// 短连接抛出异常
            throw new ConectSeverException("建立连接超时,请检查网络连接或服务是否开启", f.cause());
        return outchanle;
    }

}

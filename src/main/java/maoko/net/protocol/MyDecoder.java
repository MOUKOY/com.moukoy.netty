package maoko.net.protocol;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.net.ifs.IByteBuff;
import maoko.net.model.NetEventListener;

/**
 * 协议解码器
 *
 * @author fanpei
 */
public class MyDecoder<Protocol extends IProtocol> implements IDecoder {
    private static final IWriteLog log = new Log4j2Writer(MyDecoder.class);

    private Queue<Protocol> resluts = null;// 解析结果
    private Protocol protocol = null;// 当前数据

    public MyDecoder(NetEventListener<Protocol> listener) {
        this.protocol = listener.getProtocol();
        this.resluts = new ConcurrentLinkedDeque<>();
    }

    @Override
    public Protocol popData() {
        return resluts.poll();
    }

    @Override
    public void deCode(IByteBuff in) {
        try {
            while (in.hasData()) {

                if (!protocol.headerReadEnough())
                    protocol.readHeader(in);

                if (protocol.headerReadEnough()) {// 头读完

                    if (!protocol.dataReadEnough())
                        protocol.readData(in);

                    if (protocol.dataReadEnough()) {// 数据读完

                        if (!protocol.enderReadEnough())
                            protocol.readEnd(in);

                        if (protocol.enderReadEnough()) {// 尾部读取完
                            protocol.validate();
                            protocol.parse();
                            Protocol tmpProtocol = (Protocol) protocol.copyProtocol();
                            resluts.add(tmpProtocol);
                            protocol.clear();
                        }
                    }
                }
            }
        } catch (Exception e) {
            protocol.clear();
            log.error("解析数据报文错误", e);
        }
    }

    @Override
    public boolean hasDecDatas() {
        return !resluts.isEmpty();
    }
}

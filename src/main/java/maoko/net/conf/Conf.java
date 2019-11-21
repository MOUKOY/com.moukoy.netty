package maoko.net.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import io.netty.util.ResourceLeakDetector;
import maoko.common.StringUtil;
import maoko.common.conf.ConfException;

/**
 * 配置文件基类
 *
 * @author fanpei
 * @date 2018-09-09 15:28
 */
public abstract class Conf {
    @Deprecated
    public static final String CONF_SERVERIPS = "serverips";//接口已改成由上层初始化，底层不做配置
    public static final String CONF_MAXCLIENTS = "maxClients";
    public static final String CONF_DATARECVINTERVAL = "dataRecvInterval";
    public static final String CONF_PARENTGROUPTDCOUNT = "parentgroupTdCount";
    public static final String CONF_CHILDGROUPTDCOUNT = "childgroupTdCount";
    public static final String CONF_DATARECIVETDCOUNT = "recvHandleTdCount";
    public static final String CONF_BUFFCHECKLEVEL = "buffCheckLevel";
    public static final String CONF_FORWARDSERVERIP = "forwardServerIp";

    public static ResourceLeakDetector.Level BUFFCHECKLEVEL = ResourceLeakDetector.Level.DISABLED;
    /**
     * 接收数据线程处理个数
     */
    public static int RECVHANDLETDCOUNT = 2;

    public static List<IPAddrPackage> getHosts(String key, String values) throws ConfException {
        List<IPAddrPackage> hosts = null;
        String[] ipStrings = values.split(",");
        if (ipStrings == null)
            throw new ConfException(
                    StringUtil.getMsgStr("this key {} is  configure error,it's not correct fomat", key));
        hosts = new ArrayList<>(ipStrings.length);
        for (String v : ipStrings) {
            if (!StringUtil.isStrNullOrWhiteSpace(v)) {
                hosts.add(new IPAddrPackage(v));
            }
        }
        if (hosts.size() == 0)
            throw new ConfException("does not have any server host needs to linten!");
        return hosts;
    }

    /**
     * 初始化公共配置
     *
     * @param v
     */
    public static void init(Entry<String, String> v) {
        switch (v.getKey()) {
            case CONF_BUFFCHECKLEVEL:
                if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
                    BUFFCHECKLEVEL = ResourceLeakDetector.Level.valueOf(v.getValue());
                }
                break;
            case CONF_DATARECIVETDCOUNT:
                if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
                    RECVHANDLETDCOUNT = Integer.parseInt(v.getValue());
                }
                break;
        }
    }

    /**
     * netty 配置 DISABLED>SIMPLE>ADVANCED>PARANOID 性能排序,监控级别越来越高，
     *
     * @param level 内存泄露监控级别
     */
    public static void nettySetting(ResourceLeakDetector.Level level) {

        System.setProperty("io.netty.noUnsafe", "false");// 屏蔽 jdk.internal.misc.Unsafe.allocateUninitializedArray(int):
        // unavailable java.lang.ClassNotFoundException:
        // jdk.internal.misc.Unsafe

        switch (level) {
            case PARANOID:// 最高级测试监控
            case ADVANCED:// 高级检测
                System.setProperty("io.netty.leakDetection.maxRecords", "100");
                System.setProperty("io.netty.leakDetection.acquireAndReleaseOnly", "true");
                ResourceLeakDetector.setLevel(level);
                break;
            default:// 默认级别
                ResourceLeakDetector.setLevel(level);
                break;
        }
    }
}

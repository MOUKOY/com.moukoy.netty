package maoko.net.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import io.netty.util.ResourceLeakDetector;
import soft.common.StringUtil;
import soft.common.conf.ConfException;

/**
 * 配置文件基类
 * 
 * @author fanpei
 * @date 2018-09-09 15:28
 *
 */
public abstract class Conf {
	public static final String CONF_SERVERIPS = "serverips";
	public static final String CONF_MAXCLIENTS = "maxClients";
	public static final String CONF_DATARECVINTERVAL = "dataRecvInterval";
	public static final String CONF_PARENTGROUPTDCOUNT = "parentgroupTdCount";
	public static final String CONF_CHILDGROUPTDCOUNT = "childgroupTdCount";
	public static final String CONF_BUFFCHECKLEVEL = "buffCheckLevel";
	public static final String CONF_FORWARDSERVERIP = "forwardServerIp";
	
	public static ResourceLeakDetector.Level BUFFCHECKLEVEL = ResourceLeakDetector.Level.DISABLED;

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
		if (CONF_BUFFCHECKLEVEL.equals(v.getKey()))
			if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
				BUFFCHECKLEVEL = ResourceLeakDetector.Level.valueOf(v.getValue());
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

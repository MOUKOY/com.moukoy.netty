package maoko.net.conf;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import soft.common.PropertiesUtil;
import soft.common.StringUtil;
import soft.common.conf.ConfException;

/**
 * 服务端配置
 * 
 * @author fanpei
 * @date 2018-09-09 15:11
 *
 */
public class CongfigServer extends Conf {

	/**
	 * 监听列表
	 */
	public static List<IPAddrPackage> HOSTS;
	/**
	 * 最大客户端连接
	 */
	public static int MAXCLIENTS = Integer.MAX_VALUE;
	/**
	 * 最大未收到数据断开连接间隔 （单位：秒） 为0时代表不断开
	 */
	public static int CHANLEDATARECVINTERVAL = 0;

	/**
	 * boss线程个数
	 */
	public static int PARENTGROUPTDCOUNT = 2;

	/**
	 * work线程个数
	 */
	public static int CHILDGROUPTDCOUNT = 2;
	/**
	 * 转发服务器IP
	 */
	public static String FORWARDSERVERIP ="";
	public static void init() throws ConfException, IOException {
		Map<String, String> values = PropertiesUtil.getAllProperties(ConfReader.confPath);
		if (values == null || values.isEmpty())
			throw new ConfException(StringUtil.getMsgStr("this conf file {} read error", ConfReader.confPath));

		for (Entry<String, String> v : values.entrySet()) {
			switch (v.getKey()) {
			case CONF_SERVERIPS:
				HOSTS = getHosts(v.getKey(), v.getValue());
				break;
			case CONF_MAXCLIENTS:
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					MAXCLIENTS = Integer.parseInt(v.getValue());
				}
				break;

			case CONF_DATARECVINTERVAL:
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					CHANLEDATARECVINTERVAL = Integer.parseInt(v.getValue());
				}
				break;

			case CONF_PARENTGROUPTDCOUNT:
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					PARENTGROUPTDCOUNT = Integer.parseInt(v.getValue());
				}
				break;
			case CONF_CHILDGROUPTDCOUNT:
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					CHILDGROUPTDCOUNT = Integer.parseInt(v.getValue());
				}
				break;
			case CONF_FORWARDSERVERIP:
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					FORWARDSERVERIP = v.getValue();
				}
				break;
				
			default:
				Conf.init(v);
				break;

			}
		}
	}

}

package soft.conf;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import soft.common.PropertiesUtil;
import soft.common.StringUtil;
import soft.net.exception.ConfException;
import soft.net.model.CusHostAndPort;

/**
 * 服务端配置
 * 
 * @author fanpei
 * @date 2018-09-09 15:11
 *
 */
public class CongfigServer extends Conf {

	public static List<CusHostAndPort> HOSTS;
	public static int MAXCLIENTS = Integer.MAX_VALUE;// 最大客户端连接

	public static void init() throws ConfException, IOException {
		Map<String, String> values = PropertiesUtil.getAllProperties(ConfReader.confPath);
		if (values == null || values.isEmpty())
			throw new ConfException(StringUtil.getMsgStr("this conf file {} read error", ConfReader.confPath));
		for (Entry<String, String> v : values.entrySet()) {

			switch (v.getKey()) {
			case "serverips":
				HOSTS = getHosts(v.getKey(), v.getValue());
				break;
			case "maxClients":
				if (!StringUtil.isStrNullOrWhiteSpace(v.getValue())) {
					MAXCLIENTS = Integer.parseInt(v.getValue());
				}
				break;
			default:
				break;

			}
		}
	}

}

package soft.conf;

import java.util.ArrayList;
import java.util.List;

import soft.common.StringUtil;
import soft.net.exception.ConfException;
import soft.net.model.CusHostAndPort;

/**
 * 配置文件基类
 * 
 * @author fanpei
 * @date 2018-09-09 15:28
 *
 */
public abstract class Conf {

	public static List<CusHostAndPort> getHosts(String key, String values) throws ConfException {
		List<CusHostAndPort> hosts = null;
		String[] ipStrings = values.split(",");
		if (ipStrings == null)
			throw new ConfException(
					StringUtil.getMsgStr("this key {} is  configure error,it's not correct fomat", key));
		hosts = new ArrayList<>(ipStrings.length);
		for (String v : ipStrings) {
			String[] ips = v.split(":");
			if (ips == null || ips.length != 2)
				throw new ConfException(
						StringUtil.getMsgStr("this key {} is  configure error,it's not correct fomat", key));
			if (!StringUtil.isStrNullOrWhiteSpace(ips[1])) {
				hosts.add(new CusHostAndPort(ips[0], Integer.parseInt(ips[1])));
			}

		}

		return hosts;
	}
}

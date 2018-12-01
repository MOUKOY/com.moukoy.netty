package soft.net.conf;

import java.util.ArrayList;
import java.util.List;

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

		return hosts;
	}
}

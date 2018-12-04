package soft.net.conf;

import soft.common.StringUtil;
import soft.common.conf.ConfException;
import soft.net.model.CusHostAndPort;

public class IPAddrPackage {
	private CusHostAndPort host;
	private String ListenerClass;

	public CusHostAndPort getHost() {
		return host;
	}

	public String getListenerClass() {
		return ListenerClass;
	}

	public IPAddrPackage(String hostListener) throws ConfException {
		String[] ips = hostListener.split(":");
		if (ips == null || ips.length < 2)
			throw new ConfException(
					StringUtil.getMsgStr("this key {} is  configure error,it's not correct fomat", hostListener));
		this.host = new CusHostAndPort(ips[0], Integer.parseInt(ips[1]));
		if (ips.length == 3)
			this.ListenerClass = ips[2];
	}
}

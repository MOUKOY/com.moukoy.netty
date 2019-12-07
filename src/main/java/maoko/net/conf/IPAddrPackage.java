package maoko.net.conf;

import maoko.common.StringUtil;
import maoko.common.conf.ConfException;
import maoko.common.model.net.CusHostAndPort;

@Deprecated
public class IPAddrPackage {
	private CusHostAndPort host;
	@Deprecated
	private String ListenerClassPath;// 监听类类路径

	public CusHostAndPort getHost() {
		return host;
	}

	public String getListenerClassPath() {
		return ListenerClassPath;
	}

	public IPAddrPackage(String hostListener) throws ConfException {
		String[] ips = hostListener.split(":");
		if (ips == null || ips.length < 2)
			throw new ConfException(
					StringUtil.getMsgStr("this key {} is  configure error,it's not correct fomat", hostListener));
		this.host = new CusHostAndPort(ips[0], Integer.parseInt(ips[1]));
		if (ips.length == 3)
			this.ListenerClassPath = ips[2];
	}
}

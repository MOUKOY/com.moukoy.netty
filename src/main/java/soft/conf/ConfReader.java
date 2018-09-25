package soft.conf;

import java.io.IOException;

import soft.common.file.HDFSPathUtil;
import soft.net.exception.ConfException;

public class ConfReader {
	private static final String CONF = "config/net.properties";
	public static String confPath;

	/**
	 * 配置文件初始化
	 * 
	 * @param runDir 运行根目录
	 * @throws IOException
	 * @throws ConfException
	 */
	public static void init(String runDir) throws IOException, ConfException {
		confPath = HDFSPathUtil.combinePath(runDir, CONF);

	}

}

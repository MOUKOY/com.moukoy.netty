package soft.net.conf;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import soft.common.conf.ConfException;
import soft.common.exception.DataIsNullException;
import soft.common.exception.OstypeMissWatchException;
import soft.common.file.PathUtil;
import soft.common.system.AppRunPathUitl;

public class ConfReader {
	private static final String CONF = "config/net.properties";
	public static String confPath;

	/**
	 * 配置文件初始化
	 * 
	 * @param runDir 运行根目录
	 * @throws IOException
	 * @throws ConfException
	 * @throws DataIsNullException
	 * @throws OstypeMissWatchException
	 */
	public static void init() throws IOException, ConfException, OstypeMissWatchException, DataIsNullException {
		String runDir = AppRunPathUitl.getRunPath(ConfReader.class);

		confPath = PathUtil.combinePath(runDir, CONF);
		File confile = new File(confPath);
		if (!confile.exists()) {// 不存在使用默认配置
			confPath = URLDecoder.decode(ConfReader.class.getClassLoader().getResource(CONF).getPath(), "utf-8");
		}

	}

}

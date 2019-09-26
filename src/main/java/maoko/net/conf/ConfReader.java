package maoko.net.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import maoko.common.conf.ConfException;
import maoko.common.exception.DataIsNullException;
import maoko.common.exception.OstypeMissWatchException;
import maoko.common.file.PathUtil;
import maoko.common.log.Log4j2Writer;
import maoko.common.system.AppRunPathUitl;

public class ConfReader {
	private static final String CONF = "config/net.properties";
	public static String confPath;

	/**
	 * 配置文件初始化
	 *
	 * @throws IOException
	 * @throws ConfException
	 * @throws DataIsNullException
	 * @throws OstypeMissWatchException
	 */
	public static void init() throws IOException, ConfException, OstypeMissWatchException, DataIsNullException {
		String runDir = AppRunPathUitl.getAppRunPathNew();

		confPath = PathUtil.combinePath(runDir, CONF);
		File confile = new File(confPath);
		if (!confile.exists()) {// 不存在使用默认配置
			System.err.println(CONF + " is not found,sys will use default config");
			InputStream in = Log4j2Writer.class.getClassLoader().getResourceAsStream(CONF);
			FileUtils.copyInputStreamToFile(in, confile);
		}

	}

}

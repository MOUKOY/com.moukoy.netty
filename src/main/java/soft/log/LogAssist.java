package soft.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import soft.common.StringUtil;
import soft.common.file.HDFSPathUtil;
import soft.model.system.LogLevel;

public class LogAssist {
	public final static String CONFIGFILE = "/config/log4j2.xml";

	/**
	 * 初始化log4j
	 * 
	 * @param RunPath
	 * @throws IOException
	 */
	public static void init(String RunPath) throws Exception {
		try {
			String filepath = HDFSPathUtil.combinePath(RunPath, CONFIGFILE);

			File file = new File(filepath);
			if (!file.exists())
				throw new FileNotFoundException(filepath + " is not exist");

			// InputStream in =
			// ConfigUtil.class.getClassLoader().getResourceAsStream("log4j2.xml");
			// FileCopy.copy(in, filepath, false);
			System.setProperty("log4j.configurationFile", file.toURI().toURL().toString());
			// 异步模式
			// System.setProperty("Log4jContextSelector",
			// "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

			// ConfigurationSource source = new ConfigurationSource(new
			// FileInputStream(file));
			// LoggerContext context = Configurator.initialize(null, source);
			// XmlConfiguration xmconfig = new XmlConfiguration(context, source);
			// context.start(xmconfig);

		} catch (Exception e) {
			throw new Exception("日志初始化失败", e);
		}

	}

	public static void stop() {
		LogManager.shutdown();
	}

	public static Logger getLoger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	public static Logger getLoger(String dscp) {
		return LogManager.getLogger(dscp);
	}

	/**
	 * 打印堆栈
	 * 
	 * @param log
	 * @param e
	 * @param level 0:debug 1:info 2:warn 3:errr
	 */
	public static String printStackTrace_cus(String dscr, Exception e) {

		StackTraceElement[] errs = e.getStackTrace();
		StringBuilder errSb = new StringBuilder((errs == null ? 0 : errs.length) + 2 + dscr.length());
		errSb.append(dscr);
		if (errs != null) {
			errSb.append(System.lineSeparator()).append(" [-堆栈信息]");
			for (StackTraceElement stackTraceElement : errs) {
				errSb.append(System.lineSeparator()).append(stackTraceElement.toString());
			}
		}
		if (e.getCause() != null && !StringUtil.isStrNullOrWhiteSpace(e.getCause().getMessage())) {
			errSb.append(System.lineSeparator()).append("[-detailMessage]");
			errSb.append(System.lineSeparator()).append(e.getCause().getMessage());
		} else if (!StringUtil.isStrNullOrWhiteSpace(e.getMessage())) {
			errSb.append(System.lineSeparator()).append("[-Message]");
			errSb.append(System.lineSeparator()).append(e.getMessage());
		}

		return errSb.toString();

	}

	/**
	 * 打印堆栈
	 * 
	 * @param log
	 * @param e
	 * @param level 0:debug 1:info 2:warn 3:errr
	 */
	public static void printStackTrace(Logger log, Exception e, LogLevel level) {
		switch (level) {
		case DEBUG:
			log.debug(e);
			break;
		case INFO:
			log.info(e);
			break;
		case WARN:
			log.warn(e);
			break;
		case ERROR:
			log.error(e);
			break;
		default:
			break;
		}

	}

	public static void print(Logger log, LogLevel level, String message, Object... params) {
		if (log == null)
			return;
		switch (level) {
		case DEBUG:
			log.debug(message, params);
			break;
		case INFO:
			log.info(message, params);
			break;
		case WARN:
			log.warn(message, params);
			break;
		case ERROR:
			log.error(message, params);
			break;
		default:
			break;
		}
	}

	public static void print(Logger log, LogLevel level, Message msg) {
		if (log == null || msg == null)
			return;
		String message = msg.getFormattedMessage();
		switch (level) {
		case DEBUG:
			log.debug(message);
			break;
		case INFO:
			log.info(message);
			break;
		case WARN:
			log.warn(message);
			break;
		case ERROR:
			log.error(message);
			break;
		default:
			break;
		}
	}

}

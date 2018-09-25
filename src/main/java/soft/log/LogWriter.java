package soft.log;

import org.apache.logging.log4j.Logger;

import soft.ifs.IWriteLog;
import soft.model.system.LogLevel;

public class LogWriter implements IWriteLog {

	private Logger log;

	public LogWriter(Class<?> clas) {
		log = LogAssist.getLoger(clas);
	}

	public LogWriter(String dscp) {
		log = LogAssist.getLoger(dscp);
	}

	private void print(LogLevel lev, String dscrp, Object... e) {
		if (log != null && log.isDebugEnabled()) {
			switch (lev) {
			case DEBUG:
				log.debug(dscrp, e);
				break;
			case WARN:
				log.warn(dscrp, e);
				break;
			case ERROR:
				log.error(dscrp, e);
				break;
			default:
				log.info(dscrp, e);
				break;
			}
		}
	}

	@Override
	public void error(String dscrp, Object... e) {
		print(LogLevel.ERROR, dscrp, e);
	}

	@Override
	public void error(Throwable e) {
		print(LogLevel.ERROR, "", e);
	}

	@Override
	public void debug(String dscrp, Object... e) {
		print(LogLevel.DEBUG, dscrp, e);
	}

	@Override
	public void warn(String dscrp, Object... e) {
		print(LogLevel.WARN, dscrp, e);
	}

	@Override
	public void info(String dscrp, Object... e) {
		print(LogLevel.INFO, dscrp, e);
	}

}

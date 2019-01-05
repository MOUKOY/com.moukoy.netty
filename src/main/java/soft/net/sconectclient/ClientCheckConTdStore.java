package soft.net.sconectclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import soft.net.conf.ConfigClient;

/**
 * 自动重连检测线程仓库
 * 
 * @author fanpei
 * @date 2018-09-10 03:44
 *
 */
public class ClientCheckConTdStore {
	private ScheduledExecutorService scheduledSercice;// 周期任务执行器
	private List<ClientCheckConTd> tds;

	public ScheduledExecutorService getServervice() {
		return scheduledSercice;
	}

	/**
	 * 初始化
	 * 
	 */
	public ClientCheckConTdStore() {
		scheduledSercice = Executors.newScheduledThreadPool(ConfigClient.CHECKTDPOOLNUMBER);
		tds = new ArrayList<>();
	}

	/**
	 * 执行线程
	 * 
	 * @param tdThread
	 */
	public void excute(ClientCheckConTd tdThread) {
		addChanel(tdThread);
		scheduledSercice.scheduleWithFixedDelay(tdThread, ConfigClient.SENDDATA_TIMEOUT,
				ConfigClient.CONNET_RETRY_INTERVAL, TimeUnit.MILLISECONDS);
	}

	public void addChanel(ClientCheckConTd td) {
		tds.add(td);
	}

	public void shutdownNow() {
		for (ClientCheckConTd td : tds) {
			td.closeConect();
		}
		scheduledSercice.shutdown();
	}

}

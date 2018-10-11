package soft.net.sconectclient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import soft.net.conf.ConfigClient;
import soft.net.model.ClientChanel;

/**
 * 自动重连检测线程仓库
 * 
 * @author fanpei
 * @date 2018-09-10 03:44
 *
 */
public class ClientCheckConTdStore {
	private ScheduledExecutorService scheduledSercice;// 周期任务执行器
	private Map<String, ClientChanel> chanels;

	public ScheduledExecutorService getServervice() {
		return scheduledSercice;
	}

	/**
	 * 初始化
	 * 
	 */
	public ClientCheckConTdStore() {
		scheduledSercice = Executors.newScheduledThreadPool(ConfigClient.CHECKTDPOOLNUMBER);
		chanels = new HashMap<>();
	}

	/**
	 * 执行线程
	 * 
	 * @param tdThread
	 */
	public void run(ClientCheckConTd tdThread) {
		addChanel(tdThread.getChanel());
		scheduledSercice.scheduleWithFixedDelay(tdThread, ConfigClient.SENDDATA_TIMEOUT * 1000,
				ConfigClient.CONNET_RETRY_INTERVAL, TimeUnit.MILLISECONDS);
	}

	public void addChanel(ClientChanel channel) {
		if (!chanels.containsKey(channel.getChanelId())) {
			chanels.put(channel.getChanelId(), channel);
		}
	}

	public void removeChannel(ClientChanel chanel) {
		chanels.remove(chanel.getChanelId());
	}

	public ClientChanel getChanel(String chanelId) {
		return chanels.get(chanelId);
	}

	public void shutdownNow() {
		for (ClientChanel c : chanels.values()) {
			c.close();
		}
		scheduledSercice.shutdown();
	}

}

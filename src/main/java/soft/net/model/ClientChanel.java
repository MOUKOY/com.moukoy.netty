package soft.net.model;

import io.netty.channel.Channel;
import soft.ifs.IBytesBuild;
import soft.ifs.ISendData;

/**
 * 客户端长连接链路
 * 
 * @author fanpei
 * @date 2018-09-10 03:50
 *
 */
public class ClientChanel extends AChanelID implements ISendData {
	private NetEventListener listener;
	private boolean runFlag = true; // 运行标志

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public NetEventListener getListener() {
		return listener;
	}

	/**
	 * 更新链路
	 * 
	 * @param source
	 */
	public void updateChanel(Channel ch) {
		this.listener.updateChanel(ch);
	}

	public ClientChanel(NetEventListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean sendData(IBytesBuild data, boolean isWait) throws Exception {
		return listener.getNetSource().sendData(data, isWait);
	}

	@Override
	public boolean sendData(IBytesBuild data) throws Exception {
		return listener.getNetSource().sendData(data);
	}

	public void close() {
		listener.release();
	}
}

package soft.net.model;

import java.util.EventListener;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import soft.net.ifs.IByteBuff;
import soft.net.ifs.IBytesBuild;
import soft.net.ifs.ISendData;
import soft.net.protocol.IDecoder;
import soft.net.protocol.IProtocol;
import soft.net.protocol.MyDecoder;

/**
 * 网络事件监视器
 * 
 * @author fanpei
 *
 */
public abstract class NetEventListener implements EventListener, ISendData {
	protected IDecoder decoder;
	protected CusNetSource channel;// 连接链路

	public NetEventListener(SocketChannel ch) {
		super();
		this.channel = new CusNetSource(ch);
		this.decoder = new MyDecoder(this);
	}

	public CusNetSource getNetSource() {
		return channel;
	}

	public void updateChanel(Channel ch) {
		this.channel.setNettyChanel(ch);
	}

	public void release() {
		if (checkChannel())
			channel.close();
	}

	/**
	 * 网络连接建立
	 */
	public abstract void chanelConect();

	/**
	 * 接收数据处理
	 * 
	 * @param buff
	 */
	public abstract void dataReciveEvent(IByteBuff buff);

	/**
	 * 连接断开
	 */
	public abstract void closeEvent();

	/**
	 * 获取一个协议对象
	 * 
	 * @return
	 */
	public abstract IProtocol getProtocol();

	@Override
	public boolean sendData(byte[] datas) throws Exception {
		if (checkChannel())
			return channel.sendData(datas);
		return false;
	}

	@Override
	public boolean sendData(byte[] netdatas, boolean isWait) throws Exception {
		if (checkChannel())
			return channel.sendData(netdatas, isWait);
		return false;
	}

	@Override
	public boolean sendData(IBytesBuild data) throws Exception {
		if (checkChannel())
			return channel.sendData(data);
		return false;
	}

	@Override
	public boolean sendData(IBytesBuild netdata, boolean isWait) throws Exception {
		if (checkChannel())
			return channel.sendData(netdata, isWait);
		return false;
	}

	private boolean checkChannel() {
		return channel != null;
	}

}

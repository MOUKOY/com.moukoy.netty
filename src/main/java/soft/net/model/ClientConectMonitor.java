package soft.net.model;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClientConectMonitor {
	/** conect lock */
	private final ReentrantLock conLock = new ReentrantLock();
	/** con timeout */
	private final Condition contimeOut = conLock.newCondition();

	/**
	 * 连接等待
	 * 
	 * @throws CusException
	 */
	public boolean connectWait(int timeout) throws InterruptedException {
		boolean notimeout = true;
		final ReentrantLock lock = this.conLock;
		try {
			lock.lockInterruptibly();
			while (true) {
				notimeout = contimeOut.await(timeout, TimeUnit.MILLISECONDS);//// 延时
				break;
			}
		} finally {
			lock.unlock();
		}
		return !notimeout;
	}

	/**
	 * 连接等待完成
	 * 
	 * @throws InterruptedException
	 */
	public void connectNotify() throws InterruptedException {
		final ReentrantLock lock = this.conLock;
		try {
			lock.lockInterruptibly();
			contimeOut.signal();
		} finally {
			lock.unlock();
		}
	}

	/** recived lock */
	private final ReentrantLock recvLock = new ReentrantLock();

	/** recv timeout */
	private final Condition recvTimout = recvLock.newCondition();

	/**
	 * 数据接收等待
	 * 
	 * @throws CusException
	 */
	public boolean reciveWait(int timeout) throws InterruptedException {
		boolean notimeout = false;
		final ReentrantLock lock = this.recvLock;
		try {
			lock.lockInterruptibly();
			while (true) {
				notimeout = recvTimout.await(timeout, TimeUnit.MILLISECONDS);// 延时
				break;
			}
		} finally {
			lock.unlock();
		}
		return !notimeout;
	}

	/**
	 * 数据接收等待完成
	 */
	public void reciveNotify() {
		final ReentrantLock lock = this.recvLock;
		try {
			recvTimout.signal();
		} finally {
			lock.unlock();
		}
	}

}

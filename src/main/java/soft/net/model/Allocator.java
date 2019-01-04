package soft.net.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import soft.net.NetBuffRealse;

@Deprecated
public class Allocator {

	// private static Object lock;
	// private static Allocator alloc;

	static {
		// lock = new Object();
	}

	public static Allocator instance() {
		return new Allocator();
	}

	private final ByteBufAllocator allocator;
	private final BlockingQueue<ByteBuf> toCleanQueue;
	private final Thread thread;

	private boolean isStoped = false;

	public Allocator() {
		allocator = PooledByteBufAllocator.DEFAULT;
		toCleanQueue = new LinkedBlockingQueue<ByteBuf>();
		thread = new Thread(new AllocThread());
		// thread.setDaemon(true);
		thread.setName(String.format("网络发送数据数据分配器-allocator[%d]", thread.getId()));
		thread.start();
	}

	private class AllocThread implements Runnable {

		@Override
		public void run() {
			List<ByteBuf> toClean = null;
			// long lastCleanTime = System.currentTimeMillis();
			while (!isStoped && !toCleanQueue.isEmpty()) {
				try {
					toClean = new ArrayList<ByteBuf>();
					toClean.add(toCleanQueue.take());
					toCleanQueue.drainTo(toClean);

					for (ByteBuf buffer : toClean) {
						NetBuffRealse.realse(buffer);
					}
					// lastCleanTime = System.currentTimeMillis();
				} catch (InterruptedException e) {
					break;
				}

			}
		}
	}

	public ByteBuf alloc(int length) {

		ByteBuf buffer = allocator.directBuffer(length);
		// 确保是本线程释放
		buffer.retain();

		return buffer;
	}

	public void release(ByteBuf buf) {
		toCleanQueue.add(buf);
	}

}

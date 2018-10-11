package soft.net.model;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import soft.common.StringUtil;
import soft.net.ifs.IByteBuff;

public class NetByteBuff implements IByteBuff {

	ByteBuf in;

	public NetByteBuff(ByteBuf in) {
		this.in = in;
	}

	@Override
	public int readableBytes() {
		return in.readableBytes();
	}

	@Override
	public byte readByte() {
		if (readableBytes() <= 0)
			throw new ArrayIndexOutOfBoundsException();
		return in.readByte();
	}

	@Override
	public short readShort() {
		if (readableBytes() < 2)
			throw new ArrayIndexOutOfBoundsException();

		if (ByteOrder.BIG_ENDIAN == ORDER)
			return in.readShort();
		else
			return in.readShortLE();
	}

	@Override
	public int readInt() {
		if (readableBytes() < 4)
			throw new ArrayIndexOutOfBoundsException();
		if (ByteOrder.BIG_ENDIAN == ORDER)
			return in.readInt();
		else
			return in.readIntLE();
	}

	@Override
	public long readLong() {
		if (readableBytes() < 8)
			throw new ArrayIndexOutOfBoundsException();
		if (ByteOrder.BIG_ENDIAN == ORDER)
			return in.readLong();
		else
			return in.readLongLE();
	}

	@Override
	public void readBytes(byte[] tmpBuff) {
		if (readableBytes() <= 0 || tmpBuff.length > readableBytes())
			throw new ArrayIndexOutOfBoundsException();

		in.readBytes(tmpBuff);
	}

	@Override
	public void readBytes(byte[] destBuff, int destPos, int destReadLen) {
		if (readableBytes() <= 0 || destReadLen > readableBytes())
			throw new ArrayIndexOutOfBoundsException();
		in.readBytes(destBuff, destPos, destReadLen);
	}

	@Override
	public void release() {
		if (in.refCnt() > 0)// 大于0才释放
			ReferenceCountUtil.release(in);
	}

	@Override
	public boolean hasData() {
		return readableBytes() > 0;
	}

	@Override
	public String toString() {
		return StringUtil.bytes2HexStr(in.array());
	}

	private int hasPrintNum = 0;

	@Override
	public void printHex() {
		while (hasData()) {
			byte value = readByte();
			System.err.print(StringUtil.byte2HexStr(value));
			System.err.print(" ");
			if (hasPrintNum % 20 == 0) {
				System.err.print("\n");
			}
			hasPrintNum += 1;
		}

	}

	@Override
	public byte[] readAllBytes() {
		if (hasData()) {
			int l = readableBytes();
			byte[] tmpBuff = new byte[l];
			in.readBytes(tmpBuff);
			return tmpBuff;
		}
		return new byte[0];
	}

}

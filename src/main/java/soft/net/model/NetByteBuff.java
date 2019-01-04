package soft.net.model;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import soft.common.StringUtil;
import soft.net.NetBuffRealse;
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
		NetBuffRealse.realse(in);
	}

	@Override
	public boolean hasData() {
		return readableBytes() > 0;
	}

	@Override
	public String printHex() {
		String hexStr = null;
		ByteBuf tmpbuff = null;
		try {
			tmpbuff = in.copy();
			int len = tmpbuff.readableBytes();
			if (len > 0) {
				byte[] printData = new byte[len];
				tmpbuff.readBytes(printData);
				hexStr = StringUtil.bytes2HexStr(printData);
			}
		} finally {
			NetBuffRealse.realse(tmpbuff);
		}

		return hexStr;
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

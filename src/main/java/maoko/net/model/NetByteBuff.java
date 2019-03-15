package maoko.net.model;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import soft.common.StringUtil;
import maoko.net.ifs.IByteBuff;
import maoko.net.protocol.SrcBinaryHex;
import maoko.net.util.NetBuffRealse;

public class NetByteBuff implements IByteBuff {

	ByteBuf in;

	public NetByteBuff(ByteBuf in) {
		this.in = in;
	}

	@Override
	public int readableBytes() {
		int reslut = in.readableBytes();
		return reslut;
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
	public byte[] readAllBytes() {
		if (hasData()) {
			int l = readableBytes();
			byte[] tmpBuff = new byte[l];
			in.readBytes(tmpBuff);
			return tmpBuff;
		}
		return new byte[0];
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
	public byte readByte(SrcBinaryHex srcBH) {
		byte v = readByte();
		if (srcBH != null)
			srcBH.addByte(v);
		return v;
	}

	@Override
	public short readShort(SrcBinaryHex srcBH) {
		short v = readShort();
		if (srcBH != null)
			srcBH.addShort(v);
		return v;
	}

	@Override
	public int readInt(SrcBinaryHex srcBH) {
		int v = readShort();
		if (srcBH != null)
			srcBH.addInt(v);
		return v;
	}

	@Override
	public long readLong(SrcBinaryHex srcBH) {
		long v = readLong();
		if (srcBH != null)
			srcBH.addLong(v);
		return v;
	}

	@Override
	public void readBytes(byte[] tmpBuff, SrcBinaryHex srcBH) {
		readBytes(tmpBuff);
		if (srcBH != null)
			srcBH.addBytes(tmpBuff);
	}

	@Override
	public void readBytes(byte[] destBuff, int destPos, int destReadLen, SrcBinaryHex srcBH) {
		readBytes(destBuff, destPos, destReadLen);
		byte[] v = new byte[destReadLen];
		System.arraycopy(destBuff, destPos, v, 0, destReadLen);
		if (srcBH != null)
			srcBH.addBytes(v);
	}

	@Override
	public byte[] readAllBytes(SrcBinaryHex srcBH) {
		byte[] v = readAllBytes();
		if (srcBH != null)
			srcBH.addBytes(v);
		return v;
	}

}

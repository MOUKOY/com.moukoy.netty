package maoko.net.model;

import maoko.net.ifs.IByteBuff;
import maoko.net.protocol.SrcBinaryHex;

public abstract class BuffPrintHex implements IByteBuff {

	public byte readByte(SrcBinaryHex srcBH) {
		byte v = readByte();
		if (srcBH != null)
			srcBH.addByte(v);
		return v;
	}

	public short readShort(SrcBinaryHex srcBH) {
		short v = readShort();
		if (srcBH != null)
			srcBH.addShort(v);
		return v;
	}

	public int readInt(SrcBinaryHex srcBH) {
		int v = readShort();
		if (srcBH != null)
			srcBH.addInt(v);
		return v;
	}

	public long readLong(SrcBinaryHex srcBH) {
		long v = readLong();
		if (srcBH != null)
			srcBH.addLong(v);
		return v;
	}

	public void readBytes(byte[] tmpBuff, SrcBinaryHex srcBH) {
		readBytes(tmpBuff);
		if (srcBH != null)
			srcBH.addBytes(tmpBuff);
	}

	public void readBytes(byte[] destBuff, int destPos, int destReadLen, SrcBinaryHex srcBH) {
		readBytes(destBuff, destPos, destReadLen);
		byte[] v = new byte[destReadLen];
		System.arraycopy(destBuff, destPos, v, 0, destReadLen);
		if (srcBH != null)
			srcBH.addBytes(v);
	}

	public byte[] readAllBytes(SrcBinaryHex srcBH) {
		byte[] v = readAllBytes();
		if (srcBH != null)
			srcBH.addBytes(v);
		return v;
	}
}

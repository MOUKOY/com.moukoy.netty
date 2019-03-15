package maoko.net.model;

import soft.common.BConvrtUtil;
import soft.common.StringUtil;
import maoko.net.ifs.IByteBuff;
import maoko.net.protocol.SrcBinaryHex;

/**
 * 自定义具有指针移动的数据byte对象
 * 
 * @author fanpei
 *
 */
public class CusByteStream implements IByteBuff {

	byte[] orgDatas;
	int len;
	int readIndex = 0;// 读取数据移动指针

	public CusByteStream(byte[] datas) {
		this.orgDatas = datas;
		this.len = datas.length;
	}

	/**
	 * 剩下可读取数据长度
	 * 
	 * @return
	 */
	@Override
	public int readableBytes() {
		return len - readIndex;
	}

	@Override
	public byte readByte() {
		byte b = orgDatas[readIndex];
		readIndex++;
		return b;
	}

	@Override
	public short readShort() {
		byte[] tmp = new byte[2];
		tmp[0] = readByte();
		tmp[1] = readByte();
		return BConvrtUtil.byteToShort(tmp);
	}

	@Override
	public int readInt() {
		byte[] tmp = new byte[4];
		tmp[0] = readByte();
		tmp[1] = readByte();
		tmp[2] = readByte();
		tmp[3] = readByte();
		return BConvrtUtil.byteToInt(tmp);
	}

	@Override
	public long readLong() {
		byte[] tmp = new byte[8];
		tmp[0] = readByte();
		tmp[1] = readByte();
		tmp[2] = readByte();
		tmp[3] = readByte();
		tmp[4] = readByte();
		tmp[5] = readByte();
		tmp[6] = readByte();
		tmp[7] = readByte();
		return BConvrtUtil.byteToLong(tmp);
	}

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param tmpBuff
	 * @return 返回读取的数据长度
	 */
	@Override
	public void readBytes(byte[] tmpBuff) {
		readBytes(tmpBuff, 0, tmpBuff.length);
	}

	/**
	 * 向指定的缓冲区填充数据
	 * 
	 * @param destBuff
	 * @param destPos     目标填充位置
	 * @param destReadLen 欲读取数据长度
	 * @return 已读取的数据长度
	 */
	@Override
	public void readBytes(byte[] destBuff, int destPos, int destReadLen) {
		if (readableBytes() <= 0 || destReadLen > readableBytes())
			throw new ArrayIndexOutOfBoundsException();

		System.arraycopy(orgDatas, readIndex, destBuff, destPos, destReadLen);
		readIndex = readIndex + destReadLen;
	}

	@Override
	public void release() {
		orgDatas = null;
	}

	@Override
	public boolean hasData() {
		return readableBytes() > 0;
	}

	@Override
	public String printHex() {
		StringBuilder hexSb = new StringBuilder();
		if (orgDatas != null) {
			for (byte bValue : orgDatas) {
				hexSb.append(StringUtil.byte2HexStr(bValue)).append(" ");
			}
		}
		return hexSb.toString();
	}

	@Override
	public byte[] readAllBytes() {
		if (hasData()) {
			int l = readableBytes();
			byte[] tmpBuff = new byte[l];
			System.arraycopy(orgDatas, readIndex, tmpBuff, 0, l);
			return tmpBuff;
		}
		return new byte[0];
	}

	// un implements begin
	@Override
	public byte readByte(SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readShort(SrcBinaryHex srcBH) {
		return 0;
	}

	@Override
	public int readInt(SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readLong(SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void readBytes(byte[] tmpBuff, SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readBytes(byte[] destBuff, int destPos, int destReadLen, SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] readAllBytes(SrcBinaryHex srcBH) {
		// TODO Auto-generated method stub
		return null;
	}
	// un implements end

}

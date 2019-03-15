package maoko.net.protocol;

import soft.common.StringUtil;

/**
 * 原始二进制数据十六进制助手
 * 
 * @author fanpei
 *
 */
public class SrcBinaryHex {
	private static final String SPERATOR = " ";// 分割符
	private StringBuilder srcBulder;

	public SrcBinaryHex() {
		srcBulder = new StringBuilder();
	}

	/**
	 * add src bytes
	 * 
	 * @param srcs will be added src bytes
	 */
	public void addBytes(byte[] srcs) {
		if (srcs == null || srcs.length == 0)
			return;
		for (byte b : srcs) {
			addByte(b);
		}
	}

	public void addByte(byte b) {
		srcBulder.append(StringUtil.getByteLeft(b));
		srcBulder.append(StringUtil.getByteRight(b));
		srcBulder.append(SPERATOR);// 加入空格
	}

	public void addShort(short v) {
		addIntLongShort(v, 2);
		srcBulder.append(SPERATOR);// 加入空格
	}

	public void addInt(int v) {
		addIntLongShort(v, 4);
		srcBulder.append(SPERATOR);// 加入空格
	}

	public void addLong(long v) {
		addIntLongShort(v, 8);
		srcBulder.append(SPERATOR);// 加入空格
	}

	/**
	 * @param value
	 * @param radix 字节位，取值范围2,4,8
	 */
	private void addIntLongShort(Object value, int radix) {
		radix = 2 * radix;
		String formatStr = StringUtil.getMsgStr("% %0{}X", radix);
		String str = String.format(formatStr, value);
		srcBulder.append(StringUtil.reverseStr(str, 2, SPERATOR));
	}

	/**
	 * get hex string result and clear buff
	 * 
	 * @return
	 */
	public String getSrcHexAndClear() {
		String srcHexStr = getSrcHex();
		clear();
		return srcHexStr;
	}

	/**
	 * just get src hex string
	 * 
	 * @return
	 */
	public String getSrcHex() {
		return srcBulder.toString();
	}

	public void clear() {
		srcBulder.setLength(0);
	}

}

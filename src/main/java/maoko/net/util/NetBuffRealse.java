package maoko.net.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import soft.common.StaticClass;

/**
 * netty bytebuff 释放助手
 * 
 * @author fanpei
 *
 */
public class NetBuffRealse extends StaticClass {

	public static void realse(ByteBuf in) {
		if (in != null)// 大于0才释放//&& in.refCnt() > 0
			ReferenceCountUtil.release(in);
	}
}

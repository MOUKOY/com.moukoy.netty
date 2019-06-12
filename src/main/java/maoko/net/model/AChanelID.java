package maoko.net.model;

import maoko.common.UUIDUtil;

/**
 * 链路ID
 * 
 * @author fanpei
 *
 */
public abstract class AChanelID {
	protected String chanelId = null;

	/**
	 * 获取链路id
	 * 
	 * @return
	 */
	public String getChanelId() {
		if (null == chanelId)
			chanelId = UUIDUtil.getUUIDStr();
		return chanelId;
	}

	/**
	 * 重设链路id
	 * 
	 * @param chanelId
	 */
	@Deprecated
	public void resetChanelId(String chanelId) {
		this.chanelId = chanelId;
	}
}

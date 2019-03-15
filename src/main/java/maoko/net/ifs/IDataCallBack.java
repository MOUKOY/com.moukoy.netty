package maoko.net.ifs;

/**
 * 数据回调接口
 * 
 * @author fanpei
 *
 */
public interface IDataCallBack {
	/**
	 * 收到数据
	 * 
	 * @param <T>
	 * 
	 * @param data
	 */
	<T> void dataRecive(T data);
}

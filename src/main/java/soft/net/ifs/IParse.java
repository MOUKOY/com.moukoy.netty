package soft.net.ifs;

/**
 * 解析数据
 * 
 * @author fanpei
 *
 */
public interface IParse {

	/**
	 * 解析数据
	 * 
	 */
	void parse() throws Exception;

	/**
	 * 清楚垃圾数据
	 */
	default void clear() {
	}
}

package soft.ifs;

import java.util.Map;

/**
 * json map组装和对象转换
 * 
 * @author fanpei
 *
 * @date 2017年8月3日下午4:17:12
 */
public interface IJsonGen {

	/**
	 * 组装数据map
	 * 
	 * @return
	 */
	Map<String, String> genMap();

	/**
	 * 从map中生成对象实体
	 * 
	 * @param map
	 */
	void genCusObj(Map<String, Object> map);

}

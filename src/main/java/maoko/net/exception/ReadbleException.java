package maoko.net.exception;

/**
 * 读数据错误-不可读
 * 
 * @author fanpei
 *
 */
public class ReadbleException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReadbleException(String error) {
		super(error);
	}
}

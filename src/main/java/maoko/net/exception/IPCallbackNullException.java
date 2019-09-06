package maoko.net.exception;

/**
 * 初始化ip和listener初始化错误
 */
public class IPCallbackNullException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IPCallbackNullException() {
        super("server ip/listener  is null");
    }

}

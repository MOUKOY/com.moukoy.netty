package soft.net.model;

import java.util.EventObject;

public class EventArgs extends EventObject {

	public EventArgs(Object source, Object Data) {
		super(source);
		this.EventSource = source;
		this.Data = Data;
	}

	/**
	 * @return 事件源
	 */
	public Object getEventSource() {
		return EventSource;
	}

	public Object getData() {
		return Data;
	}

	/**
	 * 自动生成序列化ID
	 */
	private static final long serialVersionUID = 7967136274967139295L;
	private Object EventSource;
	private Object Data;
}

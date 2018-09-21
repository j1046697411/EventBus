package org.jzl.eventbus;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/19
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public class OnErrorEvent {
    public static final int CODE_EVENT_NULL = 1 << 0;
    public static final int CODE_SUBSCRIBER_NULL = 1 << 1;
    public static final int CODE_OTHER = 1 << 2;

    private int code;
    private String message;
    private Throwable mError;
    private Object event;

    public OnErrorEvent(int code, String message, Throwable mError, Object event) {
        this.code = code;
        this.message = message;
        this.mError = mError;
        this.event = event;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getError() {
        return mError;
    }

    public Object getEvent() {
        return event;
    }
}

package org.jzl.eventbus;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public class SubscriberInfo<T>  {
    private Class<T> mType;
    private ISubscriber<T> mSubscriber;
    private IHandler mHandler;

    public Class<T> getType() {
        return mType;
    }

    public SubscriberInfo<T> setType(Class<T> mType) {
        this.mType = mType;
        return this;
    }

    public ISubscriber<T> getSubscriber() {
        return mSubscriber;
    }

    public SubscriberInfo<T> setSubscriber(ISubscriber<T> mSubscriber) {
        this.mSubscriber = mSubscriber;
        return this;
    }

    public IHandler getHandler() {
        return mHandler;
    }

    public SubscriberInfo<T> setHandler(IHandler handler) {
        this.mHandler = handler;
        return this;
    }
}

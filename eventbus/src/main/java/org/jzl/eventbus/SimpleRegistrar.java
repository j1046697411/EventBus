package org.jzl.eventbus;

import org.jzl.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
class SimpleRegistrar implements IRegistrar {

    private Collection<SubscriberInfo<?>> mSubscriberInfo;
    private Callback mCallback;
    private Logger mLogger;

    public SimpleRegistrar(Collection<SubscriberInfo<?>> subscriberInfo, Logger logger, Callback callback) {

        this.mSubscriberInfo = subscriberInfo;
        this.mCallback = callback;
        this.mLogger = logger;
    }

    @Override
    public <T> IRegistrar register(Class<T> type, IHandler handler, ISubscriber<T> subscriber) {
        return register(type, handler, false, subscriber);
    }

    @Override
    public <T> IRegistrar registerSticky(Class<T> type, IHandler handler, ISubscriber<T> subscriber) {
        return register(type, handler, true, subscriber);
    }

    private <T> IRegistrar register(Class<T> type, IHandler handler, boolean isSticky, ISubscriber<T> subscriber) {
        ObjectUtil.requireNonNull(type);
        ObjectUtil.requireNonNull(handler);
        ObjectUtil.requireNonNull(subscriber);

        SubscriberInfo<T> info = this.<T>obtain()
                .setType(type)
                .setSubscriber(subscriber)
                .setHandler(handler);
        mSubscriberInfo.add(info);
        mCallback.onRegister((SubscriberInfo<Object>) info, isSticky);

        mLogger.log(Level.INFO, "type:" + type.getCanonicalName() + "| ThreadMode:" + handler + " | sticky:" + isSticky);

        return this;
    }


    private <T> SubscriberInfo<T> obtain() {
        return new SubscriberInfo<>();
    }

    @Override
    public <T> IRegistrar unregister(ISubscriber<T> subscriber) {
        Iterator<SubscriberInfo<?>> iterator = mSubscriberInfo.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSubscriber() == subscriber) {
                iterator.remove();
                break;
            }
        }
        mLogger.log(Level.WARNING, "subscriber not find -> " + subscriber);
        return this;
    }

    public <T> List<SubscriberInfo<T>> findSubscriberInfoByType(Class<T> type) {
        List<SubscriberInfo<T>> info = new ArrayList<>();
        if (ObjectUtil.isNull(type)) {
            return info;
        }
        for (SubscriberInfo<?> subscriberInfo : mSubscriberInfo) {
            if (subscriberInfo.getType().isAssignableFrom(type)) {
                info.add((SubscriberInfo<T>) subscriberInfo);
            }
        }
        return info;
    }

    @Override
    public void dispose() {
        if (mSubscriberInfo != null) {
            mSubscriberInfo.clear();
        }
        mCallback.onDispose(this);
    }

    public interface Callback {
        void onRegister(SubscriberInfo<Object> subscriberInfo, boolean isSticky);

        void onUnregister(SubscriberInfo<Object> subscriberInfo);

        void onDispose(IRegistrar registrar);
    }

}

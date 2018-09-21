package org.jzl.eventbus;

import org.jzl.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public class EventBus implements IDisposable {

    private static EventBus sBus;
    private static String TAG = "JEventBus";

    private CopyOnWriteArraySet<SimpleRegistrar> mRegistrars;
    private SimpleRegistrar.Callback mCallback;
    private IPoster mPoster;
    private Map<Class<?>, Object> mStickyEvents;
    private Logger mLogger;

    private EventBus() {
        mRegistrars = new CopyOnWriteArraySet<>();
        mStickyEvents = new HashMap<>();
        mPoster = new SimplePoster();
        mCallback = new MyCallback(this);
        mLogger = new Logger.AndroidLogger(TAG);
    }

    public static EventBus get() {
        if (sBus == null) {
            synchronized (EventBus.class) {
                if (sBus == null) {
                    sBus = new EventBus();
                }
            }
        }
        return sBus;
    }

    public IRegistrar register() {
        SimpleRegistrar registrar = new SimpleRegistrar(new CopyOnWriteArrayList<SubscriberInfo<?>>(), mLogger, mCallback);
        mRegistrars.add(registrar);
        mLogger.log(Level.INFO, "register");
        return registrar;
    }

    public <T> void post(T event) {
        if (ObjectUtil.isNull(event)) {
            mLogger.log(Level.WARNING, "event is null");
            return;
        }
        List<SubscriberInfo<T>> list = new ArrayList<>();
        for (SimpleRegistrar registrar : mRegistrars) {
            list.addAll(registrar.findSubscriberInfoByType((Class<T>) event.getClass()));
        }
        mPoster.post(event, list);
    }

    public <T> void postSticky(T event) {
        if (ObjectUtil.isNull(event)) {
            mLogger.log(Level.WARNING, "event is null");
            return;
        }
        addStickyEvent(event);
        post(event);
    }

    private void postStickyEvent(SubscriberInfo<Object> info) {
        Class<?> type = info.getType();

        for (Map.Entry<Class<?>, Object> entry : mStickyEvents.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                mPoster.post(entry.getValue(), Arrays.asList(info));
                return;
            }
        }
    }

    private void addStickyEvent(Object event) {
        mStickyEvents.put(event.getClass(), event);
    }

    @Override
    public void dispose() {
        mRegistrars.clear();
    }

    private static class MyCallback implements SimpleRegistrar.Callback {
        private EventBus mEventBus;

        public MyCallback(EventBus eventBus) {
            this.mEventBus = eventBus;
        }

        @Override
        public void onRegister(SubscriberInfo<Object> subscriberInfo, boolean isSticky) {
            if (isSticky) {
                mEventBus.postStickyEvent(subscriberInfo);
            }
        }

        @Override
        public void onUnregister(SubscriberInfo<Object> subscriberInfo) {
        }

        @Override
        public void onDispose(IRegistrar registrar) {
            mEventBus.mRegistrars.remove(registrar);
        }
    }
}

package org.jzl.eventbus;

import org.jzl.util.ObjectUtil;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : 处理事件
 *     @since  : 1.0
 * </pre>
 */
public interface IHandler {
    <T> void post(Task<T> task);

    abstract class Task<T> implements Runnable {
        private ISubscriber<T> subscriber;
        private T event;

        public Task(T event, ISubscriber<T> subscriber) {
            this.subscriber = subscriber;
            this.event = event;
        }

        protected abstract void execute(T event, ISubscriber<T> subscriber) throws Exception;

        @Override
        public final void run() {
            if (ObjectUtil.isNull(event)) {
                EventBus.get().post(new OnErrorEvent(OnErrorEvent.CODE_EVENT_NULL, "event in task is null", new NullPointerException("event in task is null"), null));
            } else if (ObjectUtil.isNull(subscriber)) {
                EventBus.get().post(new OnErrorEvent(OnErrorEvent.CODE_SUBSCRIBER_NULL, "subscriber in task is null", new NullPointerException("subscriber in task is null"), event));
            } else {
                try {
                    execute(event, subscriber);
                } catch (Exception e) {
                    if (event instanceof OnErrorEvent) {
                        return;
                    }
                    EventBus.get().post(new OnErrorEvent(OnErrorEvent.CODE_OTHER, e.getMessage(), e, event));
                }
            }
        }
    }
}

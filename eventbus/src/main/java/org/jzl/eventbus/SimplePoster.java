package org.jzl.eventbus;

import java.util.Collection;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
class SimplePoster implements IPoster {

    @Override
    public <T> void post(T event, Collection<SubscriberInfo<T>> info) {
        for (SubscriberInfo<T> subscriberInfo : info) {
            subscriberInfo.getHandler().post(new MyTask<T>(event, subscriberInfo.getSubscriber()));
        }
    }

    private static class MyTask<T> extends IHandler.Task<T> {
        public MyTask(T event, ISubscriber<T> subscriber) {
            super(event, subscriber);
        }

        @Override
        protected void execute(T event, ISubscriber<T> subscriber) throws Exception {
            subscriber.onReceive(event);
        }
    }
}

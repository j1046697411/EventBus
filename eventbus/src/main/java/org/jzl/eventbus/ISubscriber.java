package org.jzl.eventbus;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public interface ISubscriber<T> {
    void onReceive(T event);
}

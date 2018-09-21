package org.jzl.eventbus;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public interface IRegistrar extends IDisposable {

    <T> IRegistrar register(Class<T> type, IHandler handler, ISubscriber<T> subscriber);

    <T> IRegistrar registerSticky(Class<T> type, IHandler handler, ISubscriber<T> subscriber);

    <T> IRegistrar unregister(ISubscriber<T> subscriber);
}

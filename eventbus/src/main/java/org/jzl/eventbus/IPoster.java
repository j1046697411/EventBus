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
public interface IPoster {
    <T> void post(T event, Collection<SubscriberInfo<T>> info);
}

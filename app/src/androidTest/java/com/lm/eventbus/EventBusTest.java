package com.lm.eventbus;

import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jzl.eventbus.EventBus;
import org.jzl.eventbus.IRegistrar;
import org.jzl.eventbus.ISubscriber;
import org.jzl.eventbus.OnErrorEvent;
import org.jzl.eventbus.ThreadMode;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/20
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
@RunWith(AndroidJUnit4.class)
public class EventBusTest {

    private IRegistrar registrar;
    EventBus eventBus;
    @Before
    public void before(){
        eventBus = EventBus.get();
        registrar = eventBus.register();
    }

    @Test
    public void testSticky(){
        final TestEvent testEvent = new TestEvent();
        eventBus.postSticky(testEvent);

        registrar.register(OnErrorEvent.class, ThreadMode.MAIN, new ISubscriber<OnErrorEvent>() {
            @Override
            public void onReceive(OnErrorEvent event) {
                Assert.assertTrue(event.getError() instanceof RuntimeException);
            }
        });

        registrar.registerSticky(TestEvent.class, ThreadMode.MAIN, new ISubscriber<TestEvent>() {
            @Override
            public void onReceive(TestEvent event) {
                Assert.assertEquals(event, testEvent);
                Assert.assertTrue(isMain());
                throw new RuntimeException();
            }
        });

        eventBus.post(testEvent);
    }

    @After
    public void after(){
        registrar.dispose();
    }
    class TestEvent{
    }

    private boolean isMain(){
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}

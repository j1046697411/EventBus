package org.jzl.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/18
 *     desc     : 事件处理方式
 *     @since  : 1.0
 * </pre>
 */
public enum ThreadMode implements IHandler {

    MAIN {
        @Override
        public <T> void post(IHandler.Task<T> task) {
            ThreadMode.mainHandler.post(task);
        }
    }, ASYNC {
        @Override
        public <T> void post(IHandler.Task<T> task) {
            ThreadMode.asyncExecutor.execute(task);
        }
    }, BACKGROUND {
        @Override
        public <T> void post(IHandler.Task<T> task) {
            task.run();
        }
    }, MATH_OTHER {
        @Override
        public <T> void post(IHandler.Task<T> task) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                asyncExecutor.execute(task);
            } else {
                task.run();
            }
        }
    };

    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static Executor asyncExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public static void setAsyncExecutor(Executor asyncExecutor) {
        ThreadMode.asyncExecutor = asyncExecutor == null ? ThreadMode.asyncExecutor : asyncExecutor;
    }
}

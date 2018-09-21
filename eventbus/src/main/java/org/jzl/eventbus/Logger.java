package org.jzl.eventbus;

import android.util.Log;

import org.jzl.util.ObjectUtil;

import java.util.logging.Level;

/**
 * <pre>
 *     @author : jzl
 *     time     : 2018/09/19
 *     desc     : xxxx
 *     @since  : 1.0
 * </pre>
 */
public interface Logger {
    void log(Level level, String message);

    void log(Level level, String message, Throwable e);

    class JavaLogger implements Logger {
        private Logger logger;

        public JavaLogger(Logger logger) {
            this.logger = ObjectUtil.requireNonNull(logger);
        }

        @Override
        public void log(Level level, String message) {
            logger.log(level, message);
        }

        @Override
        public void log(Level level, String message, Throwable e) {
            logger.log(level, message, e);
        }
    }

    class SystemOutLogger implements Logger {
        @Override
        public void log(Level level, String message) {
            System.out.println("[" + level.getName() + "] " + message);
        }

        @Override
        public void log(Level level, String message, Throwable e) {
            System.out.println("[" + level.getName() + "] " + message);
            e.printStackTrace(System.out);
        }
    }

    class AndroidLogger implements Logger {

        private String mTag;

        public AndroidLogger(String mTag) {
            this.mTag = mTag;
        }

        @Override
        public void log(Level level, String message, Throwable e) {
            if (level != level.OFF) {
                Log.println(mapLevel(level), mTag, message + "\n" + Log.getStackTraceString(e));
            }
        }

        @Override
        public void log(Level level, String message) {
            if (level != Level.OFF) {
                Log.println(mapLevel(level), mTag, message);
            }
        }

        private int mapLevel(Level level) {
            int value = level.intValue();
            if (value < 800) { // below INFO
                if (value < 500) { // below FINE
                    return Log.VERBOSE;
                } else {
                    return Log.DEBUG;
                }
            } else if (value < 900) { // below WARNING
                return Log.INFO;
            } else if (value < 1000) { // below ERROR
                return Log.WARN;
            } else {
                return Log.ERROR;
            }
        }
    }
}

package com.yuanli.base.log;


import com.yuanli.base.BuildConfig;
public class Logger {

    private static final String TAG = "MyAppLogger";

    // 是否允许在 Release 版本中打印日志
    private static boolean ALLOW_LOG_IN_RELEASE = true;

    public static void setAllowLogInRelease(boolean allowLogInRelease) {
        ALLOW_LOG_IN_RELEASE = allowLogInRelease;
    }

    // 获取当前是否是调试模式
    private static final boolean isDebug = BuildConfig.DEBUG;

    // 日志级别
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    private static void log(Level level, String message, Throwable throwable) {
        if (isDebug || ALLOW_LOG_IN_RELEASE) {
            switch (level) {
                case DEBUG:
                    // 在 release 模式下用 Log.i() 替代 Log.d()
                    if (throwable == null) {
                        if (isDebug) {
                            android.util.Log.d(TAG, message);
                        } else {
                            android.util.Log.i(TAG, message); // 使用 Log.i() 输出
                        }
                    } else {
                        if (isDebug) {
                            android.util.Log.d(TAG, message, throwable);
                        } else {
                            android.util.Log.i(TAG, message, throwable); // 使用 Log.i() 输出
                        }
                    }
                    break;
                case INFO:
                    if (throwable == null) {
                        android.util.Log.i(TAG, message);
                    } else {
                        android.util.Log.i(TAG, message, throwable);
                    }
                    break;
                case WARN:
                    if (throwable == null) {
                        android.util.Log.w(TAG, message);
                    } else {
                        android.util.Log.w(TAG, message, throwable);
                    }
                    break;
                case ERROR:
                    if (throwable == null) {
                        android.util.Log.e(TAG, message);
                    } else {
                        android.util.Log.e(TAG, message, throwable);
                    }
                    break;
            }
        }
    }

    // 调试日志
    public static void d(String message) {
        log(Level.DEBUG, message, null);
    }

    public static void d(String message, Throwable throwable) {
        log(Level.DEBUG, message, throwable);
    }

    // 信息日志
    public static void i(String message) {
        log(Level.INFO, message, null);
    }

    public static void i(String tag, String msg) {
        log(Level.INFO, tag + msg, null);
    }

    // 警告日志
    public static void w(String message) {
        log(Level.WARN, message, null);
    }

    public static void w(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }

    // 错误日志
    public static void e(String message) {
        log(Level.ERROR, message, null);
    }

    public static void e(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }
}

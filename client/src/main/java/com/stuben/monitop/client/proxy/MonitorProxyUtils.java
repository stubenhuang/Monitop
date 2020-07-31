package com.stuben.monitop.client.proxy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

public class MonitorProxyUtils {
    private static ExecutorService EXECUTOR = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    /**
     * 把object转换成int
     * 
     * @return 装换不了返回-1
     */
    public static Integer objectToInt(Object object) {
        if (null == object) {
            return null;
        }

        if (!NumberUtils.isParsable(object.toString())) {
            return null;
        }

        return (int) NumberUtils.toDouble(object.toString());
    }

    /**
     * 使用线程池执行
     * 
     * @param runnable
     */
    public static void exec(Runnable runnable) {
        EXECUTOR.submit(runnable);
    }


}

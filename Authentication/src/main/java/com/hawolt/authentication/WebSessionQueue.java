package com.hawolt.authentication;

import com.hawolt.logger.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 11/01/2023 14:12
 * Author: Twitter @hawolt
 **/

public class WebSessionQueue {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final List<Runnable> RUNNABLE_LIST = new LinkedList<>();
    private static final Object LOCK = new Object();

    static {
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            synchronized (LOCK) {
                if (RUNNABLE_LIST.isEmpty()) return;
                Runnable runnable = RUNNABLE_LIST.remove(0);
                try {
                    runnable.run();
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void add(Runnable runnable) {
        synchronized (LOCK) {
            RUNNABLE_LIST.add(runnable);
        }
    }

    public static void kill() {
        SCHEDULED_EXECUTOR_SERVICE.shutdown();
    }
}

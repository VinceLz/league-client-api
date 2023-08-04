package com.hawolt.generic.value.impl;

import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.SchedulableRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created: 12/01/2023 23:34
 * Author: Twitter @hawolt
 **/

public abstract class PairedValueSupplier<K, V> extends SchedulableRunnable {
    public static boolean debug = true;
    private final Map<K, V> map = new HashMap<>();

    public PairedValueSupplier() {
        this(null);
    }

    public PairedValueSupplier(IExceptionCallback callback) {
        super(callback);
    }

    public void put(K k, V v) {
        map.put(k, v);
    }

    public V getValue(K k) {
        return map.get(k);
    }

    public boolean containsKey(K k) {
        return map.containsKey(k);
    }
}

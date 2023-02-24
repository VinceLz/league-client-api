package com.hawolt.generic.value.impl;

import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.SchedulableRunnable;

/**
 * Created: 12/01/2023 23:34
 * Author: Twitter @hawolt
 **/

public abstract class SingletonValueSupplier<T> extends SchedulableRunnable {
    private T value;

    public SingletonValueSupplier() {
        this(null);
    }

    public SingletonValueSupplier(IExceptionCallback callback) {
        super(callback);
    }

    protected void init() {

    }

    protected void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

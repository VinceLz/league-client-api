package com.hawolt.generic.stage;

import java.util.function.Supplier;

/**
 * Created: 13/01/2023 11:17
 * Author: Twitter @hawolt
 **/

public class StageAwareObject<T> {
    private final IStageCallback<T> callback;
    private final Supplier<T> supplier;
    private final int stages;
    private int counter;

    public StageAwareObject(IStageCallback<T> callback, Supplier<T> supplier, int stages) {
        this.callback = callback;
        this.supplier = supplier;
        this.stages = stages;
    }

    public void complete() {
        callback.onStageReached(supplier.get());
    }

    public void next() {
        if (++counter >= stages) {
            complete();
        }
    }
}

package com.hawolt.generic.stage;

/**
 * Created: 13/01/2023 11:17
 * Author: Twitter @hawolt
 **/

public interface IStageCallback<T> {
    void onStageReached(T object);

    void onStageError(Throwable throwable);
}

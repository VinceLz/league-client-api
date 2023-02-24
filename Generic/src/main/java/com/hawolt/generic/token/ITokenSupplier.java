package com.hawolt.generic.token;

import java.util.Set;

/**
 * Created: 10/01/2023 17:42
 * Author: Twitter @hawolt
 **/

public interface ITokenSupplier<T> {
    String get(T name);

    Set<T> keySet();

    void add(T k, String v);

    boolean has(T t);

    String getSupplierName();
}

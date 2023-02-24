package com.hawolt.rman.io.downloader;

/**
 * Created: 05/01/2023 14:24
 * Author: Twitter @hawolt
 **/

public class BadBundleException extends RuntimeException {
    public BadBundleException(String name) {
        super("Bad Bundle: " + name);
    }
}

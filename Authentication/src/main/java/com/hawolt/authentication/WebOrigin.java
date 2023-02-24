package com.hawolt.authentication;

/**
 * Created: 11/01/2023 14:50
 * Author: Twitter @hawolt
 **/

public enum WebOrigin {
    ENTITLEMENTS, LOL_LOGIN, UNKNOWN;
    private final static WebOrigin[] WEB_ORIGINS = WebOrigin.values();

    public static WebOrigin findByString(String name) {
        for (WebOrigin webOrigin : WEB_ORIGINS) {
            if (webOrigin.toString().equalsIgnoreCase(name)) {
                return webOrigin;
            }
        }
        return WebOrigin.UNKNOWN;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", "-");
    }
}

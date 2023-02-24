package com.hawolt.manifest;

/**
 * Created: 09/01/2023 13:08
 * Author: Twitter @hawolt
 **/

public enum ManifestType {
    KEYSTONE("https://ks-foundation.secure.dyn.riotcdn.net/channels/public/bundles"),
    LOL("https://lol.dyn.riotcdn.net/channels/public/bundles");
    private final String bundleUrl;

    ManifestType(String bundleUrl) {
        this.bundleUrl = bundleUrl;
    }

    public String getBundleUrl() {
        return bundleUrl;
    }
}

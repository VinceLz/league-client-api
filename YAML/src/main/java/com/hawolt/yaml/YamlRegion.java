package com.hawolt.yaml;

import java.util.Map;

/**
 * Created: 10/01/2023 19:06
 * Author: Twitter @hawolt
 **/

public class YamlRegion {
    private final String name, platformId;
    private final YamlServers servers;

    public YamlRegion(String key, Object o) {
        this.name = key;
        this.platformId = (((Map<?, ?>) o).get("rso_platform_id").toString());
        this.servers = new YamlServers(((Map<?, ?>) o).get("servers"));
    }

    public String getName() {
        return name;
    }

    public String getPlatformId() {
        return platformId;
    }

    public YamlServers getServers() {
        return servers;
    }
}

package com.hawolt.yaml;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created: 13/01/2023 00:10
 * Author: Twitter @hawolt
 **/

public class YamlWrapper {

    private final Map<ConfigValue, String> map = new HashMap<>();

    public YamlWrapper(JSONObject o) {
        for (String key : o.keySet()) {
            map.put(ConfigValue.valueOf(key.toUpperCase()), o.getString(key));
        }
    }

    public String get(ConfigValue value) {
        return map.get(value);
    }

    @Override
    public String toString() {
        return "YamlWrapper{" +
                "map=" + map +
                '}';
    }
}

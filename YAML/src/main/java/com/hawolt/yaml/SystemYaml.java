package com.hawolt.yaml;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created: 30/07/2022 17:52
 * Author: Twitter @hawolt
 **/

@SuppressWarnings("all")
public class SystemYaml {

    public String default_region, player_bug_report_url, account_protect_url;
    public Map app, build, patcher, player_support_url, region_data, riotclient;
    public ArrayList<LinkedHashMap> products;

    public static JSONObject generate(String o) throws IOException {
        YamlReader reader = new YamlReader(o);
        SystemYaml yaml = reader.read(SystemYaml.class);
        JSONObject object = new JSONObject();
        for (Object key : yaml.region_data.keySet()) {
            YamlRegion yamlRegion = new YamlRegion((String) key, yaml.region_data.get(key));
            YamlServers yamlServers = yamlRegion.getServers();
            JSONObject region = new JSONObject();
            region.put("config", yamlServers.getConfig());
            region.put("email", yamlServers.getEmail());
            region.put("entitlement", yamlServers.getEntitlement());
            region.put("queue", yamlServers.getQueue());
            region.put("ledge", yamlServers.getLedge());
            region.put("platform", yamlServers.getPlatform());
            region.put("lcds", yamlServers.getLCDS());
            object.put(yamlRegion.getPlatformId(), region);
        }
        return object;
    }
}

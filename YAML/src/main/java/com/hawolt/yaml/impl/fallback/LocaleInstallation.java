package com.hawolt.yaml.impl.fallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 30/07/2022 23:35
 * Author: Twitter @hawolt
 **/

public class LocaleInstallation {

    public static File SYSTEM_YAML;

    static {
        try {
            SYSTEM_YAML = locateYaml();
        } catch (Exception e) {
            System.err.println("Error locating RiotClientServices.exe or system.yaml");
        }
    }

    public static File getRiotClientServices() throws IOException {
        File file = Paths.get(System.getenv("ALLUSERSPROFILE"))
                .resolve(StaticConstants.RIOT_GAMES)
                .resolve(StaticConstants.RIOT_INSTALLS_JSON).toFile();
        if (!file.exists()) return null;
        JSONObject object = new JSONObject(new String(Files.readAllBytes(file.toPath())));
        List<String> list = load(new ArrayList<>(), object);
        return list.stream().map(File::new)
                .filter(File::exists)
                .findAny()
                .orElseThrow(() -> new IOException("Unable to locate required file"));
    }

    public static File locateYaml() throws IOException {
        File riotClientServices = getRiotClientServices();
        if (riotClientServices == null || !riotClientServices.exists()) {
            throw new FileNotFoundException("Unable to locate system.yaml");
        }
        return riotClientServices.toPath()
                .getParent()
                .getParent()
                .resolve(StaticConstants.LEAGUE_OF_LEGENDS)
                .resolve(StaticConstants.SYSTEM_YAML)
                .toFile();
    }

    private static List<String> load(List<String> list, JSONObject object) {
        for (String key : object.keySet()) {
            if (object.get(key) instanceof JSONObject) {
                load(list, object.getJSONObject(key));
            } else {
                list.add(object.getString(key));
            }
        }
        return list;
    }
}

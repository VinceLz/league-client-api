package com.hawolt.yaml.impl.fallback;

import com.hawolt.logger.Logger;
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

    public static File RIOT_CLIENT_SERVICES, SYSTEM_YAML;

    static {
        try {
            RIOT_CLIENT_SERVICES = getRiotClientServices();
            SYSTEM_YAML = locateYaml(RIOT_CLIENT_SERVICES);
        } catch (IOException e) {
            Logger.error(e);
            System.err.println("Unable to locate RiotClientServices.exe or system.yaml, exiting (1).");
            System.exit(1);
        }
    }

    public static File getRiotClientServices() throws IOException {
        File file = Paths.get(System.getenv("ALLUSERSPROFILE"))
                .resolve(StaticConstants.RIOT_GAMES)
                .resolve(StaticConstants.RIOT_INSTALLS_JSON).toFile();
        if (!file.exists()) return getRiotClientServices();
        JSONObject object = new JSONObject(new String(Files.readAllBytes(file.toPath())));
        List<String> list = load(new ArrayList<>(), object);
        return list.stream().map(File::new)
                .filter(File::exists)
                .findAny()
                .orElseThrow(() -> new IOException("Unable to locate required file"));
    }

    public static File locateYaml(File riotClientServices) throws FileNotFoundException {
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

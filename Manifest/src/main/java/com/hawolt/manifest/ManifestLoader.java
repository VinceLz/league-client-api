package com.hawolt.manifest;

import com.hawolt.io.Core;
import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created: 01/01/2023 02:58
 * Author: Twitter @hawolt
 **/

public class ManifestLoader {

    private final ManifestType type;
    private final String uri;

    public ManifestLoader(KeystoneFoundation keystoneFoundation) throws IOException {
        this.type = ManifestType.KEYSTONE;
        JSONObject object = keystoneFoundation.load();
        this.uri = object.getString("keystone.self_update.manifest_url");
    }

    public ManifestLoader(Sieve sieve) throws IOException {
        this.type = ManifestType.LOL;
        JSONObject object = sieve.load();
        JSONArray releases = object.getJSONArray("releases");
        JSONObject release = releases.getJSONObject(0);
        JSONObject download = release.getJSONObject("download");
        this.uri = download.getString("url");
    }

    public ManifestLoader(ManifestType type, String uri) {
        this.type = type;
        this.uri = uri;
    }

    public ManifestLoader(String uri) {
        this.type = ManifestType.LOL;
        this.uri = uri;
    }

    public byte[] getManifest() throws IOException {
        String name = uri.substring(uri.lastIndexOf("/") + 1);
        if (RMANCache.isCached(name)) {
            return RMANCache.load(name);
        } else {
            Logger.debug("[rman] loading manifest: {}", uri);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
            connection.setRequestProperty("User-Agent", "");
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] b = Core.read(inputStream).toByteArray();
                RMANCache.store(name, b);
                return b;
            }
        }
    }

    public ManifestType getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
}

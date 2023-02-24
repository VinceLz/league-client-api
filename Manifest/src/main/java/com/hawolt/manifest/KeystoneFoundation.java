package com.hawolt.manifest;

import com.hawolt.io.Core;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created: 31/12/2022 23:53
 * Author: Twitter @hawolt
 **/

public class KeystoneFoundation {
    private final String uri;

    public KeystoneFoundation(String uri) {
        this.uri = uri;
    }

    public JSONObject load() throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
        connection.setRequestProperty("User-Agent", "");
        try (InputStream inputStream = connection.getInputStream()) {
            return new JSONObject(Core.read(inputStream).toString());
        }
    }
}

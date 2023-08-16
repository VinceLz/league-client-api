package com.hawolt.manifest;

import com.hawolt.io.Core;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created: 07/08/2023 19:18
 * Author: Twitter @hawolt
 **/

public class Sieve {
    private final String uri;

    public Sieve(String uri) {
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

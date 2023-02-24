package com.hawolt.manifest;

import com.hawolt.io.Core;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 09/01/2023 13:41
 * Author: Twitter @hawolt
 **/

public class LeaguePatchline {
    private final String uri;

    public LeaguePatchline(String uri) {
        this.uri = uri;
    }

    public List<LeagueRegionalPatchline> load() throws IOException {
        JSONObject object = fetch();
        JSONObject patchline = object.getJSONObject("keystone.products.league_of_legends.patchlines.live");
        JSONObject platforms = patchline.getJSONObject("platforms");
        JSONObject win = platforms.getJSONObject("win");
        JSONArray configurations = win.getJSONArray("configurations");
        List<LeagueRegionalPatchline> list = new ArrayList<>();
        for (int i = 0; i < configurations.length(); i++) {
            JSONObject configuration = configurations.getJSONObject(i);
            String id = configuration.getString("id");
            String url = configuration.getString("patch_url");
            list.add(new LeagueRegionalPatchline(id, url));
        }
        return list;
    }

    private JSONObject fetch() throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
        connection.setRequestProperty("User-Agent", "");
        try (InputStream inputStream = connection.getInputStream()) {
            return new JSONObject(Core.read(inputStream).toString());
        }
    }


}

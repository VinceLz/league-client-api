package com.hawolt.manifest;

/**
 * Created: 09/01/2023 13:44
 * Author: Twitter @hawolt
 **/

public class LeagueRegionalPatchline {
    private final String id, url;

    public LeagueRegionalPatchline(String id, String url) {
        this.url = url;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}

package com.hawolt.virtual.leagueclient.userinfo.child;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 10/01/2023 22:22
 * Author: Twitter @hawolt
 **/

public class UserInformationBanRestrictionGameData {
    private final List<String> additionalGameIds = new ArrayList<>();
    private final String productName, gameLocation, triggerGameId;

    public UserInformationBanRestrictionGameData(JSONObject o) {
        this.productName = o.getString("productName");
        this.gameLocation = o.getString("gameLocation");
        this.triggerGameId = o.getString("triggerGameId");
        JSONArray array = o.getJSONArray("additionalGameIds");
        for (int i = 0; i < array.length(); i++) {
            additionalGameIds.add(array.getString(i));
        }
    }

    public List<String> getAdditionalGameIds() {
        return additionalGameIds;
    }

    public String getProductName() {
        return productName;
    }

    public String getGameLocation() {
        return gameLocation;
    }

    public String getTriggerGameId() {
        return triggerGameId;
    }

    @Override
    public String toString() {
        return "UserInformationBanRestrictionGameData{" +
                "additionalGameIds=" + additionalGameIds +
                ", productName='" + productName + '\'' +
                ", gameLocation='" + gameLocation + '\'' +
                ", triggerGameId='" + triggerGameId + '\'' +
                '}';
    }
}

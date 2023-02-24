package com.hawolt.virtual.riotclient;

import com.hawolt.authentication.CookieType;
import com.hawolt.authentication.ICookieSupplier;
import com.hawolt.generic.data.Platform;
import com.hawolt.generic.data.QueryTokenParser;
import com.hawolt.generic.token.impl.StringTokenSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.virtual.leagueclient.VirtualLeagueClientInstance;
import com.hawolt.virtual.leagueclient.authentication.Userinfo;
import com.hawolt.virtual.leagueclient.exception.LeagueException;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;
import com.hawolt.virtual.riotclient.userinfo.RiotClientUser;
import com.hawolt.yaml.IYamlSupplier;
import com.hawolt.yaml.impl.YamlSupplier;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created: 26/11/2022 13:39
 * Author: Twitter @hawolt
 **/

public class VirtualRiotClient {
    private final StringTokenSupplier riotClientSupplier;
    private final VirtualRiotClientInstance instance;
    private final RiotClientUser riotClientUser;
    private final String username, password;

    public VirtualRiotClient(VirtualRiotClientInstance instance, String username, String password, StringTokenSupplier riotClientSupplier) {
        this.riotClientUser = new RiotClientUser(riotClientSupplier.get("access_token"));
        this.riotClientSupplier = riotClientSupplier;
        this.username = username;
        this.password = password;
        this.instance = instance;
        Logger.debug("[riot-client] {}", riotClientUser);
    }

    public VirtualRiotClientInstance getInstance() {
        return instance;
    }

    public StringTokenSupplier getRiotClientSupplier() {
        return riotClientSupplier;
    }

    public RiotClientUser getRiotClientUser() {
        return riotClientUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public VirtualLeagueClientInstance createVirtualLeagueClientInstance() throws LeagueException, IOException {
        return createVirtualLeagueClientInstance(false);
    }

    public VirtualLeagueClientInstance createVirtualLeagueClientInstance(boolean selfUpdate) throws LeagueException, IOException {
        if (!riotClientUser.isLeagueAccountAssociated()) {
            throw new LeagueException("Riot User has not created a League of Legends account");
        }
        Platform platform = Platform.valueOf(riotClientUser.getDataRegion());
        IYamlSupplier yamlSupplier = new YamlSupplier(platform);
        ICookieSupplier cookieSupplier = instance.getCookieSupplier();
        String leagueClientCookie = cookieSupplier.getClientCookie(instance.getLocalRiotFileVersion(), CookieType.LOL, platform, instance.getGateway());
        StringTokenSupplier leagueClientSupplier = QueryTokenParser.getTokens("lol", instance.get(username, password, leagueClientCookie, instance.getGateway()));
        Userinfo clear = new Userinfo();
        UserInformation userInformation = new UserInformation(new JSONObject(clear.authenticate(instance.getGateway(), instance.getLocalRiotFileVersion(), riotClientSupplier)));
        return new VirtualLeagueClientInstance(this, userInformation, yamlSupplier, leagueClientSupplier, selfUpdate);
    }
}

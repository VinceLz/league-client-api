package com.hawolt.virtual.leagueclient.instance;

import com.hawolt.authentication.ICookieSupplier;
import com.hawolt.authentication.WebOrigin;
import com.hawolt.generic.data.Platform;
import com.hawolt.generic.token.impl.StringTokenSupplier;
import com.hawolt.http.Gateway;
import com.hawolt.virtual.leagueclient.client.VirtualLeagueClient;
import com.hawolt.virtual.leagueclient.exception.LeagueException;
import com.hawolt.virtual.riotclient.client.IVirtualRiotClient;

import java.util.concurrent.CompletableFuture;

/**
 * Created: 13/01/2023 11:46
 * Author: Twitter @hawolt
 **/

public interface IVirtualLeagueClientInstance {
    CompletableFuture<VirtualLeagueClient> login(boolean ignoreSummoner, boolean selfRefresh, boolean complete, boolean minimal) throws LeagueException;

    CompletableFuture<StringTokenSupplier> prepare(Gateway gateway, ICookieSupplier cookieSupplier, WebOrigin webOrigin, Platform platform);

    CompletableFuture<VirtualLeagueClient> login(boolean ignoreSummoner, boolean selfRefresh) throws LeagueException;

    CompletableFuture<VirtualLeagueClient> login() throws LeagueException;

    CompletableFuture<VirtualLeagueClient> chat() throws LeagueException;

    IVirtualRiotClient getVirtualRiotClient();
}

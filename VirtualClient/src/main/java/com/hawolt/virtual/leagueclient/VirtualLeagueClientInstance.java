package com.hawolt.virtual.leagueclient;

import com.hawolt.authentication.ICookieSupplier;
import com.hawolt.authentication.WebOrigin;
import com.hawolt.generic.data.Platform;
import com.hawolt.generic.data.QueryTokenParser;
import com.hawolt.generic.stage.IStageCallback;
import com.hawolt.generic.stage.StageAwareObject;
import com.hawolt.generic.token.impl.StringTokenSupplier;
import com.hawolt.http.Gateway;
import com.hawolt.logger.Logger;
import com.hawolt.version.local.LocalLeagueFileVersion;
import com.hawolt.version.local.LocalRiotFileVersion;
import com.hawolt.virtual.leagueclient.authentication.*;
import com.hawolt.virtual.leagueclient.exception.LeagueException;
import com.hawolt.virtual.leagueclient.refresh.RefreshGroup;
import com.hawolt.virtual.leagueclient.refresh.Refreshable;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;
import com.hawolt.virtual.riotclient.VirtualRiotClient;
import com.hawolt.yaml.ConfigValue;
import com.hawolt.yaml.IYamlSupplier;
import com.hawolt.yaml.YamlWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created: 13/01/2023 11:46
 * Author: Twitter @hawolt
 **/

public class VirtualLeagueClientInstance {
    private final StringTokenSupplier leagueClientSupplier;
    private final VirtualRiotClient virtualRiotClient;
    private final UserInformation userInformation;
    private final IYamlSupplier yamlSupplier;
    private final boolean selfUpdate;

    private LocalLeagueFileVersion localLeagueFileVersion;
    private String platformId;
    private Platform platform;

    public VirtualLeagueClientInstance(VirtualRiotClient virtualRiotClient, UserInformation userInformation, IYamlSupplier yamlSupplier, StringTokenSupplier leagueClientSupplier, boolean selfUpdate) {
        this.leagueClientSupplier = leagueClientSupplier;
        this.virtualRiotClient = virtualRiotClient;
        this.userInformation = userInformation;
        this.yamlSupplier = yamlSupplier;
        this.selfUpdate = selfUpdate;
        Logger.debug("[riot-client] {}", userInformation);
    }

    public CompletableFuture<VirtualLeagueClient> login() throws LeagueException {
        return login(false, true);
    }

    public CompletableFuture<VirtualLeagueClient> login(boolean ignoreSummoner, boolean selfRefresh) throws LeagueException {
        if (!ignoreSummoner && !userInformation.isLeagueAccountAssociated()) {
            throw new LeagueException("League Account has no Summoner attached");
        }
        this.platformId = userInformation.getUserInformationLeague().getCPID();
        this.platform = Platform.valueOf(platformId);
        this.localLeagueFileVersion = new LocalLeagueFileVersion(Arrays.asList("LeagueClientUxRender.exe", "RiotGamesApi.dll"), platform);
        if (selfUpdate) localLeagueFileVersion.schedule(15, 15, TimeUnit.MINUTES);
        CompletableFuture<VirtualLeagueClient> future = new CompletableFuture<>();
        VirtualLeagueClient virtualLeagueClient = new VirtualLeagueClient(this);
        IStageCallback<VirtualLeagueClient> callback = new IStageCallback<VirtualLeagueClient>() {
            @Override
            public void onStageReached(VirtualLeagueClient client) {
                future.complete(client);
            }

            @Override
            public void onStageError(Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };
        StageAwareObject<VirtualLeagueClient> awareness = new StageAwareObject<>(callback, () -> {
            if (selfRefresh) {
                Entitlement entitlement = virtualLeagueClient.getEntitlement();
                LocalRiotFileVersion localRiotFileVersion = virtualRiotClient.getInstance().getLocalRiotFileVersion();
                StringTokenSupplier supplier = virtualLeagueClient.getWebOriginOAuthTokenMap().get(WebOrigin.ENTITLEMENTS);
                RefreshGroup group = new RefreshGroup();
                for (WebOrigin webOrigin : WebOrigin.values()) {
                    if (webOrigin == WebOrigin.UNKNOWN) continue;
                    OAuthToken token = virtualLeagueClient.getWebOriginOAuthTokenMap().get(webOrigin);
                    StringTokenSupplier tokenSupplier = virtualLeagueClient.getWebOriginStringTokenSupplierMap().get(webOrigin);
                    Refreshable refreshable = new Refreshable(virtualLeagueClient, token, localLeagueFileVersion, tokenSupplier);
                    group.add(refreshable);
                }
                Refreshable refreshable = new Refreshable(virtualLeagueClient, entitlement, localRiotFileVersion, supplier);
                try {
                    refreshable.refresh(virtualRiotClient.getInstance());
                } catch (IOException e) {
                    Logger.error(e);
                }
                group.add(refreshable);
                virtualLeagueClient.refresh(group, 55, 55);
            }
            return virtualLeagueClient;
        }, 2);
        LocalRiotFileVersion localRiotFileVersion = virtualRiotClient.getInstance().getLocalRiotFileVersion();
        Gateway gateway = virtualRiotClient.getInstance().getGateway();
        try {
            Userinfo userinfo = new Userinfo();
            userinfo.authenticate(gateway, localRiotFileVersion, leagueClientSupplier);
            virtualLeagueClient.setUserInfo(userinfo);
            Entitlement entitlement = new Entitlement();
            entitlement.authenticate(gateway, localRiotFileVersion, leagueClientSupplier);
            entitlement.authenticate(gateway, localRiotFileVersion, virtualRiotClient.getRiotClientSupplier());
            virtualLeagueClient.setEntitlement(entitlement);
            YamlWrapper wrapper = yamlSupplier.getYamlResources(platform);

            virtualLeagueClient.setYamlWrapper(wrapper);
            LoginQueue loginQueue = new LoginQueue(wrapper.get(ConfigValue.PLATFORM), platform);
            loginQueue.authenticate(gateway, localLeagueFileVersion, StringTokenSupplier.merge("queue", leagueClientSupplier, userinfo, entitlement));
            virtualLeagueClient.setLoginQueue(loginQueue);
            Session session = new Session(userInformation, platform, wrapper.get(ConfigValue.PLATFORM));
            session.authenticate(gateway, localLeagueFileVersion, loginQueue);
            virtualLeagueClient.setSession(session);

            GeoPas geoPas = new GeoPas();
            geoPas.authenticate(gateway, localLeagueFileVersion, leagueClientSupplier);
            virtualLeagueClient.setGeoPas(geoPas);

            virtualLeagueClient.refresh(virtualLeagueClient, session, localLeagueFileVersion, null, 5, 5);

            if (selfRefresh) {
                ICookieSupplier supplier = virtualRiotClient.getInstance().getCookieSupplier();
                Map<WebOrigin, StringTokenSupplier> webOriginStringTokenSupplierMap = new HashMap<>();
                Map<WebOrigin, OAuthToken> webOriginOAuthTokenMap = new HashMap<>();
                for (WebOrigin webOrigin : WebOrigin.values()) {
                    if (webOrigin == WebOrigin.UNKNOWN) continue;
                    prepare(gateway, supplier, webOrigin, platform).whenComplete((tokenSupplier, throwable) -> {
                        if (throwable != null) callback.onStageError(throwable);
                        else {
                            try {
                                OAuthToken token = new OAuthToken(platform);
                                token.authenticate(gateway, localLeagueFileVersion, tokenSupplier);
                                webOriginOAuthTokenMap.put(webOrigin, token);
                                webOriginStringTokenSupplierMap.put(webOrigin, tokenSupplier);
                                virtualLeagueClient.setWebOriginStringTokenSupplierMap(webOriginStringTokenSupplierMap);
                                virtualLeagueClient.setWebOriginOAuthTokenMap(webOriginOAuthTokenMap);
                                awareness.next();
                            } catch (IOException e) {
                                callback.onStageError(e);
                            }
                        }
                    });
                }
            } else {
                awareness.complete();
            }
        } catch (IOException e) {
            callback.onStageError(e);
        }
        return future;
    }

    private CompletableFuture<StringTokenSupplier> prepare(Gateway gateway, ICookieSupplier cookieSupplier, WebOrigin webOrigin, Platform platform) {
        CompletableFuture<StringTokenSupplier> future = new CompletableFuture<>();
        cookieSupplier.getWebCookie(localLeagueFileVersion, webOrigin, platform).whenComplete((cookie, throwable) -> {
            if (throwable != null) future.completeExceptionally(throwable);
            else {
                try {
                    String raw = virtualRiotClient.getInstance().get(virtualRiotClient.getUsername(), virtualRiotClient.getPassword(), cookie, gateway);
                    StringTokenSupplier oauth = QueryTokenParser.getOAuthValues("lol-login", raw);
                    future.complete(oauth);
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
            }
        });
        return future;
    }

    public LocalLeagueFileVersion getLocalLeagueFileVersion() {
        return localLeagueFileVersion;
    }

    public StringTokenSupplier getLeagueClientSupplier() {
        return leagueClientSupplier;
    }

    public VirtualRiotClient getVirtualRiotClient() {
        return virtualRiotClient;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public IYamlSupplier getYamlSupplier() {
        return yamlSupplier;
    }

    public boolean isSelfUpdate() {
        return selfUpdate;
    }

    public String getPlatformId() {
        return platformId;
    }

    public Platform getPlatform() {
        return platform;
    }
}

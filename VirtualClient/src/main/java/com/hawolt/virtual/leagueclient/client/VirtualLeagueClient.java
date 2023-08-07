package com.hawolt.virtual.leagueclient.client;

import com.hawolt.authentication.WebOrigin;
import com.hawolt.generic.token.impl.StringTokenSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.version.IVersionSupplier;
import com.hawolt.virtual.leagueclient.authentication.*;
import com.hawolt.virtual.leagueclient.instance.IVirtualLeagueClientInstance;
import com.hawolt.virtual.leagueclient.refresh.IRefreshStatus;
import com.hawolt.virtual.leagueclient.refresh.RefreshGroup;
import com.hawolt.virtual.leagueclient.refresh.Refreshable;
import com.hawolt.virtual.leagueclient.refresh.ScheduledRefresh;
import com.hawolt.virtual.riotclient.client.IVirtualRiotClient;
import com.hawolt.virtual.riotclient.instance.IVirtualRiotClientInstance;
import com.hawolt.yaml.YamlWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created: 13/01/2023 11:46
 * Author: Twitter @hawolt
 **/

public class VirtualLeagueClient implements IRefreshStatus {
    private final IVirtualLeagueClientInstance virtualLeagueClientInstance;
    private final IVirtualRiotClientInstance virtualRiotClientInstance;
    private final IVirtualRiotClient virtualRiotClient;
    private Map<WebOrigin, StringTokenSupplier> webOriginStringTokenSupplierMap;
    private Map<WebOrigin, OAuthToken> webOriginOAuthTokenMap;
    private YamlWrapper yamlWrapper;
    private Entitlement entitlement;
    private LoginQueue loginQueue;
    private Userinfo userinfo;
    private Session session;
    private GeoPas geopas;
    private Sipt sipt;

    public VirtualLeagueClient(IVirtualLeagueClientInstance virtualLeagueClientInstance) {
        this.virtualRiotClient = virtualLeagueClientInstance.getVirtualRiotClient();
        this.virtualRiotClientInstance = virtualRiotClient.getInstance();
        this.virtualLeagueClientInstance = virtualLeagueClientInstance;
    }

    @Override
    public void onError(Throwable throwable) {
        Logger.error(throwable);
    }

    public ScheduledRefresh<?> refresh(IRefreshStatus status, IAuthentication authentication, IVersionSupplier versionSupplier, StringTokenSupplier tokenSupplier, long initial, long minutes) {
        Refreshable refreshable = new Refreshable(status, authentication, versionSupplier, tokenSupplier);
        return refresh(refreshable, initial, minutes);
    }

    public ScheduledRefresh<?> refresh(Refreshable refreshable, long initial, long minutes) {
        return refresh(new RefreshGroup(refreshable), initial, minutes);
    }

    public ScheduledRefresh<?> refresh(RefreshGroup group, long initial, long minutes) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = service.scheduleAtFixedRate(() -> {
            for (Refreshable refreshable : group) {
                try {
                    refreshable.refresh(virtualRiotClientInstance);
                } catch (IOException e) {
                    onError(e);
                }
            }
        }, initial, minutes, TimeUnit.MINUTES);
        return new ScheduledRefresh<>(service, future);
    }

    public void setWebOriginStringTokenSupplierMap(Map<WebOrigin, StringTokenSupplier> webOriginStringTokenSupplierMap) {
        this.webOriginStringTokenSupplierMap = webOriginStringTokenSupplierMap;
    }

    public void setWebOriginOAuthTokenMap(Map<WebOrigin, OAuthToken> webOriginOAuthTokenMap) {
        this.webOriginOAuthTokenMap = webOriginOAuthTokenMap;
    }

    public void setYamlWrapper(YamlWrapper yamlWrapper) {
        this.yamlWrapper = yamlWrapper;
    }

    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }

    public void setLoginQueue(LoginQueue loginQueue) {
        this.loginQueue = loginQueue;
    }


    public void setUserInfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public void setGeoPas(GeoPas geopas) {
        this.geopas = geopas;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setSipt(Sipt sipt) {
        this.sipt = sipt;
    }

    public IVirtualLeagueClientInstance getVirtualLeagueClientInstance() {
        return virtualLeagueClientInstance;
    }

    public IVirtualRiotClientInstance getVirtualRiotClientInstance() {
        return virtualRiotClientInstance;
    }

    public IVirtualRiotClient getVirtualRiotClient() {
        return virtualRiotClient;
    }

    public Map<WebOrigin, StringTokenSupplier> getWebOriginStringTokenSupplierMap() {
        return webOriginStringTokenSupplierMap;
    }

    public Map<WebOrigin, OAuthToken> getWebOriginOAuthTokenMap() {
        return webOriginOAuthTokenMap;
    }

    public YamlWrapper getYamlWrapper() {
        return yamlWrapper;
    }

    public Entitlement getEntitlement() {
        return entitlement;
    }

    public LoginQueue getLoginQueue() {
        return loginQueue;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public Session getSession() {
        return session;
    }

    public GeoPas getGeoPas() {
        return geopas;
    }

    public Sipt getSipt() {
        return sipt;
    }

}

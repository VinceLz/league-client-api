# league-client-api

currently this api will let you login to an account which is self managed and refreshes any tokens when required for you

## Discord

since this code lacks documentation the best help you can get is my knowledge, proper questions can be asked in this [discord](https://discord.gg/3wknX5gxaW) server, please note that I will not guide you to achieve something or answer beginner level questions

## Maven

to use league-client-api in your maven project include the following repository

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

and the following dependency

```xml
<dependency>
    <groupId>com.github.hawolt</groupId>
    <artifactId>league-client-api</artifactId>
    <version>c58898cfa3</version>
</dependency>
```

## Usage

an example usage that gives you a full Client instace looks as follows

```java
import com.hawolt.authentication.LocalCookieSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.manifest.RMANCache;
import com.hawolt.virtual.leagueclient.VirtualLeagueClient;
import com.hawolt.virtual.leagueclient.VirtualLeagueClientInstance;
import com.hawolt.virtual.leagueclient.exception.LeagueException;
import com.hawolt.virtual.riotclient.VirtualRiotClient;
import com.hawolt.virtual.riotclient.VirtualRiotClientInstance;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Created: 09/02/2023 00:32
 * Author: Twitter @hawolt
 **/

public class Preset {
    public static void main(String[] args) {
        RMANCache.active = true;
        LocalCookieSupplier localCookieSupplier = new LocalCookieSupplier();
        VirtualRiotClientInstance virtualRiotClientInstance = VirtualRiotClientInstance.create(localCookieSupplier);
        try {
            VirtualRiotClient virtualRiotClient = virtualRiotClientInstance.login(args[0], args[1]);
            VirtualLeagueClientInstance virtualLeagueClientInstance = virtualRiotClient.createVirtualLeagueClientInstance();
            CompletableFuture<VirtualLeagueClient> virtualLeagueClientFuture = virtualLeagueClientInstance.login(true, false);
            virtualLeagueClientFuture.whenComplete(((virtualLeagueClient, throwable) -> {
                if (throwable != null) throwable.printStackTrace();
                else {
                    Logger.info("Client setup complete");
                    Logger.info(virtualLeagueClientInstance.getUserInformation());
                }
            }));
        } catch (IOException e) {
            Logger.error(e);
        } catch (LeagueException e) {
            throw new RuntimeException(e);
        }
    }
}
```

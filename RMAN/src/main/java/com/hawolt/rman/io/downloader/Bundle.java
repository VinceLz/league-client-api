package com.hawolt.rman.io.downloader;

import com.hawolt.logger.Logger;
import com.hawolt.manifest.RMANCache;
import com.hawolt.rman.io.StreamReader;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created: 05/01/2023 14:12
 * Author: Twitter @hawolt
 **/

public class Bundle {
    private final String base, name;
    private byte[] b;

    public Bundle(String base, String name) {
        this.base = base;
        this.name = name;
    }

    public String getBase() {
        return base;
    }

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        if (b == null || b.length == 0) throw new BadBundleException(name);
        return b;
    }

    public void download() {
        download(null);
    }

    public void download(IBundleCallback callback) {
        try {
            if (RMANCache.isCached(name)) {
                this.b = RMANCache.load(name);
            } else {
                String uri = String.join("/", base, name);
                Logger.debug("[rman] downloading bundle: {}", name);
                HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
                try (InputStream stream = connection.getInputStream()) {
                    this.b = StreamReader.from(stream);
                }
                RMANCache.store(name, this.b);
            }
        } catch (IOException e) {
            callback.onError(e);
        }
    }
}

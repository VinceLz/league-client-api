package com.hawolt.rman.io.downloader;

import com.hawolt.logger.Logger;
import com.hawolt.manifest.RMANCache;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created: 05/01/2023 14:12
 * Author: Twitter @hawolt
 **/

public class Bundle {
    private final String base, name;

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

    public byte[] getBytes() throws IOException {
        return RMANCache.load(name);
    }

    public void download() {
        download(null);
    }

    public void download(IBundleCallback callback) {
        try {
            if (RMANCache.isCached(name)) return;
            String uri = String.join("/", base, name);
            Logger.debug("[rman] downloading bundle: {}", uri);
            Path tmp = Files.createTempFile("download-", ".tmp");
            HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
            try (InputStream stream = connection.getInputStream(); OutputStream outStream = Files.newOutputStream(tmp)) {
                byte[] chunk = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(chunk)) != -1) {
                    outStream.write(chunk, 0, bytesRead);
                }
            } finally {
                connection.disconnect();
            }
            RMANCache.store(name, tmp);
        } catch (IOException e) {
            callback.onError(e);
        }
    }
}

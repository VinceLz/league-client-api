package com.hawolt.http;

import com.hawolt.logger.Logger;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Pipe;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created: 26/11/2022 13:55
 * Author: Twitter @hawolt
 **/

public class OkHttp3Client {
    public static boolean debug = true;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private static OkHttpClient get(Gateway gateway) {
        if (gateway == null) return okHttpClient;
        return get(gateway.getProxy(), gateway.getAuthentication());
    }

    private static OkHttpClient get(Proxy proxy, Authentication authentication) {
        if (proxy == null) return okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .proxy(proxy);
        return authentication == null ? builder.build() : builder.proxyAuthenticator(authentication).build();
    }

    public static Call perform(Request request) {
        return perform(request, null);
    }

    public static Call perform(Request request, Gateway gateway) {
        if (debug) Logger.debug("[http-out] {}", translate(request));
        return get(gateway).newCall(request);
    }

    private static String translate(Request request) {
        StringBuilder builder = new StringBuilder()
                .append(System.lineSeparator())
                .append(request.method())
                .append(" ")
                .append(request.url());
        Headers headers = request.headers();
        for (String name : headers.names()) {
            builder.append(System.lineSeparator()).append(name).append(": ").append(headers.get(name));
        }
        RequestBody body = request.body();
        if (body != null) {
            Pipe pipe = new Pipe(8192);
            BufferedSink sink = Okio.buffer(pipe.sink());
            try {
                body.writeTo(sink);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String content = Diffuser.vaporize(new String(sink.getBuffer().readByteArray()));
            builder.append(System.lineSeparator()).append(content);
        }
        return builder.toString();
    }

}

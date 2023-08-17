package com.hawolt.authentication;

import com.hawolt.generic.Constant;
import com.hawolt.generic.data.Platform;
import com.hawolt.http.ApacheHttp2Client;
import com.hawolt.http.Gateway;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.version.IVersionSupplier;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created: 09/01/2023 21:17
 * Author: Twitter @hawolt
 **/

public class LocalCookieSupplier implements ICookieSupplier {

    public static String build(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();
        for (String string : list) {
            String[] data = string.split(";");
            builder.append(data[0]).append("; ");
        }
        return builder.toString().trim();
    }

    @Override
    public String getClientCookie(IVersionSupplier versionSupplier, CookieType type, Platform platform, Gateway gateway) throws IOException {
        return createSessionCookie(versionSupplier, type, platform, gateway);
    }

    @Override
    public String getClientCookie(CookieType type, Platform platform, Gateway gateway) throws IOException {
        throw new IOException("This call is unable to execute without a buildVersion, please use LocalCookieSupplier#getClientCookie(String, CookieType, Region, Gateway)");

    }

    @Override
    public String getClientCookie(IVersionSupplier versionSupplier, CookieType type, Platform platform) throws IOException {
        return createSessionCookie(versionSupplier, type, platform);
    }

    @Override
    public String getClientCookie(CookieType type, Platform platform) throws IOException {
        throw new IOException("This call is unable to execute without a buildVersion, please use LocalCookieSupplier#getClientCookie(String, CookieType, Region, Gateway)");
    }

    public static String createSessionCookie(IVersionSupplier versionSupplier, CookieType type, Platform platform) throws IOException {
        return createSessionCookie(versionSupplier, type, platform, null);
    }

    public static String createSessionCookie(IVersionSupplier versionSupplier, CookieType type, Platform platform, Gateway gateway) throws IOException {
        return createSessionCookie(versionSupplier, createSessionCookie(versionSupplier, null, type, platform, gateway), type, platform, gateway);
    }

    private static String createSessionCookie(IVersionSupplier versionSupplier, String __cf_bm, CookieType type, Platform platform, Gateway gateway) throws IOException {
        String nonce = generateNonce();
        String body = payload(type, nonce);
        RequestBody post = RequestBody.create(body, Constant.APPLICATION_JSON);
        String minor = versionSupplier.getVersionValue("RiotGamesApi.dll");
        Request.Builder builder = new Request.Builder()
                .url("https://auth.riotgames.com/api/v1/authorization")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent",
                        String.format(
                                "RiotClient/%s%s rso-auth (Windows;10;;Professional, x64)",
                                versionSupplier.getVersionValue("RiotClientFoundation.dll"),
                                minor.substring(minor.lastIndexOf('.'))
                        )
                )
                .addHeader("Pragma", "no-cache")
                .post(post);
        if (__cf_bm != null) builder.addHeader("Cookie", __cf_bm);
        Request request = builder.build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            if (response.code() == 429) throw new IOException("RATE_LIMITED");
            if (__cf_bm == null) return build(response.headers("set-cookie"));
            if (response.code() == 200) {
                return build(response.headers("set-cookie"));
            }
        }
        throw new IOException("UNABLE_TO_CREATE_SESSION");
    }

    private static String payload(CookieType type, String nonce) {
        JSONObject object = new JSONObject();
        object.put("claims", "");
        object.put("nonce", nonce);
        object.put("acr_values", "");
        object.put("code_challenge", "");
        object.put("client_id", type == CookieType.RIOT_CLIENT ?
                "riot-client" :
                "lol"
        );
        object.put("code_challenge_method", "");
        object.put("response_type", "token id_token");
        object.put("redirect_uri", "http://localhost/redirect");
        object.put("scope", type == CookieType.RIOT_CLIENT ?
                "openid link ban lol_region lol summoner offline_access" :
                "lol_region account openid ban lol summoner offline_access"
        );
        return object.toString();
    }

    private static String generateNonce() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))).substring(0, 22);
    }

    @Override
    public CompletableFuture<String> getWebCookie(WebOrigin origin, Platform platform) {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new IOException("This call is unable to execute without a buildVersion, please use LocalCookieSupplier#getWebCookie(String, WebOrigin, Region)"));
        return future;
    }

    @Override
    public CompletableFuture<String> getWebCookie(IVersionSupplier versionSupplier, WebOrigin origin, Platform platform) {
        CompletableFuture<String> future = new CompletableFuture<>();
        WebSessionQueue.add(() -> {
            try {
                HttpHost target = new HttpHost("https", "auth.riotgames.com", 443);
                String agent = String.format("LeagueOfLegendsClient/%s (rcp-be-%s)", versionSupplier.getVersionValue(platform, "LeagueClientUxRender.exe"), origin.toString());
                String scopes = "account lol_region openid offline_access lol ban profile email phone birthdate summoner";
                String query = String.format(
                        "response_type=code&client_id=lol&redirect_uri=http://localhost/redirect&scope=%s",
                        URLEncoder.encode(scopes, StandardCharsets.UTF_8.name())
                );
                AsyncRequestBuilder builder = ApacheHttp2Client.build(target, "/authorize", query).addHeader("User-Agent", agent);
                Message<HttpResponse, String> message = ApacheHttp2Client.request(target, builder);
                List<String> list = Arrays.stream(message.getHead().getHeaders())
                        .filter(header -> header.getName().equalsIgnoreCase("set-cookie"))
                        .map(NameValuePair::getValue)
                        .collect(Collectors.toList());
                future.complete(build(list));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}

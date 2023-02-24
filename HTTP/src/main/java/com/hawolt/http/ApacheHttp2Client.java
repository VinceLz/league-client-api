package com.hawolt.http;

import com.hawolt.logger.Logger;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ApacheHttp2Client implements FutureCallback<Message<HttpResponse, String>> {
    private static final FutureCallback<Message<HttpResponse, String>> callback = new ApacheHttp2Client();

    @Override
    public void completed(Message<HttpResponse, String> message) {
    }

    @Override
    public void failed(Exception e) {
        Logger.error(e);
    }

    @Override
    public void cancelled() {

    }

    private static final BasicResponseConsumer<String> consumer = new BasicResponseConsumer<>(new StringAsyncEntityConsumer());
    private static final HttpAsyncRequester requester;

    public static int timeoutInSeconds = 5;

    static {
        IOReactorConfig config = IOReactorConfig.copy(IOReactorConfig.DEFAULT)
                .setSocksProxyAddress(null)
                .setSocksProxyUsername(null)
                .setSocksProxyUsername(null).build();
        requester = H2RequesterBootstrap.bootstrap().setIOReactorConfig(config).create();
        requester.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.debug("Shutting down {}", "$CLASS");
            requester.initiateShutdown();
            requester.close(CloseMode.GRACEFUL);
        }));
    }

    public static AsyncRequestBuilder build(HttpHost host, String path, String query) throws IOException {
        if (!path.startsWith("/")) throw new IOException("path must start with a leading forward slash");
        final String uri = query == null ? path : String.join("?", path, query);
        return AsyncRequestBuilder.get()
                .setHttpHost(host)
                .setPath(uri);
    }

    public static Message<HttpResponse, String> request(HttpHost host, AsyncRequestBuilder builder) throws ExecutionException, InterruptedException, IOException {
        final Future<AsyncClientEndpoint> future = requester.connect(host, Timeout.ofSeconds(timeoutInSeconds));
        final AsyncClientEndpoint clientEndpoint = future.get();
        Message<HttpResponse, String> message = clientEndpoint.execute(builder.build(), consumer, callback).get();
        clientEndpoint.releaseAndReuse();
        return message;
    }
}

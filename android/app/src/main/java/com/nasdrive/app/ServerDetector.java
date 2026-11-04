package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* compiled from: ServerDetector.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0006\u0010\n\u001a\u00020\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/nasdrive/app/ServerDetector;", "", "<init>", "()V", "client", "Lokhttp3/OkHttpClient;", "testUrl", "Lcom/nasdrive/app/DetectionResult;", "baseUrl", "", "findBestServer", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class ServerDetector {
    public static final ServerDetector INSTANCE = new ServerDetector();
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1500, TimeUnit.MILLISECONDS).readTimeout(1500, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false).build();

    private ServerDetector() {
    }

    public final DetectionResult testUrl(String baseUrl) {
        DetectionResult detectionResult;
        Intrinsics.checkNotNullParameter(baseUrl, "baseUrl");
        String testUrl = StringsKt.trimEnd(baseUrl, '/') + "/api/storage";
        long start = System.currentTimeMillis();
        try {
            Request request = new Request.Builder().url(testUrl).get().build();
            Response execute = client.newCall(request).execute();
            try {
                long currentTimeMillis = System.currentTimeMillis() - start;
                if (execute.isSuccessful()) {
                    detectionResult = new DetectionResult(StringsKt.trimEnd(baseUrl, '/'), ServerConfig.INSTANCE.isLanUrl(baseUrl), currentTimeMillis, true);
                } else {
                    detectionResult = new DetectionResult(baseUrl, ServerConfig.INSTANCE.isLanUrl(baseUrl), currentTimeMillis, false);
                }
                CloseableKt.closeFinally(execute, null);
                return detectionResult;
            } finally {
            }
        } catch (Exception e) {
            return new DetectionResult(baseUrl, ServerConfig.INSTANCE.isLanUrl(baseUrl), -1L, false);
        }
    }

    public final DetectionResult findBestServer() {
        List<String> candidates = CollectionsKt.listOf((Object[]) new String[]{ServerConfig.URL_LAN_88, ServerConfig.URL_LAN_DOMAIN, ServerConfig.URL_LAN_1, ServerConfig.URL_WAN_CLOUDFLARE});
        for (String candidate : candidates) {
            DetectionResult res = testUrl(candidate);
            if (res.getSuccess()) {
                return res;
            }
        }
        return new DetectionResult(ServerConfig.URL_WAN_CLOUDFLARE, false, -1L, false);
    }
}

package com.nasdrive.app;

import android.content.Context;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.google.android.material.internal.ViewUtils;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: NasApiClient.kt */
@Metadata(d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\t\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0016\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\tJ\u0016\u0010\u0011\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\tJ\u0016\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\tJ\u0016\u0010\u0013\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\tJ\u0016\u0010\u0015\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\tJ+\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t¢\u0006\u0004\b\u001a\u0010\u001bJ@\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\t2\b\b\u0002\u0010\u001f\u001a\u00020\t2\b\b\u0002\u0010 \u001a\u00020\t2\b\b\u0002\u0010!\u001a\u00020\t2\b\b\u0002\u0010\"\u001a\u00020\tJ!\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$0\u00172\u0006\u0010&\u001a\u00020'¢\u0006\u0004\b(\u0010)JS\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$0\u00172\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\t2\b\b\u0002\u0010\u001f\u001a\u00020\t2\b\b\u0002\u0010 \u001a\u00020\t2\b\b\u0002\u0010!\u001a\u00020\t2\b\b\u0002\u0010\"\u001a\u00020\t¢\u0006\u0004\b+\u0010,J\u001b\u0010-\u001a\b\u0012\u0004\u0012\u00020.0\u00172\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0004\b/\u00100J5\u00101\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u00103\u001a\u00020\t2\b\b\u0002\u00104\u001a\u000202¢\u0006\u0004\b5\u00106J#\u00107\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\t¢\u0006\u0004\b8\u00109J/\u0010:\u001a\b\u0012\u0004\u0012\u00020;0\u00172\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\t2\b\b\u0002\u00104\u001a\u000202¢\u0006\u0004\b<\u0010=J;\u0010>\u001a\b\u0012\u0004\u0012\u00020?0\u00172\u0006\u0010\n\u001a\u00020\u000b2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\t0$2\u0006\u0010A\u001a\u00020\t2\b\b\u0002\u00104\u001a\u000202¢\u0006\u0004\bB\u0010CJ\u001b\u0010D\u001a\b\u0012\u0004\u0012\u00020E0\u00172\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0004\bF\u00100J#\u0010G\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010H\u001a\u00020\t¢\u0006\u0004\bI\u00109J#\u0010J\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010H\u001a\u00020\t¢\u0006\u0004\bK\u00109J\u001b\u0010L\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0004\bM\u00100J)\u0010N\u001a\b\u0012\u0004\u0012\u00020O0\u00172\u0006\u0010\n\u001a\u00020\u000b2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\t0$¢\u0006\u0004\bP\u0010QJ+\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$0\u00172\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u001e\u001a\u00020\t¢\u0006\u0004\bS\u00109J3\u0010T\u001a\b\u0012\u0004\u0012\u00020O0\u00172\u0006\u0010\n\u001a\u00020\u000b2\f\u0010U\u001a\b\u0012\u0004\u0012\u00020\t0$2\b\b\u0002\u0010A\u001a\u00020\t¢\u0006\u0004\bV\u0010WJ#\u0010X\u001a\b\u0012\u0004\u0012\u0002020\u00172\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\t¢\u0006\u0004\bY\u00109J\u000e\u0010Z\u001a\u00020\t2\u0006\u0010[\u001a\u00020\\R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006]"}, d2 = {"Lcom/nasdrive/app/NasApiClient;", "", "<init>", "()V", "client", "Lokhttp3/OkHttpClient;", "getClient", "()Lokhttp3/OkHttpClient;", "getBaseUrl", "", "context", "Landroid/content/Context;", "applyAuth", "Lokhttp3/Request$Builder;", "builder", "getStreamUrl", "path", "getThumbnailUrl", "getDownloadUrl", "getVaultStreamUrl", TtmlNode.ATTR_ID, "getVaultDownloadUrl", "login", "Lkotlin/Result;", "username", "pass", "login-0E7RQCE", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "newFilesCall", "Lokhttp3/Call;", "folder", "search", "type", "sort", "order", "parseFilesResponse", "", "Lcom/nasdrive/app/NasItem;", "response", "Lokhttp3/Response;", "parseFilesResponse-IoAF18A", "(Lokhttp3/Response;)Ljava/lang/Object;", "fetchFiles", "fetchFiles-hUnOzRk", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", "fetchStorage", "Lcom/nasdrive/app/StorageInfo;", "fetchStorage-IoAF18A", "(Landroid/content/Context;)Ljava/lang/Object;", "createFolder", "", "name", "isVault", "createFolder-BWLJW6A", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/Object;", "deleteItem", "deleteItem-gIAlu-s", "(Landroid/content/Context;Ljava/lang/String;)Ljava/lang/Object;", "fetchFolders", "Lcom/nasdrive/app/FolderListResult;", "fetchFolders-0E7RQCE", "(Landroid/content/Context;Ljava/lang/String;Z)Ljava/lang/Object;", "moveItems", "Lcom/nasdrive/app/MoveResult;", "paths", "targetFolder", "moveItems-BWLJW6A", "(Landroid/content/Context;Ljava/util/List;Ljava/lang/String;Z)Ljava/lang/Object;", "fetchVaultStatus", "Lcom/nasdrive/app/VaultStatus;", "fetchVaultStatus-IoAF18A", "setupVault", "password", "setupVault-gIAlu-s", "unlockVault", "unlockVault-gIAlu-s", "lockVault", "lockVault-IoAF18A", "importToVault", "", "importToVault-gIAlu-s", "(Landroid/content/Context;Ljava/util/List;)Ljava/lang/Object;", "fetchVaultFiles", "fetchVaultFiles-gIAlu-s", "exportFromVault", "ids", "exportFromVault-0E7RQCE", "(Landroid/content/Context;Ljava/util/List;Ljava/lang/String;)Ljava/lang/Object;", "deleteVaultItem", "deleteVaultItem-gIAlu-s", "formatBytes", "bytes", "", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class NasApiClient {
    public static final NasApiClient INSTANCE = new NasApiClient();
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(8, TimeUnit.SECONDS).readTimeout(25, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

    private NasApiClient() {
    }

    public final OkHttpClient getClient() {
        return client;
    }

    public final String getBaseUrl(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return StringsKt.trimEnd(ServerConfig.INSTANCE.getActiveUrl(context), '/');
    }

    private final Request.Builder applyAuth(Context context, Request.Builder builder) {
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        if (token.length() > 0) {
            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return builder;
    }

    public final String getStreamUrl(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        String tokenParam = token.length() > 0 ? "&token=" + Uri.encode(token) : "";
        return getBaseUrl(context) + "/api/stream?path=" + Uri.encode(path) + tokenParam;
    }

    public final String getThumbnailUrl(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        String tokenParam = token.length() > 0 ? "&token=" + Uri.encode(token) : "";
        return getBaseUrl(context) + "/api/thumbnail?path=" + Uri.encode(path) + tokenParam;
    }

    public final String getDownloadUrl(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        String tokenParam = token.length() > 0 ? "&token=" + Uri.encode(token) : "";
        return getBaseUrl(context) + "/api/download?path=" + Uri.encode(path) + tokenParam;
    }

    public final String getVaultStreamUrl(Context context, String id) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(id, "id");
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        String tokenParam = token.length() > 0 ? "&token=" + Uri.encode(token) : "";
        return getBaseUrl(context) + "/api/vault/stream?id=" + Uri.encode(id) + tokenParam;
    }

    public final String getVaultDownloadUrl(Context context, String id) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(id, "id");
        String token = ServerConfig.INSTANCE.getAuthToken(context);
        String tokenParam = token.length() > 0 ? "&token=" + Uri.encode(token) : "";
        return getBaseUrl(context) + "/api/vault/download?id=" + Uri.encode(id) + tokenParam;
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x0086, code lost:
    
        if (r0 == null) goto L12;
     */
    /* renamed from: login-0E7RQCE, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object m373login0E7RQCE(android.content.Context r19, java.lang.String r20, java.lang.String r21) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.NasApiClient.m373login0E7RQCE(android.content.Context, java.lang.String, java.lang.String):java.lang.Object");
    }

    public static /* synthetic */ Call newFilesCall$default(NasApiClient nasApiClient, Context context, String str, String str2, String str3, String str4, String str5, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        if ((i & 4) != 0) {
            str2 = "";
        }
        if ((i & 8) != 0) {
            str3 = TtmlNode.COMBINE_ALL;
        }
        if ((i & 16) != 0) {
            str4 = "name";
        }
        if ((i & 32) != 0) {
            str5 = "asc";
        }
        return nasApiClient.newFilesCall(context, str, str2, str3, str4, str5);
    }

    public final Call newFilesCall(Context context, String folder, String search, String type, String sort, String order) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(folder, "folder");
        Intrinsics.checkNotNullParameter(search, "search");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(sort, "sort");
        Intrinsics.checkNotNullParameter(order, "order");
        String base = getBaseUrl(context);
        String url = base + "/api/files?folder=" + Uri.encode(folder) + "&search=" + Uri.encode(search) + "&type=" + Uri.encode(type) + "&sort=" + Uri.encode(sort) + "&order=" + Uri.encode(order);
        Request.Builder builder = new Request.Builder().url(url).get();
        return client.newCall(applyAuth(context, builder).build());
    }

    /* renamed from: parseFilesResponse-IoAF18A, reason: not valid java name */
    public final Object m375parseFilesResponseIoAF18A(Response response) {
        String bodyStr;
        String str = "--";
        Intrinsics.checkNotNullParameter(response, "response");
        try {
            if (!response.isSuccessful()) {
                if (response.code() == 401) {
                    Result.Companion companion = Result.INSTANCE;
                    return Result.m382constructorimpl(ResultKt.createFailure(new IOException("Chưa đăng nhập (401)")));
                }
                Result.Companion companion2 = Result.INSTANCE;
                return Result.m382constructorimpl(ResultKt.createFailure(new IOException("Mã lỗi máy chủ: " + response.code())));
            }
            ResponseBody body = response.body();
            if (body != null && (bodyStr = body.string()) != null) {
                JSONObject json = new JSONObject(bodyStr);
                boolean z = false;
                if (!json.optBoolean("ok", false)) {
                    Result.Companion companion3 = Result.INSTANCE;
                    return Result.m382constructorimpl(ResultKt.createFailure(new IOException(json.optString("error", "Lỗi nạp danh sách tệp"))));
                }
                JSONArray itemsArray = json.optJSONArray("items");
                if (itemsArray == null) {
                    Result.Companion companion4 = Result.INSTANCE;
                    return Result.m382constructorimpl(CollectionsKt.emptyList());
                }
                ArrayList list = new ArrayList(itemsArray.length());
                int i = 0;
                int length = itemsArray.length();
                while (i < length) {
                    JSONObject obj = itemsArray.getJSONObject(i);
                    String optString = obj.optString("name", "Unknown");
                    Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                    String optString2 = obj.optString("path", "");
                    Intrinsics.checkNotNullExpressionValue(optString2, "optString(...)");
                    boolean optBoolean = obj.optBoolean("isFolder", z);
                    String bodyStr2 = bodyStr;
                    long optLong = obj.optLong("size", 0L);
                    String optString3 = obj.optString("formattedSize", str);
                    Intrinsics.checkNotNullExpressionValue(optString3, "optString(...)");
                    String optString4 = obj.optString("formattedDate", str);
                    Intrinsics.checkNotNullExpressionValue(optString4, "optString(...)");
                    String str2 = str;
                    String optString5 = obj.optString("type", "other");
                    Intrinsics.checkNotNullExpressionValue(optString5, "optString(...)");
                    String optString6 = obj.optString("ext", "");
                    Intrinsics.checkNotNullExpressionValue(optString6, "optString(...)");
                    list.add(new NasItem(optString, optString2, optBoolean, optLong, optString3, optString4, optString5, optString6, false, null, ViewUtils.EDGE_TO_EDGE_FLAGS, null));
                    i++;
                    bodyStr = bodyStr2;
                    str = str2;
                    z = false;
                }
                Result.Companion companion5 = Result.INSTANCE;
                return Result.m382constructorimpl(list);
            }
            Result.Companion companion6 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(new IOException("Không có dữ liệu phản hồi")));
        } catch (Exception e) {
            Result.Companion companion7 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: fetchFiles-hUnOzRk$default, reason: not valid java name */
    public static /* synthetic */ Object m358fetchFileshUnOzRk$default(NasApiClient nasApiClient, Context context, String str, String str2, String str3, String str4, String str5, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        if ((i & 4) != 0) {
            str2 = "";
        }
        if ((i & 8) != 0) {
            str3 = TtmlNode.COMBINE_ALL;
        }
        if ((i & 16) != 0) {
            str4 = "name";
        }
        if ((i & 32) != 0) {
            str5 = "asc";
        }
        return nasApiClient.m366fetchFileshUnOzRk(context, str, str2, str3, str4, str5);
    }

    /* renamed from: fetchFiles-hUnOzRk, reason: not valid java name */
    public final Object m366fetchFileshUnOzRk(Context context, String folder, String search, String type, String sort, String order) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(folder, "folder");
        Intrinsics.checkNotNullParameter(search, "search");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(sort, "sort");
        Intrinsics.checkNotNullParameter(order, "order");
        Call call = newFilesCall(context, folder, search, type, sort, order);
        try {
            Response execute = call.execute();
            try {
                Object m375parseFilesResponseIoAF18A = INSTANCE.m375parseFilesResponseIoAF18A(execute);
                CloseableKt.closeFinally(execute, null);
                return m375parseFilesResponseIoAF18A;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: fetchStorage-IoAF18A, reason: not valid java name */
    public final Object m368fetchStorageIoAF18A(Context context) {
        String string;
        Intrinsics.checkNotNullParameter(context, "context");
        String base = getBaseUrl(context);
        String url = base + "/api/storage";
        Request request = applyAuth(context, new Request.Builder().url(url).get()).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                if (!response.isSuccessful()) {
                    Result.Companion companion = Result.INSTANCE;
                    Object m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Lỗi storage: " + response.code())));
                    CloseableKt.closeFinally(execute, null);
                    return m382constructorimpl;
                }
                ResponseBody body = response.body();
                if (body != null && (string = body.string()) != null) {
                    JSONObject jSONObject = new JSONObject(string);
                    Result.Companion companion2 = Result.INSTANCE;
                    String optString = jSONObject.optString("totalGb", "0");
                    Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                    String optString2 = jSONObject.optString("usedGb", "0");
                    Intrinsics.checkNotNullExpressionValue(optString2, "optString(...)");
                    String optString3 = jSONObject.optString("freeGb", "0");
                    Intrinsics.checkNotNullExpressionValue(optString3, "optString(...)");
                    Object m382constructorimpl2 = Result.m382constructorimpl(new StorageInfo(optString, optString2, optString3, jSONObject.optInt("usedPercent", 0)));
                    CloseableKt.closeFinally(execute, null);
                    return m382constructorimpl2;
                }
                Result.Companion companion3 = Result.INSTANCE;
                Object m382constructorimpl3 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Rỗng")));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl3;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion4 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: createFolder-BWLJW6A$default, reason: not valid java name */
    public static /* synthetic */ Object m356createFolderBWLJW6A$default(NasApiClient nasApiClient, Context context, String str, String str2, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        return nasApiClient.m362createFolderBWLJW6A(context, str, str2, z);
    }

    /* renamed from: createFolder-BWLJW6A, reason: not valid java name */
    public final Object m362createFolderBWLJW6A(Context context, String folder, String name, boolean isVault) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(folder, "folder");
        Intrinsics.checkNotNullParameter(name, "name");
        String base = getBaseUrl(context);
        String url = base + "/api/folder";
        JSONObject json = new JSONObject();
        json.put("folder", folder);
        json.put("name", name);
        json.put("isVault", isVault);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject, "toString(...)");
        RequestBody body = companion.create(jSONObject, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Result.Companion companion2 = Result.INSTANCE;
                Object m382constructorimpl = Result.m382constructorimpl(Boolean.valueOf(execute.isSuccessful()));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion3 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: deleteItem-gIAlu-s, reason: not valid java name */
    public final Object m363deleteItemgIAlus(Context context, String path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        String base = getBaseUrl(context);
        String url = base + "/api/delete";
        JSONObject json = new JSONObject();
        json.put("path", path);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject, "toString(...)");
        RequestBody body = companion.create(jSONObject, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Result.Companion companion2 = Result.INSTANCE;
                Object m382constructorimpl = Result.m382constructorimpl(Boolean.valueOf(execute.isSuccessful()));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion3 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: fetchFolders-0E7RQCE$default, reason: not valid java name */
    public static /* synthetic */ Object m359fetchFolders0E7RQCE$default(NasApiClient nasApiClient, Context context, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        if ((i & 4) != 0) {
            z = false;
        }
        return nasApiClient.m367fetchFolders0E7RQCE(context, str, z);
    }

    /* renamed from: fetchFolders-0E7RQCE, reason: not valid java name */
    public final Object m367fetchFolders0E7RQCE(Context context, String folder, boolean isVault) {
        Throwable th;
        String string;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(folder, "folder");
        String base = getBaseUrl(context);
        String str = "";
        String vaultParam = isVault ? "&vault=1" : "";
        String url = base + "/api/folders?folder=" + Uri.encode(folder) + vaultParam;
        Request request = applyAuth(context, new Request.Builder().url(url).get()).build();
        try {
            try {
                Response execute = client.newCall(request).execute();
                try {
                    Response response = execute;
                    try {
                        try {
                            if (!response.isSuccessful()) {
                                if (isVault && response.code() == 403) {
                                    Result.Companion companion = Result.INSTANCE;
                                    Object m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException("Thư mục bí mật đang khóa")));
                                    CloseableKt.closeFinally(execute, null);
                                    return m382constructorimpl;
                                }
                                Result.Companion companion2 = Result.INSTANCE;
                                Object m382constructorimpl2 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Lỗi máy chủ (" + response.code() + ")")));
                                CloseableKt.closeFinally(execute, null);
                                return m382constructorimpl2;
                            }
                            ResponseBody body = response.body();
                            try {
                                if (body != null && (string = body.string()) != null) {
                                    JSONObject jSONObject = new JSONObject(string);
                                    if (!jSONObject.optBoolean("ok", false)) {
                                        if (!Intrinsics.areEqual(jSONObject.optString("code", ""), "VAULT_LOCKED")) {
                                            Result.Companion companion3 = Result.INSTANCE;
                                            Object m382constructorimpl3 = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi nạp thư mục"))));
                                            CloseableKt.closeFinally(execute, null);
                                            return m382constructorimpl3;
                                        }
                                        Result.Companion companion4 = Result.INSTANCE;
                                        String optString = jSONObject.optString("error", "Thư mục bí mật đang khóa");
                                        Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                                        Object m382constructorimpl4 = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException(optString)));
                                        CloseableKt.closeFinally(execute, null);
                                        return m382constructorimpl4;
                                    }
                                    String optString2 = jSONObject.optString("currentFolder", "");
                                    String optString3 = jSONObject.optString("parentFolder", "");
                                    JSONArray optJSONArray = jSONObject.optJSONArray("folders");
                                    if (optJSONArray == null) {
                                        optJSONArray = new JSONArray();
                                    }
                                    try {
                                        ArrayList arrayList = new ArrayList(optJSONArray.length());
                                        int length = optJSONArray.length();
                                        int i = 0;
                                        while (i < length) {
                                            try {
                                                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                                                JSONArray jSONArray = optJSONArray;
                                                int i2 = length;
                                                int i3 = i;
                                                String optString4 = jSONObject2.optString("name", str);
                                                Intrinsics.checkNotNullExpressionValue(optString4, "optString(...)");
                                                String url2 = url;
                                                try {
                                                    String optString5 = jSONObject2.optString("path", str);
                                                    Intrinsics.checkNotNullExpressionValue(optString5, "optString(...)");
                                                    String str2 = str;
                                                    Request request2 = request;
                                                    arrayList.add(new NasFolderItem(optString4, optString5, jSONObject2.optInt("subFolderCount", 0)));
                                                    i = i3 + 1;
                                                    optJSONArray = jSONArray;
                                                    url = url2;
                                                    length = i2;
                                                    str = str2;
                                                    request = request2;
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    try {
                                                        throw th;
                                                    } catch (Throwable th3) {
                                                        CloseableKt.closeFinally(execute, th);
                                                        throw th3;
                                                    }
                                                }
                                            } catch (Throwable th4) {
                                                th = th4;
                                            }
                                        }
                                        Result.Companion companion5 = Result.INSTANCE;
                                        Intrinsics.checkNotNull(optString2);
                                        Intrinsics.checkNotNull(optString3);
                                        Object m382constructorimpl5 = Result.m382constructorimpl(new FolderListResult(optString2, optString3, arrayList));
                                        CloseableKt.closeFinally(execute, null);
                                        return m382constructorimpl5;
                                    } catch (Throwable th5) {
                                        th = th5;
                                    }
                                }
                                Result.Companion companion6 = Result.INSTANCE;
                                Object m382constructorimpl6 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Không có dữ liệu phản hồi")));
                                CloseableKt.closeFinally(execute, null);
                                return m382constructorimpl6;
                            } catch (Throwable th6) {
                                th = th6;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                        }
                    } catch (Exception e) {
                        e = e;
                        Result.Companion companion7 = Result.INSTANCE;
                        return Result.m382constructorimpl(ResultKt.createFailure(e));
                    }
                } catch (Throwable th8) {
                    th = th8;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            Result.Companion companion72 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: moveItems-BWLJW6A$default, reason: not valid java name */
    public static /* synthetic */ Object m361moveItemsBWLJW6A$default(NasApiClient nasApiClient, Context context, List list, String str, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        return nasApiClient.m374moveItemsBWLJW6A(context, list, str, z);
    }

    /* renamed from: moveItems-BWLJW6A, reason: not valid java name */
    public final Object m374moveItemsBWLJW6A(Context context, List<String> paths, String targetFolder, boolean isVault) {
        String str;
        JSONObject jSONObject;
        Object m382constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(paths, "paths");
        Intrinsics.checkNotNullParameter(targetFolder, "targetFolder");
        String base = getBaseUrl(context);
        String url = base + "/api/move";
        JSONObject json = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        Iterator<String> it = paths.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next());
        }
        json.put("paths", jSONArray);
        json.put("targetFolder", targetFolder);
        json.put("isVault", isVault);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject2 = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject2, "toString(...)");
        RequestBody body = companion.create(jSONObject2, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                ResponseBody body2 = response.body();
                if (body2 == null || (str = body2.string()) == null) {
                    str = "";
                }
                try {
                    jSONObject = new JSONObject(str);
                } catch (Exception e) {
                    jSONObject = new JSONObject();
                }
                if (response.isSuccessful() && jSONObject.optBoolean("ok", false)) {
                    int optInt = jSONObject.optInt("movedCount", 0);
                    JSONArray optJSONArray = jSONObject.optJSONArray("errors");
                    ArrayList arrayList = new ArrayList();
                    if (optJSONArray != null) {
                        int i = 0;
                        for (int length = optJSONArray.length(); i < length; length = length) {
                            arrayList.add(optJSONArray.getString(i));
                            i++;
                        }
                    }
                    Result.Companion companion2 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(new MoveResult(optInt, arrayList));
                } else {
                    if (!Intrinsics.areEqual(jSONObject.optString("code", ""), "VAULT_LOCKED") && response.code() != 403) {
                        Result.Companion companion3 = Result.INSTANCE;
                        m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi di chuyển tệp (" + response.code() + ")"))));
                    }
                    Result.Companion companion4 = Result.INSTANCE;
                    String optString = jSONObject.optString("error", "Thư mục bí mật đang khóa");
                    Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                    m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException(optString)));
                }
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e2) {
            Result.Companion companion5 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
    }

    /* renamed from: fetchVaultStatus-IoAF18A, reason: not valid java name */
    public final Object m370fetchVaultStatusIoAF18A(Context context) {
        String string;
        Intrinsics.checkNotNullParameter(context, "context");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/status";
        Request request = applyAuth(context, new Request.Builder().url(url).get()).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                if (!response.isSuccessful()) {
                    Result.Companion companion = Result.INSTANCE;
                    Object m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Lỗi trạng thái Két sắt (" + response.code() + ")")));
                    CloseableKt.closeFinally(execute, null);
                    return m382constructorimpl;
                }
                ResponseBody body = response.body();
                if (body != null && (string = body.string()) != null) {
                    JSONObject jSONObject = new JSONObject(string);
                    if (!jSONObject.optBoolean("ok", false)) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Object m382constructorimpl2 = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi kiểm tra Két sắt"))));
                        CloseableKt.closeFinally(execute, null);
                        return m382constructorimpl2;
                    }
                    Result.Companion companion3 = Result.INSTANCE;
                    Object m382constructorimpl3 = Result.m382constructorimpl(new VaultStatus(jSONObject.optBoolean("isConfigured", false), jSONObject.optBoolean("isUnlocked", false)));
                    CloseableKt.closeFinally(execute, null);
                    return m382constructorimpl3;
                }
                Result.Companion companion4 = Result.INSTANCE;
                Object m382constructorimpl4 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Không có dữ liệu phản hồi")));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl4;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion5 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: setupVault-gIAlu-s, reason: not valid java name */
    public final Object m376setupVaultgIAlus(Context context, String password) {
        String str;
        JSONObject jSONObject;
        Object m382constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(password, "password");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/setup";
        JSONObject json = new JSONObject();
        json.put("password", password);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject2 = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject2, "toString(...)");
        RequestBody body = companion.create(jSONObject2, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                ResponseBody body2 = response.body();
                if (body2 == null || (str = body2.string()) == null) {
                    str = "";
                }
                try {
                    jSONObject = new JSONObject(str);
                } catch (Exception e) {
                    jSONObject = new JSONObject();
                }
                if (response.isSuccessful() && jSONObject.optBoolean("ok", false)) {
                    Result.Companion companion2 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(true);
                } else {
                    Result.Companion companion3 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Khởi tạo Thư mục bí mật thất bại (" + response.code() + ")"))));
                }
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e2) {
            Result.Companion companion4 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
    }

    /* renamed from: unlockVault-gIAlu-s, reason: not valid java name */
    public final Object m377unlockVaultgIAlus(Context context, String password) {
        String str;
        JSONObject jSONObject;
        Object m382constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(password, "password");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/unlock";
        JSONObject json = new JSONObject();
        json.put("password", password);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject2 = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject2, "toString(...)");
        RequestBody body = companion.create(jSONObject2, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                ResponseBody body2 = response.body();
                if (body2 == null || (str = body2.string()) == null) {
                    str = "";
                }
                try {
                    jSONObject = new JSONObject(str);
                } catch (Exception e) {
                    jSONObject = new JSONObject();
                }
                if (response.isSuccessful() && jSONObject.optBoolean("ok", false)) {
                    Result.Companion companion2 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(true);
                } else {
                    Result.Companion companion3 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Mật khẩu Thư mục bí mật không đúng"))));
                }
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e2) {
            Result.Companion companion4 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
    }

    /* renamed from: lockVault-IoAF18A, reason: not valid java name */
    public final Object m372lockVaultIoAF18A(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/lock";
        RequestBody body = RequestBody.INSTANCE.create("{}", MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Result.Companion companion = Result.INSTANCE;
                Object m382constructorimpl = Result.m382constructorimpl(Boolean.valueOf(execute.isSuccessful()));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion2 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    /* renamed from: importToVault-gIAlu-s, reason: not valid java name */
    public final Object m371importToVaultgIAlus(Context context, List<String> paths) {
        String str;
        JSONObject jSONObject;
        Object m382constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(paths, "paths");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/import";
        JSONObject json = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        Iterator<String> it = paths.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next());
        }
        json.put("paths", jSONArray);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject2 = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject2, "toString(...)");
        RequestBody body = companion.create(jSONObject2, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                ResponseBody body2 = response.body();
                if (body2 == null || (str = body2.string()) == null) {
                    str = "";
                }
                try {
                    jSONObject = new JSONObject(str);
                } catch (Exception e) {
                    jSONObject = new JSONObject();
                }
                if (response.isSuccessful() && jSONObject.optBoolean("ok", false)) {
                    int optInt = jSONObject.optInt("total", paths.size());
                    JSONArray optJSONArray = jSONObject.optJSONArray("imported");
                    Result.Companion companion2 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(Integer.valueOf(optJSONArray != null ? optJSONArray.length() : optInt));
                } else {
                    if (!Intrinsics.areEqual(jSONObject.optString("code", ""), "VAULT_LOCKED") && response.code() != 403) {
                        Result.Companion companion3 = Result.INSTANCE;
                        m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi chuyển tệp vào Thư mục bí mật"))));
                    }
                    Result.Companion companion4 = Result.INSTANCE;
                    String optString = jSONObject.optString("error", "Thư mục bí mật đang khóa");
                    Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                    m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException(optString)));
                }
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e2) {
            Result.Companion companion5 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
    }

    /* renamed from: fetchVaultFiles-gIAlu-s$default, reason: not valid java name */
    public static /* synthetic */ Object m360fetchVaultFilesgIAlus$default(NasApiClient nasApiClient, Context context, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        return nasApiClient.m369fetchVaultFilesgIAlus(context, str);
    }

    /* renamed from: fetchVaultFiles-gIAlu-s, reason: not valid java name */
    public final Object m369fetchVaultFilesgIAlus(Context context, String folder) {
        Throwable th;
        String string;
        String str;
        String str2;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(folder, "folder");
        String base = getBaseUrl(context);
        String url = (folder.length() > 0 ? new StringBuilder().append(base).append("/api/vault/files?folder=").append(Uri.encode(folder)) : new StringBuilder().append(base).append("/api/vault/files")).toString();
        Request request = applyAuth(context, new Request.Builder().url(url).get()).build();
        try {
            try {
                Response execute = client.newCall(request).execute();
                try {
                    Response response = execute;
                    int i = 0;
                    try {
                        try {
                            if (!response.isSuccessful()) {
                                if (response.code() == 403) {
                                    Result.Companion companion = Result.INSTANCE;
                                    Object m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException("Thư mục bí mật đang khóa")));
                                    CloseableKt.closeFinally(execute, null);
                                    return m382constructorimpl;
                                }
                                Result.Companion companion2 = Result.INSTANCE;
                                Object m382constructorimpl2 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Mã lỗi: " + response.code())));
                                CloseableKt.closeFinally(execute, null);
                                return m382constructorimpl2;
                            }
                            ResponseBody body = response.body();
                            try {
                                if (body != null && (string = body.string()) != null) {
                                    JSONObject jSONObject = new JSONObject(string);
                                    if (!jSONObject.optBoolean("ok", false)) {
                                        if (!Intrinsics.areEqual(jSONObject.optString("code", ""), "VAULT_LOCKED")) {
                                            Result.Companion companion3 = Result.INSTANCE;
                                            Object m382constructorimpl3 = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi nạp tệp Thư mục bí mật"))));
                                            CloseableKt.closeFinally(execute, null);
                                            return m382constructorimpl3;
                                        }
                                        Result.Companion companion4 = Result.INSTANCE;
                                        String optString = jSONObject.optString("error", "Thư mục bí mật đang khóa");
                                        Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                                        Object m382constructorimpl4 = Result.m382constructorimpl(ResultKt.createFailure(new VaultLockedException(optString)));
                                        CloseableKt.closeFinally(execute, null);
                                        return m382constructorimpl4;
                                    }
                                    JSONArray optJSONArray = jSONObject.optJSONArray("items");
                                    if (optJSONArray == null) {
                                        Result.Companion companion5 = Result.INSTANCE;
                                        Object m382constructorimpl5 = Result.m382constructorimpl(CollectionsKt.emptyList());
                                        CloseableKt.closeFinally(execute, null);
                                        return m382constructorimpl5;
                                    }
                                    ArrayList arrayList = new ArrayList(optJSONArray.length());
                                    int length = optJSONArray.length();
                                    int i2 = 0;
                                    while (i2 < length) {
                                        JSONObject jSONObject2 = optJSONArray.getJSONObject(i2);
                                        int i3 = length;
                                        int i4 = i2;
                                        String optString2 = jSONObject2.optString(TtmlNode.ATTR_ID, "");
                                        String base2 = base;
                                        try {
                                            String optString3 = jSONObject2.optString("name", "Unknown");
                                            String url2 = url;
                                            try {
                                                boolean optBoolean = jSONObject2.optBoolean("isFolder", false);
                                                JSONArray jSONArray = optJSONArray;
                                                long optLong = jSONObject2.optLong("size", 0L);
                                                Intrinsics.checkNotNull(optString3);
                                                Request request2 = request;
                                                Response response2 = response;
                                                int i5 = i;
                                                if (StringsKt.contains$default((CharSequence) optString3, '.', false, 2, (Object) null)) {
                                                    String substring = optString3.substring(StringsKt.lastIndexOf$default((CharSequence) optString3, '.', 0, false, 6, (Object) null));
                                                    Intrinsics.checkNotNullExpressionValue(substring, "substring(...)");
                                                    String lowerCase = substring.toLowerCase(Locale.ROOT);
                                                    Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                                                    str = lowerCase;
                                                } else {
                                                    str = "";
                                                }
                                                String str3 = str;
                                                boolean contains = CollectionsKt.listOf((Object[]) new String[]{".mp4", ".mkv", ".mov", ".avi", ".webm", ".m4v"}).contains(str3);
                                                boolean contains2 = CollectionsKt.listOf((Object[]) new String[]{".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp", ".svg", ".heic"}).contains(str3);
                                                if (optBoolean) {
                                                    str2 = "folder";
                                                } else {
                                                    str2 = contains ? MimeTypes.BASE_TYPE_VIDEO : contains2 ? "image" : "other";
                                                }
                                                String optString4 = jSONObject2.optString("path", optString2);
                                                Intrinsics.checkNotNull(optString4);
                                                String formatBytes = optBoolean ? "--" : INSTANCE.formatBytes(optLong);
                                                String optString5 = jSONObject2.optString("createdAt", "--");
                                                Intrinsics.checkNotNullExpressionValue(optString5, "optString(...)");
                                                Intrinsics.checkNotNull(optString2);
                                                arrayList.add(new NasItem(optString3, optString4, optBoolean, optLong, formatBytes, optString5, str2, str3, true, optString2));
                                                i2 = i4 + 1;
                                                length = i3;
                                                base = base2;
                                                url = url2;
                                                optJSONArray = jSONArray;
                                                request = request2;
                                                response = response2;
                                                i = i5;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                try {
                                                    throw th;
                                                } catch (Throwable th3) {
                                                    CloseableKt.closeFinally(execute, th);
                                                    throw th3;
                                                }
                                            }
                                        } catch (Throwable th4) {
                                            th = th4;
                                        }
                                    }
                                    Result.Companion companion6 = Result.INSTANCE;
                                    Object m382constructorimpl6 = Result.m382constructorimpl(arrayList);
                                    CloseableKt.closeFinally(execute, null);
                                    return m382constructorimpl6;
                                }
                                Result.Companion companion7 = Result.INSTANCE;
                                Object m382constructorimpl7 = Result.m382constructorimpl(ResultKt.createFailure(new IOException("Không có dữ liệu phản hồi")));
                                CloseableKt.closeFinally(execute, null);
                                return m382constructorimpl7;
                            } catch (Throwable th5) {
                                th = th5;
                            }
                        } catch (Exception e) {
                            e = e;
                            Result.Companion companion8 = Result.INSTANCE;
                            return Result.m382constructorimpl(ResultKt.createFailure(e));
                        }
                    } catch (Throwable th6) {
                        th = th6;
                    }
                } catch (Throwable th7) {
                    th = th7;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* renamed from: exportFromVault-0E7RQCE$default, reason: not valid java name */
    public static /* synthetic */ Object m357exportFromVault0E7RQCE$default(NasApiClient nasApiClient, Context context, List list, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            str = "";
        }
        return nasApiClient.m365exportFromVault0E7RQCE(context, list, str);
    }

    /* renamed from: exportFromVault-0E7RQCE, reason: not valid java name */
    public final Object m365exportFromVault0E7RQCE(Context context, List<String> ids, String targetFolder) {
        String str;
        JSONObject jSONObject;
        Object m382constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(ids, "ids");
        Intrinsics.checkNotNullParameter(targetFolder, "targetFolder");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/export";
        JSONObject json = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        Iterator<String> it = ids.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next());
        }
        json.put("ids", jSONArray);
        json.put("targetFolder", targetFolder);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject2 = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject2, "toString(...)");
        RequestBody body = companion.create(jSONObject2, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Response response = execute;
                ResponseBody body2 = response.body();
                if (body2 == null || (str = body2.string()) == null) {
                    str = "";
                }
                try {
                    jSONObject = new JSONObject(str);
                } catch (Exception e) {
                    jSONObject = new JSONObject();
                }
                if (response.isSuccessful() && jSONObject.optBoolean("ok", false)) {
                    JSONArray optJSONArray = jSONObject.optJSONArray("exported");
                    Result.Companion companion2 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(Integer.valueOf(optJSONArray != null ? optJSONArray.length() : ids.size()));
                } else {
                    Result.Companion companion3 = Result.INSTANCE;
                    m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(new IOException(jSONObject.optString("error", "Lỗi giải mã đưa về Drive"))));
                }
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e2) {
            Result.Companion companion4 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
    }

    /* renamed from: deleteVaultItem-gIAlu-s, reason: not valid java name */
    public final Object m364deleteVaultItemgIAlus(Context context, String id) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(id, "id");
        String base = getBaseUrl(context);
        String url = base + "/api/vault/delete";
        JSONObject json = new JSONObject();
        json.put(TtmlNode.ATTR_ID, id);
        RequestBody.Companion companion = RequestBody.INSTANCE;
        String jSONObject = json.toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject, "toString(...)");
        RequestBody body = companion.create(jSONObject, MediaType.INSTANCE.get("application/json"));
        Request request = applyAuth(context, new Request.Builder().url(url).post(body)).build();
        try {
            Response execute = client.newCall(request).execute();
            try {
                Result.Companion companion2 = Result.INSTANCE;
                Object m382constructorimpl = Result.m382constructorimpl(Boolean.valueOf(execute.isSuccessful()));
                CloseableKt.closeFinally(execute, null);
                return m382constructorimpl;
            } finally {
            }
        } catch (Exception e) {
            Result.Companion companion3 = Result.INSTANCE;
            return Result.m382constructorimpl(ResultKt.createFailure(e));
        }
    }

    public final String formatBytes(long bytes) {
        if (bytes <= 0) {
            return "0 B";
        }
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024.0d));
        int index = digitGroups < units.length ? digitGroups : units.length - 1;
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String format = String.format(Locale.getDefault(), "%.1f %s", Arrays.copyOf(new Object[]{Double.valueOf(bytes / Math.pow(1024.0d, index)), units[index]}, 2));
        Intrinsics.checkNotNullExpressionValue(format, "format(...)");
        return format;
    }
}

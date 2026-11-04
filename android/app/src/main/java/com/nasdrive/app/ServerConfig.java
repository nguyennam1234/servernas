package com.nasdrive.app;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: ServerConfig.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0012\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0015J\u000e\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0005J\u000e\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0005J\u000e\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u0005J\u000e\u0010!\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\"\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0005J\u000e\u0010$\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010%\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010&\u001a\u00020\u0005J\u000e\u0010'\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010(\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/nasdrive/app/ServerConfig;", "", "<init>", "()V", "URL_LAN_88", "", "URL_LAN_1", "URL_LAN_DOMAIN", "URL_WAN_CLOUDFLARE", "PREF_NAME", "KEY_MODE_AUTO", "KEY_SELECTED_URL", "KEY_ACTIVE_URL", "KEY_AUTH_TOKEN", "KEY_USERNAME", "KEY_USER_FULL_NAME", "getPrefs", "Landroid/content/SharedPreferences;", "context", "Landroid/content/Context;", "isAutoMode", "", "setAutoMode", "", "isAuto", "getSelectedUrl", "setSelectedUrl", "url", "getActiveUrl", "setActiveUrl", "getAuthToken", "setAuthToken", "token", "getUsername", "setUsername", ServerConfig.KEY_USERNAME, "getUserFullName", "setUserFullName", "name", "clearAuth", "isLanUrl", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class ServerConfig {
    public static final ServerConfig INSTANCE = new ServerConfig();
    private static final String KEY_ACTIVE_URL = "active_url";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_MODE_AUTO = "mode_auto";
    private static final String KEY_SELECTED_URL = "selected_url";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_FULL_NAME = "user_full_name";
    private static final String PREF_NAME = "nas_drive_prefs";
    public static final String URL_LAN_1 = "http://192.168.1.8:8080";
    public static final String URL_LAN_88 = "http://192.168.88.230:8080";
    public static final String URL_LAN_DOMAIN = "http://nas.lan:8080";
    public static final String URL_WAN_CLOUDFLARE = "https://luutru.npccc.io.vn";

    private ServerConfig() {
    }

    private final SharedPreferences getPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        return sharedPreferences;
    }

    public final boolean isAutoMode(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return getPrefs(context).getBoolean(KEY_MODE_AUTO, true);
    }

    public final void setAutoMode(Context context, boolean isAuto) {
        Intrinsics.checkNotNullParameter(context, "context");
        getPrefs(context).edit().putBoolean(KEY_MODE_AUTO, isAuto).apply();
    }

    public final String getSelectedUrl(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String string = getPrefs(context).getString(KEY_SELECTED_URL, URL_LAN_88);
        return string == null ? URL_LAN_88 : string;
    }

    public final void setSelectedUrl(Context context, String url) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(url, "url");
        getPrefs(context).edit().putString(KEY_SELECTED_URL, StringsKt.trimEnd(StringsKt.trim((CharSequence) url).toString(), '/')).apply();
    }

    public final String getActiveUrl(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String string = getPrefs(context).getString(KEY_ACTIVE_URL, URL_LAN_88);
        return string == null ? URL_LAN_88 : string;
    }

    public final void setActiveUrl(Context context, String url) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(url, "url");
        getPrefs(context).edit().putString(KEY_ACTIVE_URL, StringsKt.trimEnd(StringsKt.trim((CharSequence) url).toString(), '/')).apply();
    }

    public final String getAuthToken(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String string = getPrefs(context).getString(KEY_AUTH_TOKEN, "");
        return string == null ? "" : string;
    }

    public final void setAuthToken(Context context, String token) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(token, "token");
        getPrefs(context).edit().putString(KEY_AUTH_TOKEN, StringsKt.trim((CharSequence) token).toString()).apply();
    }

    public final String getUsername(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String string = getPrefs(context).getString(KEY_USERNAME, "admin");
        return string == null ? "admin" : string;
    }

    public final void setUsername(Context context, String username) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(username, "username");
        getPrefs(context).edit().putString(KEY_USERNAME, StringsKt.trim((CharSequence) username).toString()).apply();
    }

    public final String getUserFullName(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String string = getPrefs(context).getString(KEY_USER_FULL_NAME, "Quản trị viên");
        return string == null ? "Quản trị viên" : string;
    }

    public final void setUserFullName(Context context, String name) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(name, "name");
        getPrefs(context).edit().putString(KEY_USER_FULL_NAME, StringsKt.trim((CharSequence) name).toString()).apply();
    }

    public final void clearAuth(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        getPrefs(context).edit().remove(KEY_AUTH_TOKEN).remove(KEY_USERNAME).remove(KEY_USER_FULL_NAME).apply();
    }

    public final boolean isLanUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        String clean = url.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(clean, "toLowerCase(...)");
        return StringsKt.contains$default((CharSequence) clean, (CharSequence) "192.168.", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) clean, (CharSequence) "nas.lan", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) clean, (CharSequence) "luutru.lan", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) clean, (CharSequence) "10.", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) clean, (CharSequence) "172.", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) clean, (CharSequence) ":8080", false, 2, (Object) null);
    }
}

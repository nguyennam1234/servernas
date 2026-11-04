package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ServerDetector.kt */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00052\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001J\t\u0010\u001a\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\r¨\u0006\u001b"}, d2 = {"Lcom/nasdrive/app/DetectionResult;", "", "url", "", "isLan", "", "latencyMs", "", "success", "<init>", "(Ljava/lang/String;ZJZ)V", "getUrl", "()Ljava/lang/String;", "()Z", "getLatencyMs", "()J", "getSuccess", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class DetectionResult {
    private final boolean isLan;
    private final long latencyMs;
    private final boolean success;
    private final String url;

    public static /* synthetic */ DetectionResult copy$default(DetectionResult detectionResult, String str, boolean z, long j, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = detectionResult.url;
        }
        if ((i & 2) != 0) {
            z = detectionResult.isLan;
        }
        if ((i & 4) != 0) {
            j = detectionResult.latencyMs;
        }
        if ((i & 8) != 0) {
            z2 = detectionResult.success;
        }
        boolean z3 = z2;
        return detectionResult.copy(str, z, j, z3);
    }

    /* renamed from: component1, reason: from getter */
    public final String getUrl() {
        return this.url;
    }

    /* renamed from: component2, reason: from getter */
    public final boolean getIsLan() {
        return this.isLan;
    }

    /* renamed from: component3, reason: from getter */
    public final long getLatencyMs() {
        return this.latencyMs;
    }

    /* renamed from: component4, reason: from getter */
    public final boolean getSuccess() {
        return this.success;
    }

    public final DetectionResult copy(String url, boolean isLan, long latencyMs, boolean success) {
        Intrinsics.checkNotNullParameter(url, "url");
        return new DetectionResult(url, isLan, latencyMs, success);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DetectionResult)) {
            return false;
        }
        DetectionResult detectionResult = (DetectionResult) other;
        return Intrinsics.areEqual(this.url, detectionResult.url) && this.isLan == detectionResult.isLan && this.latencyMs == detectionResult.latencyMs && this.success == detectionResult.success;
    }

    public int hashCode() {
        return (((((this.url.hashCode() * 31) + Boolean.hashCode(this.isLan)) * 31) + Long.hashCode(this.latencyMs)) * 31) + Boolean.hashCode(this.success);
    }

    public String toString() {
        return "DetectionResult(url=" + this.url + ", isLan=" + this.isLan + ", latencyMs=" + this.latencyMs + ", success=" + this.success + ")";
    }

    public DetectionResult(String url, boolean isLan, long latencyMs, boolean success) {
        Intrinsics.checkNotNullParameter(url, "url");
        this.url = url;
        this.isLan = isLan;
        this.latencyMs = latencyMs;
        this.success = success;
    }

    public final String getUrl() {
        return this.url;
    }

    public final boolean isLan() {
        return this.isLan;
    }

    public final long getLatencyMs() {
        return this.latencyMs;
    }

    public final boolean getSuccess() {
        return this.success;
    }
}

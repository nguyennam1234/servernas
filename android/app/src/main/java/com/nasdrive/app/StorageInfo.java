package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0007HÆ\u0003J1\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0007HÖ\u0001J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lcom/nasdrive/app/StorageInfo;", "", "totalGb", "", "usedGb", "freeGb", "usedPercent", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getTotalGb", "()Ljava/lang/String;", "getUsedGb", "getFreeGb", "getUsedPercent", "()I", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class StorageInfo {
    private final String freeGb;
    private final String totalGb;
    private final String usedGb;
    private final int usedPercent;

    public static /* synthetic */ StorageInfo copy$default(StorageInfo storageInfo, String str, String str2, String str3, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = storageInfo.totalGb;
        }
        if ((i2 & 2) != 0) {
            str2 = storageInfo.usedGb;
        }
        if ((i2 & 4) != 0) {
            str3 = storageInfo.freeGb;
        }
        if ((i2 & 8) != 0) {
            i = storageInfo.usedPercent;
        }
        return storageInfo.copy(str, str2, str3, i);
    }

    /* renamed from: component1, reason: from getter */
    public final String getTotalGb() {
        return this.totalGb;
    }

    /* renamed from: component2, reason: from getter */
    public final String getUsedGb() {
        return this.usedGb;
    }

    /* renamed from: component3, reason: from getter */
    public final String getFreeGb() {
        return this.freeGb;
    }

    /* renamed from: component4, reason: from getter */
    public final int getUsedPercent() {
        return this.usedPercent;
    }

    public final StorageInfo copy(String totalGb, String usedGb, String freeGb, int usedPercent) {
        Intrinsics.checkNotNullParameter(totalGb, "totalGb");
        Intrinsics.checkNotNullParameter(usedGb, "usedGb");
        Intrinsics.checkNotNullParameter(freeGb, "freeGb");
        return new StorageInfo(totalGb, usedGb, freeGb, usedPercent);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StorageInfo)) {
            return false;
        }
        StorageInfo storageInfo = (StorageInfo) other;
        return Intrinsics.areEqual(this.totalGb, storageInfo.totalGb) && Intrinsics.areEqual(this.usedGb, storageInfo.usedGb) && Intrinsics.areEqual(this.freeGb, storageInfo.freeGb) && this.usedPercent == storageInfo.usedPercent;
    }

    public int hashCode() {
        return (((((this.totalGb.hashCode() * 31) + this.usedGb.hashCode()) * 31) + this.freeGb.hashCode()) * 31) + Integer.hashCode(this.usedPercent);
    }

    public String toString() {
        return "StorageInfo(totalGb=" + this.totalGb + ", usedGb=" + this.usedGb + ", freeGb=" + this.freeGb + ", usedPercent=" + this.usedPercent + ")";
    }

    public StorageInfo(String totalGb, String usedGb, String freeGb, int usedPercent) {
        Intrinsics.checkNotNullParameter(totalGb, "totalGb");
        Intrinsics.checkNotNullParameter(usedGb, "usedGb");
        Intrinsics.checkNotNullParameter(freeGb, "freeGb");
        this.totalGb = totalGb;
        this.usedGb = usedGb;
        this.freeGb = freeGb;
        this.usedPercent = usedPercent;
    }

    public final String getTotalGb() {
        return this.totalGb;
    }

    public final String getUsedGb() {
        return this.usedGb;
    }

    public final String getFreeGb() {
        return this.freeGb;
    }

    public final int getUsedPercent() {
        return this.usedPercent;
    }
}

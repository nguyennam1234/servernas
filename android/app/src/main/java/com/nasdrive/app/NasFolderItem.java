package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0006HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0006HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0017"}, d2 = {"Lcom/nasdrive/app/NasFolderItem;", "", "name", "", "path", "subFolderCount", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V", "getName", "()Ljava/lang/String;", "getPath", "getSubFolderCount", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class NasFolderItem {
    private final String name;
    private final String path;
    private final int subFolderCount;

    public static /* synthetic */ NasFolderItem copy$default(NasFolderItem nasFolderItem, String str, String str2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = nasFolderItem.name;
        }
        if ((i2 & 2) != 0) {
            str2 = nasFolderItem.path;
        }
        if ((i2 & 4) != 0) {
            i = nasFolderItem.subFolderCount;
        }
        return nasFolderItem.copy(str, str2, i);
    }

    /* renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component2, reason: from getter */
    public final String getPath() {
        return this.path;
    }

    /* renamed from: component3, reason: from getter */
    public final int getSubFolderCount() {
        return this.subFolderCount;
    }

    public final NasFolderItem copy(String name, String path, int subFolderCount) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(path, "path");
        return new NasFolderItem(name, path, subFolderCount);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NasFolderItem)) {
            return false;
        }
        NasFolderItem nasFolderItem = (NasFolderItem) other;
        return Intrinsics.areEqual(this.name, nasFolderItem.name) && Intrinsics.areEqual(this.path, nasFolderItem.path) && this.subFolderCount == nasFolderItem.subFolderCount;
    }

    public int hashCode() {
        return (((this.name.hashCode() * 31) + this.path.hashCode()) * 31) + Integer.hashCode(this.subFolderCount);
    }

    public String toString() {
        return "NasFolderItem(name=" + this.name + ", path=" + this.path + ", subFolderCount=" + this.subFolderCount + ")";
    }

    public NasFolderItem(String name, String path, int subFolderCount) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(path, "path");
        this.name = name;
        this.path = path;
        this.subFolderCount = subFolderCount;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPath() {
        return this.path;
    }

    public final int getSubFolderCount() {
        return this.subFolderCount;
    }
}

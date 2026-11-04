package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadedFilesAdapter.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0017\u001a\u00020\tHÆ\u0003J;\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u0019\u001a\u00020\t2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001J\t\u0010\u001d\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0012¨\u0006\u001e"}, d2 = {"Lcom/nasdrive/app/DownloadedItem;", "", "file", "Ljava/io/File;", "name", "", "sizeFormatted", "dateFormatted", "isVideo", "", "<init>", "(Ljava/io/File;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getFile", "()Ljava/io/File;", "getName", "()Ljava/lang/String;", "getSizeFormatted", "getDateFormatted", "()Z", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class DownloadedItem {
    private final String dateFormatted;
    private final File file;
    private final boolean isVideo;
    private final String name;
    private final String sizeFormatted;

    public static /* synthetic */ DownloadedItem copy$default(DownloadedItem downloadedItem, File file, String str, String str2, String str3, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            file = downloadedItem.file;
        }
        if ((i & 2) != 0) {
            str = downloadedItem.name;
        }
        if ((i & 4) != 0) {
            str2 = downloadedItem.sizeFormatted;
        }
        if ((i & 8) != 0) {
            str3 = downloadedItem.dateFormatted;
        }
        if ((i & 16) != 0) {
            z = downloadedItem.isVideo;
        }
        boolean z2 = z;
        String str4 = str2;
        return downloadedItem.copy(file, str, str4, str3, z2);
    }

    /* renamed from: component1, reason: from getter */
    public final File getFile() {
        return this.file;
    }

    /* renamed from: component2, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component3, reason: from getter */
    public final String getSizeFormatted() {
        return this.sizeFormatted;
    }

    /* renamed from: component4, reason: from getter */
    public final String getDateFormatted() {
        return this.dateFormatted;
    }

    /* renamed from: component5, reason: from getter */
    public final boolean getIsVideo() {
        return this.isVideo;
    }

    public final DownloadedItem copy(File file, String name, String sizeFormatted, String dateFormatted, boolean isVideo) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(sizeFormatted, "sizeFormatted");
        Intrinsics.checkNotNullParameter(dateFormatted, "dateFormatted");
        return new DownloadedItem(file, name, sizeFormatted, dateFormatted, isVideo);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DownloadedItem)) {
            return false;
        }
        DownloadedItem downloadedItem = (DownloadedItem) other;
        return Intrinsics.areEqual(this.file, downloadedItem.file) && Intrinsics.areEqual(this.name, downloadedItem.name) && Intrinsics.areEqual(this.sizeFormatted, downloadedItem.sizeFormatted) && Intrinsics.areEqual(this.dateFormatted, downloadedItem.dateFormatted) && this.isVideo == downloadedItem.isVideo;
    }

    public int hashCode() {
        return (((((((this.file.hashCode() * 31) + this.name.hashCode()) * 31) + this.sizeFormatted.hashCode()) * 31) + this.dateFormatted.hashCode()) * 31) + Boolean.hashCode(this.isVideo);
    }

    public String toString() {
        return "DownloadedItem(file=" + this.file + ", name=" + this.name + ", sizeFormatted=" + this.sizeFormatted + ", dateFormatted=" + this.dateFormatted + ", isVideo=" + this.isVideo + ")";
    }

    public DownloadedItem(File file, String name, String sizeFormatted, String dateFormatted, boolean isVideo) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(sizeFormatted, "sizeFormatted");
        Intrinsics.checkNotNullParameter(dateFormatted, "dateFormatted");
        this.file = file;
        this.name = name;
        this.sizeFormatted = sizeFormatted;
        this.dateFormatted = dateFormatted;
        this.isVideo = isVideo;
    }

    public final File getFile() {
        return this.file;
    }

    public final String getName() {
        return this.name;
    }

    public final String getSizeFormatted() {
        return this.sizeFormatted;
    }

    public final String getDateFormatted() {
        return this.dateFormatted;
    }

    public final boolean isVideo() {
        return this.isVideo;
    }
}

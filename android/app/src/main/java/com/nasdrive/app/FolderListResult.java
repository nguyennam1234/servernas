package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006HÆ\u0003J-\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0019"}, d2 = {"Lcom/nasdrive/app/FolderListResult;", "", "currentFolder", "", "parentFolder", "folders", "", "Lcom/nasdrive/app/NasFolderItem;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getCurrentFolder", "()Ljava/lang/String;", "getParentFolder", "getFolders", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class FolderListResult {
    private final String currentFolder;
    private final List<NasFolderItem> folders;
    private final String parentFolder;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ FolderListResult copy$default(FolderListResult folderListResult, String str, String str2, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            str = folderListResult.currentFolder;
        }
        if ((i & 2) != 0) {
            str2 = folderListResult.parentFolder;
        }
        if ((i & 4) != 0) {
            list = folderListResult.folders;
        }
        return folderListResult.copy(str, str2, list);
    }

    /* renamed from: component1, reason: from getter */
    public final String getCurrentFolder() {
        return this.currentFolder;
    }

    /* renamed from: component2, reason: from getter */
    public final String getParentFolder() {
        return this.parentFolder;
    }

    public final List<NasFolderItem> component3() {
        return this.folders;
    }

    public final FolderListResult copy(String currentFolder, String parentFolder, List<NasFolderItem> folders) {
        Intrinsics.checkNotNullParameter(currentFolder, "currentFolder");
        Intrinsics.checkNotNullParameter(parentFolder, "parentFolder");
        Intrinsics.checkNotNullParameter(folders, "folders");
        return new FolderListResult(currentFolder, parentFolder, folders);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FolderListResult)) {
            return false;
        }
        FolderListResult folderListResult = (FolderListResult) other;
        return Intrinsics.areEqual(this.currentFolder, folderListResult.currentFolder) && Intrinsics.areEqual(this.parentFolder, folderListResult.parentFolder) && Intrinsics.areEqual(this.folders, folderListResult.folders);
    }

    public int hashCode() {
        return (((this.currentFolder.hashCode() * 31) + this.parentFolder.hashCode()) * 31) + this.folders.hashCode();
    }

    public String toString() {
        return "FolderListResult(currentFolder=" + this.currentFolder + ", parentFolder=" + this.parentFolder + ", folders=" + this.folders + ")";
    }

    public FolderListResult(String currentFolder, String parentFolder, List<NasFolderItem> folders) {
        Intrinsics.checkNotNullParameter(currentFolder, "currentFolder");
        Intrinsics.checkNotNullParameter(parentFolder, "parentFolder");
        Intrinsics.checkNotNullParameter(folders, "folders");
        this.currentFolder = currentFolder;
        this.parentFolder = parentFolder;
        this.folders = folders;
    }

    public final String getCurrentFolder() {
        return this.currentFolder;
    }

    public final String getParentFolder() {
        return this.parentFolder;
    }

    public final List<NasFolderItem> getFolders() {
        return this.folders;
    }
}

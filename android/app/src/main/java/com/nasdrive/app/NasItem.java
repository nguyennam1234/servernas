package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b!\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003¢\u0006\u0004\b\u000f\u0010\u0010J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001f\u001a\u00020\bHÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0006HÆ\u0003J\t\u0010%\u001a\u00020\u0003HÆ\u0003Jm\u0010&\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u0003HÆ\u0001J\u0013\u0010'\u001a\u00020\u00062\b\u0010(\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010)\u001a\u00020*HÖ\u0001J\t\u0010+\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012R\u0011\u0010\r\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012¨\u0006,"}, d2 = {"Lcom/nasdrive/app/NasItem;", "", "name", "", "path", "isFolder", "", "size", "", "formattedSize", "formattedDate", "type", "ext", "isVault", "vaultId", "<init>", "(Ljava/lang/String;Ljava/lang/String;ZJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", "getName", "()Ljava/lang/String;", "getPath", "()Z", "getSize", "()J", "getFormattedSize", "getFormattedDate", "getType", "getExt", "getVaultId", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class NasItem {
    private final String ext;
    private final String formattedDate;
    private final String formattedSize;
    private final boolean isFolder;
    private final boolean isVault;
    private final String name;
    private final String path;
    private final long size;
    private final String type;
    private final String vaultId;

    public static /* synthetic */ NasItem copy$default(NasItem nasItem, String str, String str2, boolean z, long j, String str3, String str4, String str5, String str6, boolean z2, String str7, int i, Object obj) {
        if ((i & 1) != 0) {
            str = nasItem.name;
        }
        if ((i & 2) != 0) {
            str2 = nasItem.path;
        }
        if ((i & 4) != 0) {
            z = nasItem.isFolder;
        }
        if ((i & 8) != 0) {
            j = nasItem.size;
        }
        if ((i & 16) != 0) {
            str3 = nasItem.formattedSize;
        }
        if ((i & 32) != 0) {
            str4 = nasItem.formattedDate;
        }
        if ((i & 64) != 0) {
            str5 = nasItem.type;
        }
        if ((i & 128) != 0) {
            str6 = nasItem.ext;
        }
        if ((i & 256) != 0) {
            z2 = nasItem.isVault;
        }
        if ((i & 512) != 0) {
            str7 = nasItem.vaultId;
        }
        boolean z3 = z2;
        String str8 = str7;
        long j2 = j;
        boolean z4 = z;
        return nasItem.copy(str, str2, z4, j2, str3, str4, str5, str6, z3, str8);
    }

    /* renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component10, reason: from getter */
    public final String getVaultId() {
        return this.vaultId;
    }

    /* renamed from: component2, reason: from getter */
    public final String getPath() {
        return this.path;
    }

    /* renamed from: component3, reason: from getter */
    public final boolean getIsFolder() {
        return this.isFolder;
    }

    /* renamed from: component4, reason: from getter */
    public final long getSize() {
        return this.size;
    }

    /* renamed from: component5, reason: from getter */
    public final String getFormattedSize() {
        return this.formattedSize;
    }

    /* renamed from: component6, reason: from getter */
    public final String getFormattedDate() {
        return this.formattedDate;
    }

    /* renamed from: component7, reason: from getter */
    public final String getType() {
        return this.type;
    }

    /* renamed from: component8, reason: from getter */
    public final String getExt() {
        return this.ext;
    }

    /* renamed from: component9, reason: from getter */
    public final boolean getIsVault() {
        return this.isVault;
    }

    public final NasItem copy(String name, String path, boolean isFolder, long size, String formattedSize, String formattedDate, String type, String ext, boolean isVault, String vaultId) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(formattedSize, "formattedSize");
        Intrinsics.checkNotNullParameter(formattedDate, "formattedDate");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(ext, "ext");
        Intrinsics.checkNotNullParameter(vaultId, "vaultId");
        return new NasItem(name, path, isFolder, size, formattedSize, formattedDate, type, ext, isVault, vaultId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NasItem)) {
            return false;
        }
        NasItem nasItem = (NasItem) other;
        return Intrinsics.areEqual(this.name, nasItem.name) && Intrinsics.areEqual(this.path, nasItem.path) && this.isFolder == nasItem.isFolder && this.size == nasItem.size && Intrinsics.areEqual(this.formattedSize, nasItem.formattedSize) && Intrinsics.areEqual(this.formattedDate, nasItem.formattedDate) && Intrinsics.areEqual(this.type, nasItem.type) && Intrinsics.areEqual(this.ext, nasItem.ext) && this.isVault == nasItem.isVault && Intrinsics.areEqual(this.vaultId, nasItem.vaultId);
    }

    public int hashCode() {
        return (((((((((((((((((this.name.hashCode() * 31) + this.path.hashCode()) * 31) + Boolean.hashCode(this.isFolder)) * 31) + Long.hashCode(this.size)) * 31) + this.formattedSize.hashCode()) * 31) + this.formattedDate.hashCode()) * 31) + this.type.hashCode()) * 31) + this.ext.hashCode()) * 31) + Boolean.hashCode(this.isVault)) * 31) + this.vaultId.hashCode();
    }

    public String toString() {
        return "NasItem(name=" + this.name + ", path=" + this.path + ", isFolder=" + this.isFolder + ", size=" + this.size + ", formattedSize=" + this.formattedSize + ", formattedDate=" + this.formattedDate + ", type=" + this.type + ", ext=" + this.ext + ", isVault=" + this.isVault + ", vaultId=" + this.vaultId + ")";
    }

    public NasItem(String name, String path, boolean isFolder, long size, String formattedSize, String formattedDate, String type, String ext, boolean isVault, String vaultId) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(formattedSize, "formattedSize");
        Intrinsics.checkNotNullParameter(formattedDate, "formattedDate");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(ext, "ext");
        Intrinsics.checkNotNullParameter(vaultId, "vaultId");
        this.name = name;
        this.path = path;
        this.isFolder = isFolder;
        this.size = size;
        this.formattedSize = formattedSize;
        this.formattedDate = formattedDate;
        this.type = type;
        this.ext = ext;
        this.isVault = isVault;
        this.vaultId = vaultId;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ NasItem(java.lang.String r15, java.lang.String r16, boolean r17, long r18, java.lang.String r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, boolean r24, java.lang.String r25, int r26, kotlin.jvm.internal.DefaultConstructorMarker r27) {
        /*
            r14 = this;
            r0 = r26
            r1 = r0 & 256(0x100, float:3.59E-43)
            if (r1 == 0) goto L9
            r1 = 0
            r12 = r1
            goto Lb
        L9:
            r12 = r24
        Lb:
            r0 = r0 & 512(0x200, float:7.17E-43)
            if (r0 == 0) goto L13
            java.lang.String r0 = ""
            r13 = r0
            goto L15
        L13:
            r13 = r25
        L15:
            r2 = r14
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r8 = r20
            r9 = r21
            r10 = r22
            r11 = r23
            r2.<init>(r3, r4, r5, r6, r8, r9, r10, r11, r12, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.NasItem.<init>(java.lang.String, java.lang.String, boolean, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getName() {
        return this.name;
    }

    public final String getPath() {
        return this.path;
    }

    public final boolean isFolder() {
        return this.isFolder;
    }

    public final long getSize() {
        return this.size;
    }

    public final String getFormattedSize() {
        return this.formattedSize;
    }

    public final String getFormattedDate() {
        return this.formattedDate;
    }

    public final String getType() {
        return this.type;
    }

    public final String getExt() {
        return this.ext;
    }

    public final boolean isVault() {
        return this.isVault;
    }

    public final String getVaultId() {
        return this.vaultId;
    }
}

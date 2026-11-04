package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\n\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000b\u001a\u00020\u00032\b\u0010\f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007¨\u0006\u0011"}, d2 = {"Lcom/nasdrive/app/VaultStatus;", "", "isConfigured", "", "isUnlocked", "<init>", "(ZZ)V", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class VaultStatus {
    private final boolean isConfigured;
    private final boolean isUnlocked;

    public static /* synthetic */ VaultStatus copy$default(VaultStatus vaultStatus, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = vaultStatus.isConfigured;
        }
        if ((i & 2) != 0) {
            z2 = vaultStatus.isUnlocked;
        }
        return vaultStatus.copy(z, z2);
    }

    /* renamed from: component1, reason: from getter */
    public final boolean getIsConfigured() {
        return this.isConfigured;
    }

    /* renamed from: component2, reason: from getter */
    public final boolean getIsUnlocked() {
        return this.isUnlocked;
    }

    public final VaultStatus copy(boolean isConfigured, boolean isUnlocked) {
        return new VaultStatus(isConfigured, isUnlocked);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VaultStatus)) {
            return false;
        }
        VaultStatus vaultStatus = (VaultStatus) other;
        return this.isConfigured == vaultStatus.isConfigured && this.isUnlocked == vaultStatus.isUnlocked;
    }

    public int hashCode() {
        return (Boolean.hashCode(this.isConfigured) * 31) + Boolean.hashCode(this.isUnlocked);
    }

    public String toString() {
        return "VaultStatus(isConfigured=" + this.isConfigured + ", isUnlocked=" + this.isUnlocked + ")";
    }

    public VaultStatus(boolean isConfigured, boolean isUnlocked) {
        this.isConfigured = isConfigured;
        this.isUnlocked = isUnlocked;
    }

    public final boolean isConfigured() {
        return this.isConfigured;
    }

    public final boolean isUnlocked() {
        return this.isUnlocked;
    }
}

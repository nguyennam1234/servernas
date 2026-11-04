package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasModels.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J#\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0006HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0015"}, d2 = {"Lcom/nasdrive/app/MoveResult;", "", "movedCount", "", "errors", "", "", "<init>", "(ILjava/util/List;)V", "getMovedCount", "()I", "getErrors", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final /* data */ class MoveResult {
    private final List<String> errors;
    private final int movedCount;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ MoveResult copy$default(MoveResult moveResult, int i, List list, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = moveResult.movedCount;
        }
        if ((i2 & 2) != 0) {
            list = moveResult.errors;
        }
        return moveResult.copy(i, list);
    }

    /* renamed from: component1, reason: from getter */
    public final int getMovedCount() {
        return this.movedCount;
    }

    public final List<String> component2() {
        return this.errors;
    }

    public final MoveResult copy(int movedCount, List<String> errors) {
        Intrinsics.checkNotNullParameter(errors, "errors");
        return new MoveResult(movedCount, errors);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MoveResult)) {
            return false;
        }
        MoveResult moveResult = (MoveResult) other;
        return this.movedCount == moveResult.movedCount && Intrinsics.areEqual(this.errors, moveResult.errors);
    }

    public int hashCode() {
        return (Integer.hashCode(this.movedCount) * 31) + this.errors.hashCode();
    }

    public String toString() {
        return "MoveResult(movedCount=" + this.movedCount + ", errors=" + this.errors + ")";
    }

    public MoveResult(int movedCount, List<String> errors) {
        Intrinsics.checkNotNullParameter(errors, "errors");
        this.movedCount = movedCount;
        this.errors = errors;
    }

    public final int getMovedCount() {
        return this.movedCount;
    }

    public final List<String> getErrors() {
        return this.errors;
    }
}

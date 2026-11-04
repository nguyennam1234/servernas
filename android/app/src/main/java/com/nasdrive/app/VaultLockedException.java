package com.nasdrive.app;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NasApiClient.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/nasdrive/app/VaultLockedException;", "Ljava/io/IOException;", "message", "", "<init>", "(Ljava/lang/String;)V", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class VaultLockedException extends IOException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VaultLockedException(String message) {
        super(message);
        Intrinsics.checkNotNullParameter(message, "message");
    }
}

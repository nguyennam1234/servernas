package com.nasdrive.app;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.C;
import androidx.media3.exoplayer.Renderer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: VideoPlayerActivity.kt */
@Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"com/nasdrive/app/VideoPlayerActivity$setupGestures$gestureDetector$1", "Landroid/view/GestureDetector$SimpleOnGestureListener;", "onSingleTapConfirmed", "", "e", "Landroid/view/MotionEvent;", "onDoubleTap", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class VideoPlayerActivity$setupGestures$gestureDetector$1 extends GestureDetector.SimpleOnGestureListener {
    final /* synthetic */ VideoPlayerActivity this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VideoPlayerActivity$setupGestures$gestureDetector$1(VideoPlayerActivity $receiver) {
        this.this$0 = $receiver;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent e) {
        boolean z;
        FloatingActionButton floatingActionButton;
        Handler handler;
        Intrinsics.checkNotNullParameter(e, "e");
        z = this.this$0.isScreenLocked;
        VideoPlayerActivity videoPlayerActivity = this.this$0;
        if (z) {
            floatingActionButton = videoPlayerActivity.btnFloatingUnlock;
            if (floatingActionButton == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
                floatingActionButton = null;
            }
            floatingActionButton.setVisibility(0);
            handler = this.this$0.handler;
            final VideoPlayerActivity videoPlayerActivity2 = this.this$0;
            handler.postDelayed(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$setupGestures$gestureDetector$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerActivity$setupGestures$gestureDetector$1.onSingleTapConfirmed$lambda$0(VideoPlayerActivity.this);
                }
            }, C.DEFAULT_MAX_SEEK_TO_PREVIOUS_POSITION_MS);
            return true;
        }
        videoPlayerActivity.toggleControls();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void onSingleTapConfirmed$lambda$0(VideoPlayerActivity this$0) {
        boolean z;
        FloatingActionButton floatingActionButton;
        z = this$0.isScreenLocked;
        if (z) {
            floatingActionButton = this$0.btnFloatingUnlock;
            if (floatingActionButton == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
                floatingActionButton = null;
            }
            floatingActionButton.setVisibility(8);
        }
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent e) {
        boolean z;
        View view;
        FloatingActionButton floatingActionButton;
        Intrinsics.checkNotNullParameter(e, "e");
        z = this.this$0.isScreenLocked;
        if (z) {
            return false;
        }
        view = this.this$0.gestureTouchView;
        FloatingActionButton floatingActionButton2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("gestureTouchView");
            view = null;
        }
        int width = view.getWidth();
        if (e.getX() < width * 0.4f) {
            this.this$0.seekRelative(-10000L);
            this.this$0.showDoubleTapLeftAnim();
            return true;
        }
        float x = e.getX();
        float f = width * 0.6f;
        VideoPlayerActivity videoPlayerActivity = this.this$0;
        if (x > f) {
            videoPlayerActivity.seekRelative(Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
            this.this$0.showDoubleTapRightAnim();
            return true;
        }
        floatingActionButton = videoPlayerActivity.btnPlayPauseCenter;
        if (floatingActionButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnPlayPauseCenter");
        } else {
            floatingActionButton2 = floatingActionButton;
        }
        floatingActionButton2.performClick();
        return true;
    }
}

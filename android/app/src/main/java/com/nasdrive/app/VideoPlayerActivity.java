package com.nasdrive.app;

import android.app.DownloadManager;
import android.app.PictureInPictureParams;
import android.app.PictureInPictureParams$Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Rational;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.ui.PlayerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* compiled from: VideoPlayerActivity.kt */
@Metadata(d1 = {"\u0000Å\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\t*\u0001M\b\u0007\u0018\u0000 r2\u00020\u0001:\u0001rB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010O\u001a\u00020P2\b\u0010Q\u001a\u0004\u0018\u00010RH\u0014J\b\u0010S\u001a\u00020PH\u0002J\b\u0010T\u001a\u00020PH\u0002J\b\u0010U\u001a\u00020PH\u0002J\b\u0010V\u001a\u00020PH\u0002J\b\u0010W\u001a\u00020PH\u0002J\b\u0010X\u001a\u00020PH\u0002J\b\u0010Y\u001a\u00020PH\u0002J\u0010\u0010Z\u001a\u00020P2\u0006\u0010[\u001a\u00020\\H\u0002J\b\u0010]\u001a\u00020PH\u0002J\b\u0010^\u001a\u00020PH\u0002J\b\u0010_\u001a\u00020PH\u0002J\b\u0010`\u001a\u00020PH\u0002J\b\u0010a\u001a\u00020PH\u0002J\b\u0010b\u001a\u00020PH\u0002J\b\u0010c\u001a\u00020PH\u0002J\b\u0010d\u001a\u00020PH\u0002J\b\u0010e\u001a\u00020PH\u0002J\b\u0010f\u001a\u00020PH\u0002J\u0018\u0010g\u001a\u00020P2\u0006\u0010h\u001a\u00020?2\u0006\u0010i\u001a\u00020jH\u0016J\b\u0010k\u001a\u00020PH\u0014J\b\u0010l\u001a\u00020PH\u0002J\b\u0010m\u001a\u00020PH\u0002J\u0010\u0010n\u001a\u0002042\u0006\u0010o\u001a\u00020\\H\u0002J\b\u0010p\u001a\u00020PH\u0014J\b\u0010q\u001a\u00020PH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u001aX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u000102X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u000204X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000204X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000204X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u000204X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020=X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020?X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020?X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020BX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020DX\u0082\u000e¢\u0006\u0002\n\u0000R \u0010E\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020D\u0012\u0004\u0012\u0002040G0FX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020IX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020KX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010L\u001a\u00020MX\u0082\u0004¢\u0006\u0004\n\u0002\u0010N¨\u0006s"}, d2 = {"Lcom/nasdrive/app/VideoPlayerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "playerView", "Landroidx/media3/ui/PlayerView;", "gestureTouchView", "Landroid/view/View;", "controlsOverlay", "topBar", "Landroid/widget/LinearLayout;", "bottomBar", "bufferingProgress", "Lcom/google/android/material/progressindicator/CircularProgressIndicator;", "txtVideoTitle", "Landroid/widget/TextView;", "txtServerStatus", "btnSpeed", "Lcom/google/android/material/button/MaterialButton;", "btnAspectRatio", "Landroid/widget/ImageButton;", "btnPip", "btnDownload", "btnShare", "btnBack", "btnPlayPauseCenter", "Lcom/google/android/material/floatingactionbutton/FloatingActionButton;", "btnRewind10", "btnForward10", "playerSeekBar", "Landroid/widget/SeekBar;", "txtCurrentTime", "txtTotalTime", "btnLockScreen", "btnFloatingUnlock", "btnOrientation", "txtAspectModeLabel", "hudBrightness", "pbBrightness", "Landroid/widget/ProgressBar;", "txtBrightnessValue", "hudVolume", "pbVolume", "txtVolumeValue", "hudSeek", "txtSeekDelta", "txtSeekTargetTime", "doubleTapLeft", "doubleTapRight", "exoPlayer", "Landroidx/media3/exoplayer/ExoPlayer;", "videoUrl", "", "videoTitle", "downloadUrl", "serverType", "audioManager", "Landroid/media/AudioManager;", "prefs", "Landroid/content/SharedPreferences;", "handler", "Landroid/os/Handler;", "isControlsVisible", "", "isScreenLocked", "currentSpeed", "", "currentAspectIndex", "", "aspectModes", "", "Lkotlin/Pair;", "speedOptions", "", "hideControlsRunnable", "Ljava/lang/Runnable;", "updateProgressRunnable", "com/nasdrive/app/VideoPlayerActivity$updateProgressRunnable$1", "Lcom/nasdrive/app/VideoPlayerActivity$updateProgressRunnable$1;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "hideSystemUI", "initViews", "setupListeners", "initPlayer", "setupGestures", "showDoubleTapLeftAnim", "showDoubleTapRightAnim", "seekRelative", "deltaMs", "", "toggleControls", "showControls", "hideControls", "resetControlsTimer", "showSpeedDialog", "cycleAspectRatio", "cycleOrientation", "lockScreen", "unlockScreen", "enterPipMode", "onPictureInPictureModeChanged", "isInPictureInPictureMode", "newConfig", "Landroid/content/res/Configuration;", "onUserLeaveHint", "downloadCurrentVideo", "shareVideoUrl", "formatTime", "millis", "onPause", "onDestroy", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class VideoPlayerActivity extends AppCompatActivity {
    private static final long CONTROLS_TIMEOUT_MS = 4000;
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String EXTRA_SERVER_TYPE = "extra_server_type";
    public static final String EXTRA_VIDEO_TITLE = "extra_video_title";
    public static final String EXTRA_VIDEO_URL = "extra_video_url";
    private static final String PREFS_NAME = "nas_video_resume";
    private AudioManager audioManager;
    private LinearLayout bottomBar;
    private ImageButton btnAspectRatio;
    private ImageButton btnBack;
    private ImageButton btnDownload;
    private FloatingActionButton btnFloatingUnlock;
    private ImageButton btnForward10;
    private ImageButton btnLockScreen;
    private ImageButton btnOrientation;
    private ImageButton btnPip;
    private FloatingActionButton btnPlayPauseCenter;
    private ImageButton btnRewind10;
    private ImageButton btnShare;
    private MaterialButton btnSpeed;
    private CircularProgressIndicator bufferingProgress;
    private View controlsOverlay;
    private int currentAspectIndex;
    private TextView doubleTapLeft;
    private TextView doubleTapRight;
    private ExoPlayer exoPlayer;
    private View gestureTouchView;
    private LinearLayout hudBrightness;
    private LinearLayout hudSeek;
    private LinearLayout hudVolume;
    private boolean isScreenLocked;
    private ProgressBar pbBrightness;
    private ProgressBar pbVolume;
    private SeekBar playerSeekBar;
    private PlayerView playerView;
    private SharedPreferences prefs;
    private LinearLayout topBar;
    private TextView txtAspectModeLabel;
    private TextView txtBrightnessValue;
    private TextView txtCurrentTime;
    private TextView txtSeekDelta;
    private TextView txtSeekTargetTime;
    private TextView txtServerStatus;
    private TextView txtTotalTime;
    private TextView txtVideoTitle;
    private TextView txtVolumeValue;
    private String videoUrl = "";
    private String videoTitle = "";
    private String downloadUrl = "";
    private String serverType = "";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isControlsVisible = true;
    private float currentSpeed = 1.0f;
    private final List<Pair<Integer, String>> aspectModes = CollectionsKt.listOf((Object[]) new Pair[]{new Pair(0, "Vừa khung hình"), new Pair(4, "Tràn viền (Cắt góc)"), new Pair(3, "Kéo dãn toàn màn hình"), new Pair(1, "Tỉ lệ 16:9")});
    private final float[] speedOptions = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f};
    private final Runnable hideControlsRunnable = new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda18
        @Override // java.lang.Runnable
        public final void run() {
            VideoPlayerActivity.hideControlsRunnable$lambda$0(VideoPlayerActivity.this);
        }
    };
    private final VideoPlayerActivity$updateProgressRunnable$1 updateProgressRunnable = new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$updateProgressRunnable$1
        @Override // java.lang.Runnable
        public void run() {
            ExoPlayer exoPlayer;
            Handler handler;
            SeekBar seekBar;
            SeekBar seekBar2;
            SeekBar seekBar3;
            TextView textView;
            String formatTime;
            TextView textView2;
            String formatTime2;
            exoPlayer = VideoPlayerActivity.this.exoPlayer;
            if (exoPlayer != null) {
                VideoPlayerActivity videoPlayerActivity = VideoPlayerActivity.this;
                if (exoPlayer.isPlaying()) {
                    long currentPosition = exoPlayer.getCurrentPosition();
                    long duration = exoPlayer.getDuration();
                    if (duration > 0) {
                        seekBar = videoPlayerActivity.playerSeekBar;
                        TextView textView3 = null;
                        if (seekBar == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
                            seekBar = null;
                        }
                        seekBar.setMax((int) duration);
                        seekBar2 = videoPlayerActivity.playerSeekBar;
                        if (seekBar2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
                            seekBar2 = null;
                        }
                        seekBar2.setProgress((int) currentPosition);
                        seekBar3 = videoPlayerActivity.playerSeekBar;
                        if (seekBar3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
                            seekBar3 = null;
                        }
                        seekBar3.setSecondaryProgress((int) exoPlayer.getBufferedPosition());
                        textView = videoPlayerActivity.txtCurrentTime;
                        if (textView == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("txtCurrentTime");
                            textView = null;
                        }
                        formatTime = videoPlayerActivity.formatTime(currentPosition);
                        textView.setText(formatTime);
                        textView2 = videoPlayerActivity.txtTotalTime;
                        if (textView2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("txtTotalTime");
                        } else {
                            textView3 = textView2;
                        }
                        formatTime2 = videoPlayerActivity.formatTime(duration);
                        textView3.setText(formatTime2);
                    }
                }
            }
            handler = VideoPlayerActivity.this.handler;
            handler.postDelayed(this, 500L);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void hideControlsRunnable$lambda$0(VideoPlayerActivity this$0) {
        if (this$0.isControlsVisible && !this$0.isScreenLocked) {
            this$0.hideControls();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_video_player);
        String stringExtra = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        if (stringExtra == null) {
            stringExtra = "";
        }
        this.videoUrl = stringExtra;
        String stringExtra2 = getIntent().getStringExtra(EXTRA_VIDEO_TITLE);
        if (stringExtra2 == null) {
            stringExtra2 = "NAS Video";
        }
        this.videoTitle = stringExtra2;
        String stringExtra3 = getIntent().getStringExtra(EXTRA_DOWNLOAD_URL);
        if (stringExtra3 == null) {
            stringExtra3 = this.videoUrl;
        }
        this.downloadUrl = stringExtra3;
        String stringExtra4 = getIntent().getStringExtra(EXTRA_SERVER_TYPE);
        if (stringExtra4 == null) {
            stringExtra4 = "LAN";
        }
        this.serverType = stringExtra4;
        if (this.videoUrl.length() == 0) {
            Toast.makeText(this, "Không có liên kết video hợp lệ", 0).show();
            finish();
            return;
        }
        Object systemService = getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.media.AudioManager");
        this.audioManager = (AudioManager) systemService;
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        this.prefs = sharedPreferences;
        initViews();
        setupListeners();
        setupGestures();
        initPlayer();
    }

    private final void hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(2);
        if (Build.VERSION.SDK_INT >= 28) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = 1;
        }
    }

    private final void initViews() {
        View findViewById = findViewById(R.id.playerView);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        this.playerView = (PlayerView) findViewById;
        View findViewById2 = findViewById(R.id.gestureTouchView);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
        this.gestureTouchView = findViewById2;
        View findViewById3 = findViewById(R.id.controlsOverlay);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
        this.controlsOverlay = findViewById3;
        View findViewById4 = findViewById(R.id.topBar);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
        this.topBar = (LinearLayout) findViewById4;
        View findViewById5 = findViewById(R.id.bottomBar);
        Intrinsics.checkNotNullExpressionValue(findViewById5, "findViewById(...)");
        this.bottomBar = (LinearLayout) findViewById5;
        View findViewById6 = findViewById(R.id.bufferingProgress);
        Intrinsics.checkNotNullExpressionValue(findViewById6, "findViewById(...)");
        this.bufferingProgress = (CircularProgressIndicator) findViewById6;
        View findViewById7 = findViewById(R.id.txtVideoTitle);
        Intrinsics.checkNotNullExpressionValue(findViewById7, "findViewById(...)");
        this.txtVideoTitle = (TextView) findViewById7;
        View findViewById8 = findViewById(R.id.txtServerStatus);
        Intrinsics.checkNotNullExpressionValue(findViewById8, "findViewById(...)");
        this.txtServerStatus = (TextView) findViewById8;
        View findViewById9 = findViewById(R.id.btnSpeed);
        Intrinsics.checkNotNullExpressionValue(findViewById9, "findViewById(...)");
        this.btnSpeed = (MaterialButton) findViewById9;
        View findViewById10 = findViewById(R.id.btnAspectRatio);
        Intrinsics.checkNotNullExpressionValue(findViewById10, "findViewById(...)");
        this.btnAspectRatio = (ImageButton) findViewById10;
        View findViewById11 = findViewById(R.id.btnPip);
        Intrinsics.checkNotNullExpressionValue(findViewById11, "findViewById(...)");
        this.btnPip = (ImageButton) findViewById11;
        View findViewById12 = findViewById(R.id.btnDownload);
        Intrinsics.checkNotNullExpressionValue(findViewById12, "findViewById(...)");
        this.btnDownload = (ImageButton) findViewById12;
        View findViewById13 = findViewById(R.id.btnShare);
        Intrinsics.checkNotNullExpressionValue(findViewById13, "findViewById(...)");
        this.btnShare = (ImageButton) findViewById13;
        View findViewById14 = findViewById(R.id.btnBack);
        Intrinsics.checkNotNullExpressionValue(findViewById14, "findViewById(...)");
        this.btnBack = (ImageButton) findViewById14;
        View findViewById15 = findViewById(R.id.btnPlayPauseCenter);
        Intrinsics.checkNotNullExpressionValue(findViewById15, "findViewById(...)");
        this.btnPlayPauseCenter = (FloatingActionButton) findViewById15;
        View findViewById16 = findViewById(R.id.btnRewind10);
        Intrinsics.checkNotNullExpressionValue(findViewById16, "findViewById(...)");
        this.btnRewind10 = (ImageButton) findViewById16;
        View findViewById17 = findViewById(R.id.btnForward10);
        Intrinsics.checkNotNullExpressionValue(findViewById17, "findViewById(...)");
        this.btnForward10 = (ImageButton) findViewById17;
        View findViewById18 = findViewById(R.id.playerSeekBar);
        Intrinsics.checkNotNullExpressionValue(findViewById18, "findViewById(...)");
        this.playerSeekBar = (SeekBar) findViewById18;
        View findViewById19 = findViewById(R.id.txtCurrentTime);
        Intrinsics.checkNotNullExpressionValue(findViewById19, "findViewById(...)");
        this.txtCurrentTime = (TextView) findViewById19;
        View findViewById20 = findViewById(R.id.txtTotalTime);
        Intrinsics.checkNotNullExpressionValue(findViewById20, "findViewById(...)");
        this.txtTotalTime = (TextView) findViewById20;
        View findViewById21 = findViewById(R.id.btnLockScreen);
        Intrinsics.checkNotNullExpressionValue(findViewById21, "findViewById(...)");
        this.btnLockScreen = (ImageButton) findViewById21;
        View findViewById22 = findViewById(R.id.btnFloatingUnlock);
        Intrinsics.checkNotNullExpressionValue(findViewById22, "findViewById(...)");
        this.btnFloatingUnlock = (FloatingActionButton) findViewById22;
        View findViewById23 = findViewById(R.id.btnOrientation);
        Intrinsics.checkNotNullExpressionValue(findViewById23, "findViewById(...)");
        this.btnOrientation = (ImageButton) findViewById23;
        View findViewById24 = findViewById(R.id.txtAspectModeLabel);
        Intrinsics.checkNotNullExpressionValue(findViewById24, "findViewById(...)");
        this.txtAspectModeLabel = (TextView) findViewById24;
        View findViewById25 = findViewById(R.id.hudBrightness);
        Intrinsics.checkNotNullExpressionValue(findViewById25, "findViewById(...)");
        this.hudBrightness = (LinearLayout) findViewById25;
        View findViewById26 = findViewById(R.id.pbBrightness);
        Intrinsics.checkNotNullExpressionValue(findViewById26, "findViewById(...)");
        this.pbBrightness = (ProgressBar) findViewById26;
        View findViewById27 = findViewById(R.id.txtBrightnessValue);
        Intrinsics.checkNotNullExpressionValue(findViewById27, "findViewById(...)");
        this.txtBrightnessValue = (TextView) findViewById27;
        View findViewById28 = findViewById(R.id.hudVolume);
        Intrinsics.checkNotNullExpressionValue(findViewById28, "findViewById(...)");
        this.hudVolume = (LinearLayout) findViewById28;
        View findViewById29 = findViewById(R.id.pbVolume);
        Intrinsics.checkNotNullExpressionValue(findViewById29, "findViewById(...)");
        this.pbVolume = (ProgressBar) findViewById29;
        View findViewById30 = findViewById(R.id.txtVolumeValue);
        Intrinsics.checkNotNullExpressionValue(findViewById30, "findViewById(...)");
        this.txtVolumeValue = (TextView) findViewById30;
        View findViewById31 = findViewById(R.id.hudSeek);
        Intrinsics.checkNotNullExpressionValue(findViewById31, "findViewById(...)");
        this.hudSeek = (LinearLayout) findViewById31;
        View findViewById32 = findViewById(R.id.txtSeekDelta);
        Intrinsics.checkNotNullExpressionValue(findViewById32, "findViewById(...)");
        this.txtSeekDelta = (TextView) findViewById32;
        View findViewById33 = findViewById(R.id.txtSeekTargetTime);
        Intrinsics.checkNotNullExpressionValue(findViewById33, "findViewById(...)");
        this.txtSeekTargetTime = (TextView) findViewById33;
        View findViewById34 = findViewById(R.id.doubleTapLeft);
        Intrinsics.checkNotNullExpressionValue(findViewById34, "findViewById(...)");
        this.doubleTapLeft = (TextView) findViewById34;
        View findViewById35 = findViewById(R.id.doubleTapRight);
        Intrinsics.checkNotNullExpressionValue(findViewById35, "findViewById(...)");
        this.doubleTapRight = (TextView) findViewById35;
        TextView textView = this.txtVideoTitle;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtVideoTitle");
            textView = null;
        }
        textView.setText(this.videoTitle);
        TextView textView3 = this.txtServerStatus;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtServerStatus");
            textView3 = null;
        }
        textView3.setText(StringsKt.contains((CharSequence) this.serverType, (CharSequence) "WAN", true) ? "🌐 Cloudflare CDN" : "⚡ LAN 1Gbps Tốc độ cao");
        TextView textView4 = this.txtAspectModeLabel;
        if (textView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtAspectModeLabel");
        } else {
            textView2 = textView4;
        }
        textView2.setText(this.aspectModes.get(0).getSecond());
        setRequestedOrientation(2);
    }

    private final void setupListeners() {
        ImageButton imageButton = this.btnBack;
        SeekBar seekBar = null;
        if (imageButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnBack");
            imageButton = null;
        }
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.this.finish();
            }
        });
        FloatingActionButton floatingActionButton = this.btnPlayPauseCenter;
        if (floatingActionButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnPlayPauseCenter");
            floatingActionButton = null;
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$3(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton2 = this.btnRewind10;
        if (imageButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnRewind10");
            imageButton2 = null;
        }
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$4(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton3 = this.btnForward10;
        if (imageButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnForward10");
            imageButton3 = null;
        }
        imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$5(VideoPlayerActivity.this, view);
            }
        });
        MaterialButton materialButton = this.btnSpeed;
        if (materialButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnSpeed");
            materialButton = null;
        }
        materialButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$6(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton4 = this.btnAspectRatio;
        if (imageButton4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnAspectRatio");
            imageButton4 = null;
        }
        imageButton4.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$7(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton5 = this.btnPip;
        if (imageButton5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnPip");
            imageButton5 = null;
        }
        imageButton5.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.this.enterPipMode();
            }
        });
        ImageButton imageButton6 = this.btnDownload;
        if (imageButton6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnDownload");
            imageButton6 = null;
        }
        imageButton6.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$9(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton7 = this.btnShare;
        if (imageButton7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnShare");
            imageButton7 = null;
        }
        imageButton7.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$10(VideoPlayerActivity.this, view);
            }
        });
        ImageButton imageButton8 = this.btnLockScreen;
        if (imageButton8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnLockScreen");
            imageButton8 = null;
        }
        imageButton8.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.this.lockScreen();
            }
        });
        FloatingActionButton floatingActionButton2 = this.btnFloatingUnlock;
        if (floatingActionButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
            floatingActionButton2 = null;
        }
        floatingActionButton2.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.this.unlockScreen();
            }
        });
        ImageButton imageButton9 = this.btnOrientation;
        if (imageButton9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnOrientation");
            imageButton9 = null;
        }
        imageButton9.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoPlayerActivity.setupListeners$lambda$13(VideoPlayerActivity.this, view);
            }
        });
        SeekBar seekBar2 = this.playerSeekBar;
        if (seekBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
        } else {
            seekBar = seekBar2;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.nasdrive.app.VideoPlayerActivity$setupListeners$13
            private boolean isUserTracking;

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar3, int progress, boolean fromUser) {
                TextView textView;
                String formatTime;
                if (fromUser) {
                    textView = VideoPlayerActivity.this.txtCurrentTime;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("txtCurrentTime");
                        textView = null;
                    }
                    formatTime = VideoPlayerActivity.this.formatTime(progress);
                    textView.setText(formatTime);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar3) {
                Handler handler;
                Runnable runnable;
                this.isUserTracking = true;
                handler = VideoPlayerActivity.this.handler;
                runnable = VideoPlayerActivity.this.hideControlsRunnable;
                handler.removeCallbacks(runnable);
            }

            /* JADX WARN: Code restructure failed: missing block: B:3:0x0005, code lost:
            
                r0 = r5.this$0.exoPlayer;
             */
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onStopTrackingTouch(android.widget.SeekBar r6) {
                /*
                    r5 = this;
                    r0 = 0
                    r5.isUserTracking = r0
                    if (r6 == 0) goto L17
                    com.nasdrive.app.VideoPlayerActivity r0 = com.nasdrive.app.VideoPlayerActivity.this
                    r1 = r6
                    r2 = 0
                    androidx.media3.exoplayer.ExoPlayer r0 = com.nasdrive.app.VideoPlayerActivity.access$getExoPlayer$p(r0)
                    if (r0 == 0) goto L17
                    int r3 = r1.getProgress()
                    long r3 = (long) r3
                    r0.seekTo(r3)
                L17:
                    com.nasdrive.app.VideoPlayerActivity r0 = com.nasdrive.app.VideoPlayerActivity.this
                    com.nasdrive.app.VideoPlayerActivity.access$resetControlsTimer(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.VideoPlayerActivity$setupListeners$13.onStopTrackingTouch(android.widget.SeekBar):void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$3(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        ExoPlayer exoPlayer = this$0.exoPlayer;
        if (exoPlayer != null) {
            if (exoPlayer.isPlaying()) {
                exoPlayer.pause();
            } else {
                exoPlayer.play();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$4(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.seekRelative(-10000L);
        this$0.showDoubleTapLeftAnim();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$5(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.seekRelative(Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
        this$0.showDoubleTapRightAnim();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$6(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.showSpeedDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$7(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.cycleAspectRatio();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$9(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.downloadCurrentVideo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$10(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.shareVideoUrl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$13(VideoPlayerActivity this$0, View it) {
        this$0.resetControlsTimer();
        this$0.cycleOrientation();
    }

    private final void initPlayer() {
        final ExoPlayer player = new ExoPlayer.Builder(this).build();
        Intrinsics.checkNotNullExpressionValue(player, "build(...)");
        this.exoPlayer = player;
        PlayerView playerView = this.playerView;
        SharedPreferences sharedPreferences = null;
        if (playerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playerView");
            playerView = null;
        }
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(this.videoUrl));
        Intrinsics.checkNotNullExpressionValue(mediaItem, "fromUri(...)");
        player.setMediaItem(mediaItem);
        SharedPreferences sharedPreferences2 = this.prefs;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("prefs");
        } else {
            sharedPreferences = sharedPreferences2;
        }
        long lastPos = sharedPreferences.getLong("pos_" + this.videoUrl.hashCode(), 0L);
        if (lastPos > 5000) {
            player.seekTo(lastPos);
            Toast.makeText(this, "Xem tiếp từ " + formatTime(lastPos), 0).show();
        }
        player.addListener(new Player.Listener() { // from class: com.nasdrive.app.VideoPlayerActivity$initPlayer$1
            @Override // androidx.media3.common.Player.Listener
            public void onPlaybackStateChanged(int playbackState) {
                CircularProgressIndicator circularProgressIndicator;
                CircularProgressIndicator circularProgressIndicator2;
                CircularProgressIndicator circularProgressIndicator3;
                SeekBar seekBar;
                TextView textView;
                String formatTime;
                CircularProgressIndicator circularProgressIndicator4;
                FloatingActionButton floatingActionButton;
                CircularProgressIndicator circularProgressIndicator5 = null;
                FloatingActionButton floatingActionButton2 = null;
                TextView textView2 = null;
                CircularProgressIndicator circularProgressIndicator6 = null;
                switch (playbackState) {
                    case 1:
                        circularProgressIndicator = VideoPlayerActivity.this.bufferingProgress;
                        if (circularProgressIndicator == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bufferingProgress");
                        } else {
                            circularProgressIndicator5 = circularProgressIndicator;
                        }
                        circularProgressIndicator5.setVisibility(8);
                        return;
                    case 2:
                        circularProgressIndicator2 = VideoPlayerActivity.this.bufferingProgress;
                        if (circularProgressIndicator2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bufferingProgress");
                        } else {
                            circularProgressIndicator6 = circularProgressIndicator2;
                        }
                        circularProgressIndicator6.setVisibility(0);
                        return;
                    case 3:
                        circularProgressIndicator3 = VideoPlayerActivity.this.bufferingProgress;
                        if (circularProgressIndicator3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bufferingProgress");
                            circularProgressIndicator3 = null;
                        }
                        circularProgressIndicator3.setVisibility(8);
                        long duration = player.getDuration();
                        if (duration > 0) {
                            seekBar = VideoPlayerActivity.this.playerSeekBar;
                            if (seekBar == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
                                seekBar = null;
                            }
                            seekBar.setMax((int) duration);
                            textView = VideoPlayerActivity.this.txtTotalTime;
                            if (textView == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("txtTotalTime");
                            } else {
                                textView2 = textView;
                            }
                            formatTime = VideoPlayerActivity.this.formatTime(duration);
                            textView2.setText(formatTime);
                            return;
                        }
                        return;
                    case 4:
                        circularProgressIndicator4 = VideoPlayerActivity.this.bufferingProgress;
                        if (circularProgressIndicator4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bufferingProgress");
                            circularProgressIndicator4 = null;
                        }
                        circularProgressIndicator4.setVisibility(8);
                        floatingActionButton = VideoPlayerActivity.this.btnPlayPauseCenter;
                        if (floatingActionButton == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("btnPlayPauseCenter");
                        } else {
                            floatingActionButton2 = floatingActionButton;
                        }
                        floatingActionButton2.setImageResource(R.drawable.ic_play_arrow);
                        VideoPlayerActivity.this.showControls();
                        return;
                    default:
                        return;
                }
            }

            @Override // androidx.media3.common.Player.Listener
            public void onIsPlayingChanged(boolean isPlaying) {
                FloatingActionButton floatingActionButton;
                FloatingActionButton floatingActionButton2;
                VideoPlayerActivity videoPlayerActivity = VideoPlayerActivity.this;
                FloatingActionButton floatingActionButton3 = null;
                if (isPlaying) {
                    floatingActionButton2 = videoPlayerActivity.btnPlayPauseCenter;
                    if (floatingActionButton2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("btnPlayPauseCenter");
                    } else {
                        floatingActionButton3 = floatingActionButton2;
                    }
                    floatingActionButton3.setImageResource(R.drawable.ic_pause);
                    VideoPlayerActivity.this.resetControlsTimer();
                    return;
                }
                floatingActionButton = videoPlayerActivity.btnPlayPauseCenter;
                if (floatingActionButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnPlayPauseCenter");
                } else {
                    floatingActionButton3 = floatingActionButton;
                }
                floatingActionButton3.setImageResource(R.drawable.ic_play_arrow);
                VideoPlayerActivity.this.showControls();
            }
        });
        player.prepare();
        player.setPlayWhenReady(true);
        this.handler.post(this.updateProgressRunnable);
        resetControlsTimer();
    }

    private final void setupGestures() {
        final GestureDetector gestureDetector = new GestureDetector(this, new VideoPlayerActivity$setupGestures$gestureDetector$1(this));
        final Ref.FloatRef initialTouchX = new Ref.FloatRef();
        final Ref.FloatRef initialTouchY = new Ref.FloatRef();
        final Ref.BooleanRef isDragging = new Ref.BooleanRef();
        final Ref.BooleanRef isVerticalDrag = new Ref.BooleanRef();
        final Ref.BooleanRef isHorizontalSeek = new Ref.BooleanRef();
        final Ref.BooleanRef isLeftHalf = new Ref.BooleanRef();
        final Ref.IntRef initialVolume = new Ref.IntRef();
        final Ref.FloatRef initialBrightness = new Ref.FloatRef();
        initialBrightness.element = 0.5f;
        final Ref.LongRef initialPosition = new Ref.LongRef();
        final Ref.LongRef targetSeekPosition = new Ref.LongRef();
        View view = this.gestureTouchView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("gestureTouchView");
            view = null;
        }
        view.setOnTouchListener(new View.OnTouchListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda21
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return VideoPlayerActivity.setupGestures$lambda$15(VideoPlayerActivity.this, gestureDetector, initialTouchX, initialTouchY, isDragging, isVerticalDrag, isHorizontalSeek, isLeftHalf, initialVolume, initialBrightness, initialPosition, targetSeekPosition, view2, motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean setupGestures$lambda$15(final VideoPlayerActivity this$0, GestureDetector $gestureDetector, Ref.FloatRef $initialTouchX, Ref.FloatRef $initialTouchY, Ref.BooleanRef $isDragging, Ref.BooleanRef $isVerticalDrag, Ref.BooleanRef $isHorizontalSeek, Ref.BooleanRef $isLeftHalf, Ref.IntRef $initialVolume, Ref.FloatRef $initialBrightness, Ref.LongRef $initialPosition, Ref.LongRef $targetSeekPosition, View view, MotionEvent event) {
        ExoPlayer exoPlayer;
        float viewWidth;
        ExoPlayer player;
        if (this$0.isScreenLocked) {
            $gestureDetector.onTouchEvent(event);
            return true;
        }
        if ($gestureDetector.onTouchEvent(event)) {
            return true;
        }
        View view2 = this$0.gestureTouchView;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("gestureTouchView");
            view2 = null;
        }
        float viewWidth2 = view2.getWidth();
        View view3 = this$0.gestureTouchView;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("gestureTouchView");
            view3 = null;
        }
        float viewHeight = view3.getHeight();
        switch (event.getAction()) {
            case 0:
                $initialTouchX.element = event.getX();
                $initialTouchY.element = event.getY();
                $isDragging.element = false;
                $isVerticalDrag.element = false;
                $isHorizontalSeek.element = false;
                $isLeftHalf.element = event.getX() < viewWidth2 * 0.5f;
                AudioManager audioManager = this$0.audioManager;
                if (audioManager == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("audioManager");
                    audioManager = null;
                }
                $initialVolume.element = audioManager.getStreamVolume(3);
                $initialBrightness.element = this$0.getWindow().getAttributes().screenBrightness;
                if ($initialBrightness.element < 0.0f) {
                    $initialBrightness.element = 0.5f;
                }
                ExoPlayer exoPlayer2 = this$0.exoPlayer;
                $initialPosition.element = exoPlayer2 != null ? exoPlayer2.getCurrentPosition() : 0L;
                $targetSeekPosition.element = $initialPosition.element;
                return true;
            case 1:
            case 3:
                if ($isDragging.element) {
                    if ($isHorizontalSeek.element && this$0.exoPlayer != null && (exoPlayer = this$0.exoPlayer) != null) {
                        exoPlayer.seekTo($targetSeekPosition.element);
                    }
                    this$0.handler.postDelayed(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            VideoPlayerActivity.setupGestures$lambda$15$lambda$14(VideoPlayerActivity.this);
                        }
                    }, 400L);
                    this$0.resetControlsTimer();
                    return true;
                }
                return true;
            case 2:
                float deltaX = event.getX() - $initialTouchX.element;
                float deltaY = event.getY() - $initialTouchY.element;
                if ($isDragging.element) {
                    viewWidth = viewWidth2;
                } else if (Math.abs(deltaX) > 25.0f || Math.abs(deltaY) > 25.0f) {
                    $isDragging.element = true;
                    viewWidth = viewWidth2;
                    this$0.handler.removeCallbacks(this$0.hideControlsRunnable);
                    if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        $isVerticalDrag.element = true;
                    } else {
                        $isHorizontalSeek.element = true;
                    }
                } else {
                    viewWidth = viewWidth2;
                }
                if ($isDragging.element) {
                    if (!$isVerticalDrag.element) {
                        if (!$isHorizontalSeek.element || (player = this$0.exoPlayer) == null) {
                            return true;
                        }
                        long duration = player.getDuration();
                        if (duration > 0) {
                            long seekDeltaMs = (deltaX / viewWidth) * 90000.0f;
                            $targetSeekPosition.element = RangesKt.coerceIn($initialPosition.element + seekDeltaMs, 0L, duration);
                            int deltaSec = (int) (seekDeltaMs / 1000);
                            String sign = deltaSec >= 0 ? "+" : "-";
                            TextView textView = this$0.txtSeekDelta;
                            if (textView == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("txtSeekDelta");
                                textView = null;
                            }
                            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                            String format = String.format(Locale.getDefault(), "%s%02d:%02d", Arrays.copyOf(new Object[]{sign, Integer.valueOf(Math.abs(deltaSec) / 60), Integer.valueOf(Math.abs(deltaSec) % 60)}, 3));
                            Intrinsics.checkNotNullExpressionValue(format, "format(...)");
                            textView.setText(format);
                            TextView textView2 = this$0.txtSeekTargetTime;
                            if (textView2 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("txtSeekTargetTime");
                                textView2 = null;
                            }
                            textView2.setText(this$0.formatTime($targetSeekPosition.element) + " / " + this$0.formatTime(duration));
                            LinearLayout linearLayout = this$0.hudSeek;
                            if (linearLayout == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("hudSeek");
                                linearLayout = null;
                            }
                            linearLayout.setVisibility(0);
                            LinearLayout linearLayout2 = this$0.hudBrightness;
                            if (linearLayout2 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("hudBrightness");
                                linearLayout2 = null;
                            }
                            linearLayout2.setVisibility(8);
                            LinearLayout linearLayout3 = this$0.hudVolume;
                            if (linearLayout3 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("hudVolume");
                                linearLayout3 = null;
                            }
                            linearLayout3.setVisibility(8);
                            return true;
                        }
                        return true;
                    }
                    float percentDelta = (-deltaY) / viewHeight;
                    if (!$isLeftHalf.element) {
                        AudioManager audioManager2 = this$0.audioManager;
                        if (audioManager2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("audioManager");
                            audioManager2 = null;
                        }
                        int maxVol = audioManager2.getStreamMaxVolume(3);
                        int volChange = (int) (maxVol * percentDelta);
                        int targetVol = RangesKt.coerceIn($initialVolume.element + volChange, 0, maxVol);
                        AudioManager audioManager3 = this$0.audioManager;
                        if (audioManager3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("audioManager");
                            audioManager3 = null;
                        }
                        audioManager3.setStreamVolume(3, targetVol, 0);
                        int volPercent = (int) ((targetVol / maxVol) * 100.0f);
                        ProgressBar progressBar = this$0.pbVolume;
                        if (progressBar == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("pbVolume");
                            progressBar = null;
                        }
                        progressBar.setProgress(volPercent);
                        TextView textView3 = this$0.txtVolumeValue;
                        if (textView3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("txtVolumeValue");
                            textView3 = null;
                        }
                        textView3.setText(volPercent + "%");
                        LinearLayout linearLayout4 = this$0.hudVolume;
                        if (linearLayout4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("hudVolume");
                            linearLayout4 = null;
                        }
                        linearLayout4.setVisibility(0);
                        LinearLayout linearLayout5 = this$0.hudBrightness;
                        if (linearLayout5 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("hudBrightness");
                            linearLayout5 = null;
                        }
                        linearLayout5.setVisibility(8);
                        LinearLayout linearLayout6 = this$0.hudSeek;
                        if (linearLayout6 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("hudSeek");
                            linearLayout6 = null;
                        }
                        linearLayout6.setVisibility(8);
                        return true;
                    }
                    float newBrightness = RangesKt.coerceIn($initialBrightness.element + percentDelta, 0.01f, 1.0f);
                    WindowManager.LayoutParams lp = this$0.getWindow().getAttributes();
                    lp.screenBrightness = newBrightness;
                    this$0.getWindow().setAttributes(lp);
                    int bPercent = (int) (newBrightness * 100.0f);
                    ProgressBar progressBar2 = this$0.pbBrightness;
                    if (progressBar2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("pbBrightness");
                        progressBar2 = null;
                    }
                    progressBar2.setProgress(bPercent);
                    TextView textView4 = this$0.txtBrightnessValue;
                    if (textView4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("txtBrightnessValue");
                        textView4 = null;
                    }
                    textView4.setText(bPercent + "%");
                    LinearLayout linearLayout7 = this$0.hudBrightness;
                    if (linearLayout7 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("hudBrightness");
                        linearLayout7 = null;
                    }
                    linearLayout7.setVisibility(0);
                    LinearLayout linearLayout8 = this$0.hudVolume;
                    if (linearLayout8 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("hudVolume");
                        linearLayout8 = null;
                    }
                    linearLayout8.setVisibility(8);
                    LinearLayout linearLayout9 = this$0.hudSeek;
                    if (linearLayout9 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("hudSeek");
                        linearLayout9 = null;
                    }
                    linearLayout9.setVisibility(8);
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupGestures$lambda$15$lambda$14(VideoPlayerActivity this$0) {
        LinearLayout linearLayout = this$0.hudBrightness;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hudBrightness");
            linearLayout = null;
        }
        linearLayout.setVisibility(8);
        LinearLayout linearLayout3 = this$0.hudVolume;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hudVolume");
            linearLayout3 = null;
        }
        linearLayout3.setVisibility(8);
        LinearLayout linearLayout4 = this$0.hudSeek;
        if (linearLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hudSeek");
        } else {
            linearLayout2 = linearLayout4;
        }
        linearLayout2.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showDoubleTapLeftAnim() {
        TextView textView = this.doubleTapLeft;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapLeft");
            textView = null;
        }
        textView.setVisibility(0);
        TextView textView3 = this.doubleTapLeft;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapLeft");
        } else {
            textView2 = textView3;
        }
        textView2.animate().alpha(1.0f).setDuration(150L).withEndAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.showDoubleTapLeftAnim$lambda$17(VideoPlayerActivity.this);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showDoubleTapLeftAnim$lambda$17(final VideoPlayerActivity this$0) {
        TextView textView = this$0.doubleTapLeft;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapLeft");
            textView = null;
        }
        textView.animate().alpha(0.0f).setDuration(300L).withEndAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.showDoubleTapLeftAnim$lambda$17$lambda$16(VideoPlayerActivity.this);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showDoubleTapLeftAnim$lambda$17$lambda$16(VideoPlayerActivity this$0) {
        TextView textView = this$0.doubleTapLeft;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapLeft");
            textView = null;
        }
        textView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showDoubleTapRightAnim() {
        TextView textView = this.doubleTapRight;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapRight");
            textView = null;
        }
        textView.setVisibility(0);
        TextView textView3 = this.doubleTapRight;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapRight");
        } else {
            textView2 = textView3;
        }
        textView2.animate().alpha(1.0f).setDuration(150L).withEndAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.showDoubleTapRightAnim$lambda$19(VideoPlayerActivity.this);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showDoubleTapRightAnim$lambda$19(final VideoPlayerActivity this$0) {
        TextView textView = this$0.doubleTapRight;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapRight");
            textView = null;
        }
        textView.animate().alpha(0.0f).setDuration(300L).withEndAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.showDoubleTapRightAnim$lambda$19$lambda$18(VideoPlayerActivity.this);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showDoubleTapRightAnim$lambda$19$lambda$18(VideoPlayerActivity this$0) {
        TextView textView = this$0.doubleTapRight;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doubleTapRight");
            textView = null;
        }
        textView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void seekRelative(long deltaMs) {
        ExoPlayer exoPlayer = this.exoPlayer;
        if (exoPlayer != null) {
            long coerceIn = RangesKt.coerceIn(exoPlayer.getCurrentPosition() + deltaMs, 0L, RangesKt.coerceAtLeast(exoPlayer.getDuration(), 0L));
            exoPlayer.seekTo(coerceIn);
            SeekBar seekBar = this.playerSeekBar;
            TextView textView = null;
            if (seekBar == null) {
                Intrinsics.throwUninitializedPropertyAccessException("playerSeekBar");
                seekBar = null;
            }
            seekBar.setProgress((int) coerceIn);
            TextView textView2 = this.txtCurrentTime;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtCurrentTime");
            } else {
                textView = textView2;
            }
            textView.setText(formatTime(coerceIn));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void toggleControls() {
        if (this.isControlsVisible) {
            hideControls();
        } else {
            showControls();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showControls() {
        this.isControlsVisible = true;
        View view = this.controlsOverlay;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlsOverlay");
            view = null;
        }
        view.animate().alpha(1.0f).setDuration(200L).withStartAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.showControls$lambda$21(VideoPlayerActivity.this);
            }
        }).start();
        resetControlsTimer();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showControls$lambda$21(VideoPlayerActivity this$0) {
        View view = this$0.controlsOverlay;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlsOverlay");
            view = null;
        }
        view.setVisibility(0);
    }

    private final void hideControls() {
        this.isControlsVisible = false;
        View view = this.controlsOverlay;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlsOverlay");
            view = null;
        }
        view.animate().alpha(0.0f).setDuration(250L).withEndAction(new Runnable() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerActivity.hideControls$lambda$22(VideoPlayerActivity.this);
            }
        }).start();
        hideSystemUI();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void hideControls$lambda$22(VideoPlayerActivity this$0) {
        View view = this$0.controlsOverlay;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlsOverlay");
            view = null;
        }
        view.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetControlsTimer() {
        this.handler.removeCallbacks(this.hideControlsRunnable);
        if (this.isControlsVisible) {
            ExoPlayer exoPlayer = this.exoPlayer;
            boolean z = false;
            if (exoPlayer != null && exoPlayer.isPlaying()) {
                z = true;
            }
            if (z) {
                this.handler.postDelayed(this.hideControlsRunnable, CONTROLS_TIMEOUT_MS);
            }
        }
    }

    private final void showSpeedDialog() {
        float[] fArr = this.speedOptions;
        Collection arrayList = new ArrayList(fArr.length);
        for (float f : fArr) {
            arrayList.add(f + "x");
        }
        String[] speedNames = (String[]) ((List) arrayList).toArray(new String[0]);
        float[] fArr2 = this.speedOptions;
        int currentIndex = 0;
        int length = fArr2.length;
        while (true) {
            if (currentIndex < length) {
                if (fArr2[currentIndex] == this.currentSpeed) {
                    break;
                } else {
                    currentIndex++;
                }
            } else {
                currentIndex = -1;
                break;
            }
        }
        if (currentIndex < 0) {
            currentIndex = 2;
        }
        new AlertDialog.Builder(this).setTitle("Tốc độ phát").setSingleChoiceItems(speedNames, currentIndex, new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.VideoPlayerActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                VideoPlayerActivity.showSpeedDialog$lambda$26(VideoPlayerActivity.this, dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showSpeedDialog$lambda$26(VideoPlayerActivity this$0, DialogInterface dialog, int which) {
        this$0.currentSpeed = this$0.speedOptions[which];
        ExoPlayer exoPlayer = this$0.exoPlayer;
        if (exoPlayer != null) {
            exoPlayer.setPlaybackParameters(new PlaybackParameters(this$0.currentSpeed));
        }
        MaterialButton materialButton = this$0.btnSpeed;
        if (materialButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnSpeed");
            materialButton = null;
        }
        materialButton.setText(this$0.currentSpeed + "x");
        dialog.dismiss();
    }

    private final void cycleAspectRatio() {
        this.currentAspectIndex = (this.currentAspectIndex + 1) % this.aspectModes.size();
        Pair mode = this.aspectModes.get(this.currentAspectIndex);
        PlayerView playerView = this.playerView;
        TextView textView = null;
        if (playerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playerView");
            playerView = null;
        }
        playerView.setResizeMode(mode.getFirst().intValue());
        TextView textView2 = this.txtAspectModeLabel;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtAspectModeLabel");
        } else {
            textView = textView2;
        }
        textView.setText(mode.getSecond());
        Toast.makeText(this, mode.getSecond(), 0).show();
    }

    private final void cycleOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (getRequestedOrientation() == 2 || getRequestedOrientation() == -1) {
            if (currentOrientation == 1) {
                setRequestedOrientation(6);
                Toast.makeText(this, "Khóa xoay ngang", 0).show();
                return;
            } else {
                setRequestedOrientation(1);
                Toast.makeText(this, "Khóa xoay dọc", 0).show();
                return;
            }
        }
        setRequestedOrientation(2);
        Toast.makeText(this, "Tự động xoay theo cảm biến máy", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void lockScreen() {
        this.isScreenLocked = true;
        hideControls();
        FloatingActionButton floatingActionButton = this.btnFloatingUnlock;
        if (floatingActionButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
            floatingActionButton = null;
        }
        floatingActionButton.setVisibility(0);
        Toast.makeText(this, "Đã khóa màn hình. Bấm biểu tượng khóa để mở.", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void unlockScreen() {
        this.isScreenLocked = false;
        FloatingActionButton floatingActionButton = this.btnFloatingUnlock;
        if (floatingActionButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
            floatingActionButton = null;
        }
        floatingActionButton.setVisibility(8);
        showControls();
        Toast.makeText(this, "Đã mở khóa màn hình", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void enterPipMode() {
        Rational aspectRational;
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 26) {
            ExoPlayer exoPlayer = this.exoPlayer;
            Format videoFormat = exoPlayer != null ? exoPlayer.getVideoFormat() : null;
            if (videoFormat != null && videoFormat.width > 0 && videoFormat.height > 0) {
                int w = RangesKt.coerceIn(videoFormat.width, 1, 10000);
                int h = RangesKt.coerceIn(videoFormat.height, 1, 10000);
                float ratio = w / h;
                if (0.42f <= ratio && ratio <= 2.38f) {
                    z = true;
                }
                aspectRational = z ? new Rational(w, h) : new Rational(16, 9);
            } else {
                aspectRational = new Rational(16, 9);
            }
            PictureInPictureParams params = new PictureInPictureParams$Builder().setAspectRatio(aspectRational).build();
            enterPictureInPictureMode(params);
            return;
        }
        Toast.makeText(this, "Tính năng Thu nhỏ (PiP) cần Android 8.0 trở lên", 0).show();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            View view = this.controlsOverlay;
            FloatingActionButton floatingActionButton = null;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("controlsOverlay");
                view = null;
            }
            view.setVisibility(8);
            FloatingActionButton floatingActionButton2 = this.btnFloatingUnlock;
            if (floatingActionButton2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btnFloatingUnlock");
            } else {
                floatingActionButton = floatingActionButton2;
            }
            floatingActionButton.setVisibility(8);
            return;
        }
        showControls();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        ExoPlayer exoPlayer = this.exoPlayer;
        boolean z = false;
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            z = true;
        }
        if (z && Build.VERSION.SDK_INT >= 26) {
            enterPipMode();
        }
    }

    private final void downloadCurrentVideo() {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.downloadUrl));
            request.setTitle(this.videoTitle);
            request.setDescription("Đang tải video từ máy chủ NAS");
            request.setNotificationVisibility(1);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, this.videoTitle);
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            Object systemService = getSystemService("download");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.DownloadManager");
            DownloadManager dm = (DownloadManager) systemService;
            dm.enqueue(request);
            Toast.makeText(this, "Bắt đầu tải video vào thư mục Download", 0).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải video: " + e.getMessage(), 0).show();
        }
    }

    private final void shareVideoUrl() {
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("text/plain");
        shareIntent.putExtra("android.intent.extra.SUBJECT", this.videoTitle);
        shareIntent.putExtra("android.intent.extra.TEXT", "Xem video từ NAS: " + this.videoTitle + "\n" + this.videoUrl);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ video qua"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String formatTime(long millis) {
        int totalSec = (int) (millis / 1000);
        int hours = totalSec / 3600;
        int minutes = (totalSec % 3600) / 60;
        int seconds = totalSec % 60;
        if (hours > 0) {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(Locale.getDefault(), "%02d:%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}, 3));
            Intrinsics.checkNotNullExpressionValue(format, "format(...)");
            return format;
        }
        StringCompanionObject stringCompanionObject2 = StringCompanionObject.INSTANCE;
        String format2 = String.format(Locale.getDefault(), "%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}, 2));
        Intrinsics.checkNotNullExpressionValue(format2, "format(...)");
        return format2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        ExoPlayer exoPlayer = this.exoPlayer;
        if (exoPlayer != null) {
            if (exoPlayer.isPlaying()) {
                exoPlayer.pause();
            }
            SharedPreferences sharedPreferences = this.prefs;
            if (sharedPreferences == null) {
                Intrinsics.throwUninitializedPropertyAccessException("prefs");
                sharedPreferences = null;
            }
            sharedPreferences.edit().putLong("pos_" + this.videoUrl.hashCode(), exoPlayer.getCurrentPosition()).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacksAndMessages(null);
        ExoPlayer exoPlayer = this.exoPlayer;
        if (exoPlayer != null) {
            SharedPreferences sharedPreferences = this.prefs;
            if (sharedPreferences == null) {
                Intrinsics.throwUninitializedPropertyAccessException("prefs");
                sharedPreferences = null;
            }
            sharedPreferences.edit().putLong("pos_" + this.videoUrl.hashCode(), exoPlayer.getCurrentPosition()).apply();
            exoPlayer.release();
        }
        this.exoPlayer = null;
    }
}

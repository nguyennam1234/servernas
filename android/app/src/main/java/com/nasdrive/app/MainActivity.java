package com.nasdrive.app;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.extractor.text.ttml.TtmlNode;
import androidx.media3.extractor.ts.TsExtractor;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import coil.Coil;
import coil.ImageLoader;
import coil.disk.DiskCache;
import coil.memory.MemoryCache;
import coil.request.ErrorResult;
import coil.request.ImageRequest;
import coil.request.SuccessResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.Response;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000Ù\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u001a*\u00019\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010K\u001a\u00020L2\b\u0010M\u001a\u0004\u0018\u00010NH\u0014J\u0010\u0010O\u001a\u00020L2\u0006\u0010P\u001a\u00020QH\u0014J\b\u0010R\u001a\u00020LH\u0002J\b\u0010S\u001a\u00020LH\u0002J\b\u0010T\u001a\u00020LH\u0002J\b\u0010U\u001a\u00020LH\u0002J\b\u0010V\u001a\u00020LH\u0002J\b\u0010W\u001a\u00020LH\u0002J\b\u0010X\u001a\u00020LH\u0002J\b\u0010Y\u001a\u00020LH\u0002J\u0018\u0010Z\u001a\u00020L2\u0006\u0010[\u001a\u0002012\u0006\u0010\\\u001a\u000205H\u0002J0\u0010]\u001a\u00020+2\u0006\u0010^\u001a\u00020+2\u0006\u0010_\u001a\u00020+2\u0006\u0010`\u001a\u00020+2\u0006\u0010a\u001a\u00020+2\u0006\u0010b\u001a\u00020+H\u0002J\u001c\u0010c\u001a\u00020L2\b\b\u0002\u0010d\u001a\u0002012\b\b\u0002\u0010e\u001a\u000201H\u0002J\u0016\u0010f\u001a\u00020L2\f\u0010g\u001a\b\u0012\u0004\u0012\u00020i0hH\u0002J\b\u0010j\u001a\u00020LH\u0002J\b\u0010k\u001a\u00020LH\u0002J\"\u0010l\u001a\u00020L2\u0006\u0010m\u001a\u00020+2\u0006\u0010n\u001a\u00020+2\b\b\u0002\u0010o\u001a\u000201H\u0002J\u0010\u0010p\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0010\u0010r\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0018\u0010s\u001a\u00020L2\u0006\u0010q\u001a\u00020i2\u0006\u0010t\u001a\u00020\u0005H\u0002J\u0010\u0010u\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0010\u0010v\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0010\u0010w\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0016\u0010x\u001a\u00020L2\f\u0010y\u001a\b\u0012\u0004\u0012\u00020+0hH\u0002J\u0016\u0010z\u001a\u00020L2\f\u0010y\u001a\b\u0012\u0004\u0012\u00020+0hH\u0002J\u0010\u0010{\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0010\u0010|\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0016\u0010}\u001a\u00020L2\f\u0010~\u001a\b\u0012\u0004\u0012\u00020L0\u007fH\u0002J\u0017\u0010\u0080\u0001\u001a\u00020L2\f\u0010~\u001a\b\u0012\u0004\u0012\u00020L0\u007fH\u0002J\t\u0010\u0081\u0001\u001a\u00020LH\u0002J\u0017\u0010\u0082\u0001\u001a\u00020L2\f\u0010~\u001a\b\u0012\u0004\u0012\u00020L0\u007fH\u0002J\t\u0010\u0083\u0001\u001a\u00020LH\u0002J\u0011\u0010\u0084\u0001\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0011\u0010\u0085\u0001\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\u0011\u0010\u0086\u0001\u001a\u00020L2\u0006\u0010q\u001a\u00020iH\u0002J\t\u0010\u0087\u0001\u001a\u00020LH\u0002J\t\u0010\u0088\u0001\u001a\u00020LH\u0002J\t\u0010\u0089\u0001\u001a\u00020LH\u0002J\u0018\u0010\u008a\u0001\u001a\u00020L2\r\u0010\u008b\u0001\u001a\b\u0012\u0004\u0012\u0002070hH\u0002J\u0012\u0010\u008c\u0001\u001a\u00020+2\u0007\u0010\u008d\u0001\u001a\u000207H\u0002J\u0011\u0010\u008e\u0001\u001a\u00020L2\u0006\u0010P\u001a\u00020QH\u0002J\t\u0010\u008f\u0001\u001a\u00020LH\u0002J\t\u0010\u0090\u0001\u001a\u00020LH\u0002J\t\u0010\u0091\u0001\u001a\u00020LH\u0002J\t\u0010\u0092\u0001\u001a\u00020LH\u0002J\t\u0010\u0093\u0001\u001a\u00020LH\u0002J\u0012\u0010\u0094\u0001\u001a\u0002012\u0007\u0010\u0095\u0001\u001a\u00020+H\u0002J\u0012\u0010\u0096\u0001\u001a\u00020+2\u0007\u0010\u0097\u0001\u001a\u000205H\u0002J\t\u0010\u0098\u0001\u001a\u00020LH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u000fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000201X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u000201X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000205X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00106\u001a\u0004\u0018\u000107X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00108\u001a\u000209X\u0082\u0004¢\u0006\u0004\n\u0002\u0010:R\u0010\u0010;\u001a\u0004\u0018\u00010<X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u000205X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020?X\u0082.¢\u0006\u0002\n\u0000R\u0016\u0010@\u001a\n B*\u0004\u0018\u00010A0AX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020DX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010E\u001a\b\u0012\u0004\u0012\u0002070FX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010G\u001a\b\u0012\u0004\u0012\u00020+0FX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0I0FX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010J\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0I0FX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0099\u0001"}, d2 = {"Lcom/nasdrive/app/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "btnHeaderAvatar", "Landroid/view/View;", "btnQuickScan", "edtSearch", "Landroid/widget/EditText;", "btnClearSearch", "Landroid/widget/ImageButton;", "chipServerBadge", "Landroid/widget/LinearLayout;", "serverStatusDot", "txtServerBadge", "Landroid/widget/TextView;", "btnToggleViewMode", "btnSort", "txtStorageLabel", "txtStoragePercent", "storageProgressBar", "Lcom/google/android/material/progressindicator/LinearProgressIndicator;", "breadcrumbsContainer", "pageProgressBar", "swipeRefreshLayout", "Landroidx/swiperefreshlayout/widget/SwipeRefreshLayout;", "rvFiles", "Landroidx/recyclerview/widget/RecyclerView;", "emptyStateView", "txtEmptyTitle", "txtEmptySubtitle", "btnEmptyRetry", "Lcom/google/android/material/button/MaterialButton;", "btnFabMain", "Lcom/google/android/material/floatingactionbutton/FloatingActionButton;", "uploadProgressCard", "txtUploadFileName", "txtUploadSpeed", "pbUploadProgress", "Landroid/widget/ProgressBar;", "bottomNavigationView", "Lcom/google/android/material/bottomnavigation/BottomNavigationView;", "currentFolder", "", "currentSearch", "currentFilter", "currentSort", "currentOrder", "isGridMode", "", "isCurrentLan", "isVaultMode", "backPressedTime", "", "cameraTempUri", "Landroid/net/Uri;", "folderCache", "com/nasdrive/app/MainActivity$folderCache$1", "Lcom/nasdrive/app/MainActivity$folderCache$1;", "currentFilesCall", "Lokhttp3/Call;", "activeRequestId", "filesAdapter", "Lcom/nasdrive/app/NasFilesAdapter;", "backgroundExecutor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "mainHandler", "Landroid/os/Handler;", "cameraCaptureLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "galleryPickerLauncher", "documentPickerLauncher", "", "requestPermissionLauncher", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "intent", "Landroid/content/Intent;", "initViews", "setupWindowInsets", "setupRecyclerView", "setupListeners", "showFluentActionsBottomSheet", "showMeBottomSheet", "showSharedBottomSheet", "initConnectionAndLoad", "updateServerBadge", "isLan", "latencyMs", "getCacheKey", "folder", "search", "filter", "sort", "order", "loadFiles", "showLoading", "bypassCache", "displayItems", "items", "", "Lcom/nasdrive/app/NasItem;", "loadStorage", "updateBreadcrumbs", "addBreadcrumbView", "label", "path", "isLast", "handleItemClick", "item", "showImageViewerDialog", "showItemOptionsMenu", "anchor", "showMovePickerBottomSheet", "confirmDownloadVaultItem", "moveToVault", "checkVaultAndImport", "paths", "executeVaultImport", "confirmExportVault", "confirmDeleteVaultItem", "showVaultSetupDialog", "onSuccess", "Lkotlin/Function0;", "showVaultUnlockDialog", "showVaultUnlockPrompt", "showLoginDialog", "openVaultView", "startDownload", "shareLink", "confirmDeleteItem", "showNewFolderDialog", "showSortDialog", "launchCameraCapture", "uploadFiles", "uris", "getFileNameFromUri", "uri", "handleIncomingShareIntent", "setupBackNavigation", "setupSwipeBackGesture", "checkPermissions", "showServerBottomSheet", "showDownloadsBottomSheet", "isVideoExtension", "fileName", "formatFileSize", "bytes", "vibrate", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class MainActivity extends AppCompatActivity {
    private long activeRequestId;
    private long backPressedTime;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout breadcrumbsContainer;
    private ImageButton btnClearSearch;
    private MaterialButton btnEmptyRetry;
    private FloatingActionButton btnFabMain;
    private View btnHeaderAvatar;
    private View btnQuickScan;
    private ImageButton btnSort;
    private ImageButton btnToggleViewMode;
    private Uri cameraTempUri;
    private LinearLayout chipServerBadge;
    private Call currentFilesCall;
    private EditText edtSearch;
    private LinearLayout emptyStateView;
    private NasFilesAdapter filesAdapter;
    private boolean isVaultMode;
    private LinearProgressIndicator pageProgressBar;
    private ProgressBar pbUploadProgress;
    private RecyclerView rvFiles;
    private View serverStatusDot;
    private LinearProgressIndicator storageProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtEmptySubtitle;
    private TextView txtEmptyTitle;
    private TextView txtServerBadge;
    private TextView txtStorageLabel;
    private TextView txtStoragePercent;
    private TextView txtUploadFileName;
    private TextView txtUploadSpeed;
    private LinearLayout uploadProgressCard;
    private String currentFolder = "";
    private String currentSearch = "";
    private String currentFilter = TtmlNode.COMBINE_ALL;
    private String currentSort = "name";
    private String currentOrder = "asc";
    private boolean isGridMode = true;
    private boolean isCurrentLan = true;
    private final MainActivity$folderCache$1 folderCache = new LruCache<String, List<? extends NasItem>>() { // from class: com.nasdrive.app.MainActivity$folderCache$1
    };
    private final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ActivityResultLauncher<Uri> cameraCaptureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda0
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.cameraCaptureLauncher$lambda$0(MainActivity.this, ((Boolean) obj).booleanValue());
        }
    });
    private final ActivityResultLauncher<String> galleryPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda1
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.galleryPickerLauncher$lambda$1(MainActivity.this, (List) obj);
        }
    });
    private final ActivityResultLauncher<String[]> documentPickerLauncher = registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(), new ActivityResultCallback() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda2
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.documentPickerLauncher$lambda$2(MainActivity.this, (List) obj);
        }
    });
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda3
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            Intrinsics.checkNotNullParameter((Map) obj, "<unused var>");
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void cameraCaptureLauncher$lambda$0(MainActivity this$0, boolean success) {
        if (success && this$0.cameraTempUri != null) {
            Uri uri = this$0.cameraTempUri;
            Intrinsics.checkNotNull(uri);
            this$0.uploadFiles(CollectionsKt.listOf(uri));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void galleryPickerLauncher$lambda$1(MainActivity this$0, List uris) {
        Intrinsics.checkNotNullParameter(uris, "uris");
        if (!uris.isEmpty()) {
            this$0.uploadFiles(uris);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void documentPickerLauncher$lambda$2(MainActivity this$0, List uris) {
        Intrinsics.checkNotNullParameter(uris, "uris");
        if (!uris.isEmpty()) {
            this$0.uploadFiles(uris);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupWindowInsets();
        setupRecyclerView();
        setupListeners();
        setupBackNavigation();
        setupSwipeBackGesture();
        checkPermissions();
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "getIntent(...)");
        handleIncomingShareIntent(intent);
        initConnectionAndLoad();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        handleIncomingShareIntent(intent);
    }

    private final void initViews() {
        View findViewById = findViewById(R.id.btnHeaderAvatar);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        this.btnHeaderAvatar = findViewById;
        View findViewById2 = findViewById(R.id.btnQuickScan);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
        this.btnQuickScan = findViewById2;
        View findViewById3 = findViewById(R.id.edtSearch);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
        this.edtSearch = (EditText) findViewById3;
        View findViewById4 = findViewById(R.id.btnClearSearch);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
        this.btnClearSearch = (ImageButton) findViewById4;
        View findViewById5 = findViewById(R.id.chipServerBadge);
        Intrinsics.checkNotNullExpressionValue(findViewById5, "findViewById(...)");
        this.chipServerBadge = (LinearLayout) findViewById5;
        View findViewById6 = findViewById(R.id.serverStatusDot);
        Intrinsics.checkNotNullExpressionValue(findViewById6, "findViewById(...)");
        this.serverStatusDot = findViewById6;
        View findViewById7 = findViewById(R.id.txtServerBadge);
        Intrinsics.checkNotNullExpressionValue(findViewById7, "findViewById(...)");
        this.txtServerBadge = (TextView) findViewById7;
        View findViewById8 = findViewById(R.id.btnToggleViewMode);
        Intrinsics.checkNotNullExpressionValue(findViewById8, "findViewById(...)");
        this.btnToggleViewMode = (ImageButton) findViewById8;
        View findViewById9 = findViewById(R.id.btnSort);
        Intrinsics.checkNotNullExpressionValue(findViewById9, "findViewById(...)");
        this.btnSort = (ImageButton) findViewById9;
        View findViewById10 = findViewById(R.id.txtStorageLabel);
        Intrinsics.checkNotNullExpressionValue(findViewById10, "findViewById(...)");
        this.txtStorageLabel = (TextView) findViewById10;
        View findViewById11 = findViewById(R.id.txtStoragePercent);
        Intrinsics.checkNotNullExpressionValue(findViewById11, "findViewById(...)");
        this.txtStoragePercent = (TextView) findViewById11;
        View findViewById12 = findViewById(R.id.storageProgressBar);
        Intrinsics.checkNotNullExpressionValue(findViewById12, "findViewById(...)");
        this.storageProgressBar = (LinearProgressIndicator) findViewById12;
        View findViewById13 = findViewById(R.id.breadcrumbsContainer);
        Intrinsics.checkNotNullExpressionValue(findViewById13, "findViewById(...)");
        this.breadcrumbsContainer = (LinearLayout) findViewById13;
        View findViewById14 = findViewById(R.id.pageProgressBar);
        Intrinsics.checkNotNullExpressionValue(findViewById14, "findViewById(...)");
        this.pageProgressBar = (LinearProgressIndicator) findViewById14;
        View findViewById15 = findViewById(R.id.swipeRefreshLayout);
        Intrinsics.checkNotNullExpressionValue(findViewById15, "findViewById(...)");
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById15;
        View findViewById16 = findViewById(R.id.rvFiles);
        Intrinsics.checkNotNullExpressionValue(findViewById16, "findViewById(...)");
        this.rvFiles = (RecyclerView) findViewById16;
        View findViewById17 = findViewById(R.id.emptyStateView);
        Intrinsics.checkNotNullExpressionValue(findViewById17, "findViewById(...)");
        this.emptyStateView = (LinearLayout) findViewById17;
        View findViewById18 = findViewById(R.id.txtEmptyTitle);
        Intrinsics.checkNotNullExpressionValue(findViewById18, "findViewById(...)");
        this.txtEmptyTitle = (TextView) findViewById18;
        View findViewById19 = findViewById(R.id.txtEmptySubtitle);
        Intrinsics.checkNotNullExpressionValue(findViewById19, "findViewById(...)");
        this.txtEmptySubtitle = (TextView) findViewById19;
        View findViewById20 = findViewById(R.id.btnEmptyRetry);
        Intrinsics.checkNotNullExpressionValue(findViewById20, "findViewById(...)");
        this.btnEmptyRetry = (MaterialButton) findViewById20;
        View findViewById21 = findViewById(R.id.btnFabMain);
        Intrinsics.checkNotNullExpressionValue(findViewById21, "findViewById(...)");
        this.btnFabMain = (FloatingActionButton) findViewById21;
        View findViewById22 = findViewById(R.id.uploadProgressCard);
        Intrinsics.checkNotNullExpressionValue(findViewById22, "findViewById(...)");
        this.uploadProgressCard = (LinearLayout) findViewById22;
        View findViewById23 = findViewById(R.id.txtUploadFileName);
        Intrinsics.checkNotNullExpressionValue(findViewById23, "findViewById(...)");
        this.txtUploadFileName = (TextView) findViewById23;
        View findViewById24 = findViewById(R.id.txtUploadSpeed);
        Intrinsics.checkNotNullExpressionValue(findViewById24, "findViewById(...)");
        this.txtUploadSpeed = (TextView) findViewById24;
        View findViewById25 = findViewById(R.id.pbUploadProgress);
        Intrinsics.checkNotNullExpressionValue(findViewById25, "findViewById(...)");
        this.pbUploadProgress = (ProgressBar) findViewById25;
        View findViewById26 = findViewById(R.id.bottomNavigationView);
        Intrinsics.checkNotNullExpressionValue(findViewById26, "findViewById(...)");
        this.bottomNavigationView = (BottomNavigationView) findViewById26;
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        if (swipeRefreshLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swipeRefreshLayout");
            swipeRefreshLayout = null;
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private final void setupWindowInsets() {
        View appBar = findViewById(R.id.appBarLayout);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, new OnApplyWindowInsetsListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda96
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return MainActivity.setupWindowInsets$lambda$4(view, windowInsetsCompat);
            }
        });
        BottomNavigationView bottomNavigationView = this.bottomNavigationView;
        if (bottomNavigationView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bottomNavigationView");
            bottomNavigationView = null;
        }
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, new OnApplyWindowInsetsListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda97
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return MainActivity.setupWindowInsets$lambda$5(view, windowInsetsCompat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final WindowInsetsCompat setupWindowInsets$lambda$4(View view, WindowInsetsCompat insets) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(insets, "insets");
        Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
        Intrinsics.checkNotNullExpressionValue(statusBarInsets, "getInsets(...)");
        view.setPadding(0, statusBarInsets.top, 0, 0);
        return insets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final WindowInsetsCompat setupWindowInsets$lambda$5(View view, WindowInsetsCompat insets) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(insets, "insets");
        Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
        Intrinsics.checkNotNullExpressionValue(navInsets, "getInsets(...)");
        view.setPadding(0, 0, 0, navInsets.bottom);
        return insets;
    }

    private final void setupRecyclerView() {
        this.filesAdapter = new NasFilesAdapter(this, this.isGridMode, new Function1() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.setupRecyclerView$lambda$6(MainActivity.this, (NasItem) obj);
            }
        }, new Function2() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return MainActivity.setupRecyclerView$lambda$7(MainActivity.this, (NasItem) obj, (View) obj2);
            }
        });
        RecyclerView recyclerView = this.rvFiles;
        RecyclerView recyclerView2 = null;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView = null;
        }
        recyclerView.setLayoutManager(this.isGridMode ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));
        RecyclerView recyclerView3 = this.rvFiles;
        if (recyclerView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView3 = null;
        }
        NasFilesAdapter nasFilesAdapter = this.filesAdapter;
        if (nasFilesAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("filesAdapter");
            nasFilesAdapter = null;
        }
        recyclerView3.setAdapter(nasFilesAdapter);
        RecyclerView recyclerView4 = this.rvFiles;
        if (recyclerView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView4 = null;
        }
        recyclerView4.setHasFixedSize(true);
        RecyclerView recyclerView5 = this.rvFiles;
        if (recyclerView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView5 = null;
        }
        recyclerView5.setItemViewCacheSize(25);
        RecyclerView recyclerView6 = this.rvFiles;
        if (recyclerView6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView6 = null;
        }
        recyclerView6.getRecycledViewPool().setMaxRecycledViews(1, 30);
        RecyclerView recyclerView7 = this.rvFiles;
        if (recyclerView7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
        } else {
            recyclerView2 = recyclerView7;
        }
        recyclerView2.getRecycledViewPool().setMaxRecycledViews(2, 30);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit setupRecyclerView$lambda$6(MainActivity this$0, NasItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        this$0.handleItemClick(item);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit setupRecyclerView$lambda$7(MainActivity this$0, NasItem item, View view) {
        Intrinsics.checkNotNullParameter(item, "item");
        Intrinsics.checkNotNullParameter(view, "view");
        this$0.showItemOptionsMenu(item, view);
        return Unit.INSTANCE;
    }

    private final void setupListeners() {
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        BottomNavigationView bottomNavigationView = null;
        if (swipeRefreshLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swipeRefreshLayout");
            swipeRefreshLayout = null;
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda30
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                MainActivity.setupListeners$lambda$8(MainActivity.this);
            }
        });
        View view = this.btnHeaderAvatar;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnHeaderAvatar");
            view = null;
        }
        view.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda32
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                MainActivity.setupListeners$lambda$9(MainActivity.this, view2);
            }
        });
        View view2 = this.btnQuickScan;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnQuickScan");
            view2 = null;
        }
        view2.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda33
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$10(MainActivity.this, view3);
            }
        });
        LinearLayout linearLayout = this.chipServerBadge;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("chipServerBadge");
            linearLayout = null;
        }
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$11(MainActivity.this, view3);
            }
        });
        ImageButton imageButton = this.btnToggleViewMode;
        if (imageButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnToggleViewMode");
            imageButton = null;
        }
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda35
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$12(MainActivity.this, view3);
            }
        });
        ImageButton imageButton2 = this.btnSort;
        if (imageButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnSort");
            imageButton2 = null;
        }
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda36
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$13(MainActivity.this, view3);
            }
        });
        MaterialButton materialButton = this.btnEmptyRetry;
        if (materialButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnEmptyRetry");
            materialButton = null;
        }
        materialButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda37
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.this.initConnectionAndLoad();
            }
        });
        EditText editText = this.edtSearch;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
            editText = null;
        }
        editText.addTextChangedListener(new TextWatcher() { // from class: com.nasdrive.app.MainActivity$setupListeners$8
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q;
                ImageButton imageButton3;
                String obj;
                if (s == null || (obj = s.toString()) == null || (q = StringsKt.trim((CharSequence) obj).toString()) == null) {
                    q = "";
                }
                imageButton3 = MainActivity.this.btnClearSearch;
                if (imageButton3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnClearSearch");
                    imageButton3 = null;
                }
                imageButton3.setVisibility(q.length() > 0 ? 0 : 8);
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        EditText editText2 = this.edtSearch;
        if (editText2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
            editText2 = null;
        }
        editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda38
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return MainActivity.setupListeners$lambda$15(MainActivity.this, textView, i, keyEvent);
            }
        });
        ImageButton imageButton3 = this.btnClearSearch;
        if (imageButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnClearSearch");
            imageButton3 = null;
        }
        imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda39
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$16(MainActivity.this, view3);
            }
        });
        FloatingActionButton floatingActionButton = this.btnFabMain;
        if (floatingActionButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnFabMain");
            floatingActionButton = null;
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda40
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                MainActivity.setupListeners$lambda$17(MainActivity.this, view3);
            }
        });
        BottomNavigationView bottomNavigationView2 = this.bottomNavigationView;
        if (bottomNavigationView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bottomNavigationView");
        } else {
            bottomNavigationView = bottomNavigationView2;
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda31
            @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                return MainActivity.setupListeners$lambda$18(MainActivity.this, menuItem);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$8(MainActivity this$0) {
        this$0.folderCache.remove(this$0.getCacheKey(this$0.currentFolder, this$0.currentSearch, this$0.currentFilter, this$0.currentSort, this$0.currentOrder));
        this$0.loadFiles(false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$9(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.showMeBottomSheet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$10(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.launchCameraCapture();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$11(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.showServerBottomSheet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$12(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.isGridMode = !this$0.isGridMode;
        NasFilesAdapter nasFilesAdapter = this$0.filesAdapter;
        NasFilesAdapter nasFilesAdapter2 = null;
        if (nasFilesAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("filesAdapter");
            nasFilesAdapter = null;
        }
        nasFilesAdapter.setGridMode(this$0.isGridMode);
        RecyclerView recyclerView = this$0.rvFiles;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView = null;
        }
        recyclerView.setLayoutManager(this$0.isGridMode ? new GridLayoutManager(this$0, 2) : new LinearLayoutManager(this$0));
        ImageButton imageButton = this$0.btnToggleViewMode;
        if (imageButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnToggleViewMode");
            imageButton = null;
        }
        imageButton.setImageResource(this$0.isGridMode ? R.drawable.ic_fluent_list_24_regular : R.drawable.ic_fluent_grid_24_regular);
        NasFilesAdapter nasFilesAdapter3 = this$0.filesAdapter;
        if (nasFilesAdapter3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("filesAdapter");
            nasFilesAdapter3 = null;
        }
        NasFilesAdapter nasFilesAdapter4 = this$0.filesAdapter;
        if (nasFilesAdapter4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("filesAdapter");
        } else {
            nasFilesAdapter2 = nasFilesAdapter4;
        }
        nasFilesAdapter3.notifyItemRangeChanged(0, nasFilesAdapter2.getItemCount());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$13(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.showSortDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean setupListeners$lambda$15(MainActivity this$0, TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == 3) {
            EditText editText = this$0.edtSearch;
            if (editText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                editText = null;
            }
            this$0.currentSearch = StringsKt.trim((CharSequence) editText.getText().toString()).toString();
            this$0.loadFiles(true, true);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$16(MainActivity this$0, View it) {
        EditText editText = this$0.edtSearch;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
            editText = null;
        }
        editText.setText("");
        this$0.currentSearch = "";
        this$0.loadFiles(true, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void setupListeners$lambda$17(MainActivity this$0, View it) {
        this$0.vibrate();
        this$0.showFluentActionsBottomSheet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean setupListeners$lambda$18(MainActivity this$0, MenuItem menuItem) {
        Intrinsics.checkNotNullParameter(menuItem, "menuItem");
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_home) {
            this$0.isVaultMode = false;
            this$0.currentFilter = TtmlNode.COMBINE_ALL;
            this$0.currentSort = "date";
            this$0.currentOrder = "desc";
            this$0.currentFolder = "";
            this$0.currentSearch = "";
            EditText editText = this$0.edtSearch;
            if (editText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                editText = null;
            }
            editText.setText("");
            RecyclerView recyclerView = this$0.rvFiles;
            if (recyclerView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                recyclerView = null;
            }
            recyclerView.scrollToPosition(0);
            loadFiles$default(this$0, false, false, 3, null);
            return true;
        }
        if (itemId == R.id.nav_files) {
            this$0.isVaultMode = false;
            this$0.currentFilter = TtmlNode.COMBINE_ALL;
            this$0.currentSort = "name";
            this$0.currentOrder = "asc";
            this$0.currentFolder = "";
            this$0.currentSearch = "";
            EditText editText2 = this$0.edtSearch;
            if (editText2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                editText2 = null;
            }
            editText2.setText("");
            RecyclerView recyclerView2 = this$0.rvFiles;
            if (recyclerView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                recyclerView2 = null;
            }
            recyclerView2.scrollToPosition(0);
            loadFiles$default(this$0, false, false, 3, null);
            return true;
        }
        if (itemId == R.id.nav_photos) {
            this$0.isVaultMode = false;
            this$0.currentFilter = "image";
            this$0.currentSort = "date";
            this$0.currentOrder = "desc";
            this$0.currentFolder = "";
            this$0.currentSearch = "";
            EditText editText3 = this$0.edtSearch;
            if (editText3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                editText3 = null;
            }
            editText3.setText("");
            RecyclerView recyclerView3 = this$0.rvFiles;
            if (recyclerView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                recyclerView3 = null;
            }
            recyclerView3.scrollToPosition(0);
            loadFiles$default(this$0, false, false, 3, null);
            return true;
        }
        if (itemId == R.id.nav_shared) {
            this$0.showSharedBottomSheet();
            return false;
        }
        if (itemId != R.id.nav_me) {
            return false;
        }
        this$0.showMeBottomSheet();
        return false;
    }

    private final void showFluentActionsBottomSheet() {
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.sheet_fluent_actions, (ViewGroup) null);
        sheetDialog.setContentView(sheetView);
        sheetView.findViewById(R.id.actScanCamera).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda78
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showFluentActionsBottomSheet$lambda$19(BottomSheetDialog.this, this, view);
            }
        });
        sheetView.findViewById(R.id.actUploadMedia).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda79
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showFluentActionsBottomSheet$lambda$20(BottomSheetDialog.this, this, view);
            }
        });
        sheetView.findViewById(R.id.actNewFolder).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda80
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showFluentActionsBottomSheet$lambda$21(BottomSheetDialog.this, this, view);
            }
        });
        sheetView.findViewById(R.id.actUploadFile).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda81
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showFluentActionsBottomSheet$lambda$22(BottomSheetDialog.this, this, view);
            }
        });
        sheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showFluentActionsBottomSheet$lambda$19(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.launchCameraCapture();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showFluentActionsBottomSheet$lambda$20(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.galleryPickerLauncher.launch("*/*");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showFluentActionsBottomSheet$lambda$21(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.showNewFolderDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showFluentActionsBottomSheet$lambda$22(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.documentPickerLauncher.launch(new String[]{"*/*"});
    }

    private final void showMeBottomSheet() {
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        TextView textView = null;
        final View sheetView = getLayoutInflater().inflate(R.layout.sheet_onedrive_me, (ViewGroup) null);
        sheetDialog.setContentView(sheetView);
        TextView txtServerIp = (TextView) sheetView.findViewById(R.id.txtMeServerIp);
        TextView txtPercent = (TextView) sheetView.findViewById(R.id.txtMePercent);
        LinearProgressIndicator pbStorage = (LinearProgressIndicator) sheetView.findViewById(R.id.pbMeStorage);
        TextView txtDetails = (TextView) sheetView.findViewById(R.id.txtMeStorageDetails);
        String serverUrl = ServerConfig.INSTANCE.getSelectedUrl(this);
        txtServerIp.setText(serverUrl);
        TextView textView2 = this.txtStoragePercent;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtStoragePercent");
            textView2 = null;
        }
        txtPercent.setText(textView2.getText());
        LinearProgressIndicator linearProgressIndicator = this.storageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("storageProgressBar");
            linearProgressIndicator = null;
        }
        pbStorage.setProgress(linearProgressIndicator.getProgress());
        TextView textView3 = this.txtStorageLabel;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtStorageLabel");
        } else {
            textView = textView3;
        }
        txtDetails.setText(textView.getText());
        View findViewById = sheetView.findViewById(R.id.btnMeVault);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda43
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.showMeBottomSheet$lambda$23(BottomSheetDialog.this, this, view);
                }
            });
        }
        sheetView.findViewById(R.id.btnMeRecycleBin).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda44
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMeBottomSheet$lambda$24(BottomSheetDialog.this, this, view);
            }
        });
        sheetView.findViewById(R.id.btnMeServerSettings).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda45
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMeBottomSheet$lambda$25(BottomSheetDialog.this, this, view);
            }
        });
        sheetView.findViewById(R.id.btnMeClearCache).setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda46
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMeBottomSheet$lambda$26(MainActivity.this, sheetView, view);
            }
        });
        sheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMeBottomSheet$lambda$23(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.openVaultView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMeBottomSheet$lambda$24(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.currentFolder = ".recycle";
        this$0.currentSearch = "";
        EditText editText = this$0.edtSearch;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
            editText = null;
        }
        editText.setText("");
        RecyclerView recyclerView = this$0.rvFiles;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView = null;
        }
        recyclerView.scrollToPosition(0);
        loadFiles$default(this$0, false, false, 3, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMeBottomSheet$lambda$25(BottomSheetDialog $sheetDialog, MainActivity this$0, View it) {
        $sheetDialog.dismiss();
        this$0.showServerBottomSheet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMeBottomSheet$lambda$26(MainActivity this$0, View $sheetView, View it) {
        MemoryCache memoryCache = Coil.imageLoader(this$0).getMemoryCache();
        if (memoryCache != null) {
            memoryCache.clear();
        }
        DiskCache diskCache = Coil.imageLoader(this$0).getDiskCache();
        if (diskCache != null) {
            diskCache.clear();
        }
        this$0.folderCache.evictAll();
        TextView txtCache = (TextView) $sheetView.findViewById(R.id.txtMeCacheSize);
        txtCache.setText("Đã dọn dẹp");
        Toast.makeText(this$0, "Đã xóa toàn bộ bộ nhớ đệm hình ảnh và danh mục thư mục!", 0).show();
    }

    private final void showSharedBottomSheet() {
        final String serverUrl = ServerConfig.INSTANCE.getSelectedUrl(this);
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "Đã chia sẻ (Shared)").setMessage((CharSequence) ("Mọi tệp tin trong thư mục công cộng trên máy chủ NAS có thể truy cập trực tiếp tại:\n\n" + serverUrl + "/api/files\n\nBạn có thể chia sẻ liên kết này cho bất kỳ máy tính hoặc điện thoại nào trong mạng.")).setPositiveButton((CharSequence) "Sao chép liên kết", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda29
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.showSharedBottomSheet$lambda$27(MainActivity.this, serverUrl, dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "Đóng", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showSharedBottomSheet$lambda$27(MainActivity this$0, String $serverUrl, DialogInterface dialogInterface, int i) {
        Object systemService = this$0.getSystemService("clipboard");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.content.ClipboardManager");
        ClipboardManager clipboard = (ClipboardManager) systemService;
        ClipData clip = ClipData.newPlainText("NAS Share Link", $serverUrl);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this$0, "Đã sao chép liên kết vào bộ nhớ tạm!", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void initConnectionAndLoad() {
        LinearProgressIndicator linearProgressIndicator = this.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.initConnectionAndLoad$lambda$29(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void initConnectionAndLoad$lambda$29(final MainActivity this$0) {
        DetectionResult test;
        boolean isAuto = ServerConfig.INSTANCE.isAutoMode(this$0);
        if (isAuto) {
            test = ServerDetector.INSTANCE.findBestServer();
        } else {
            String selected = ServerConfig.INSTANCE.getSelectedUrl(this$0);
            DetectionResult test2 = ServerDetector.INSTANCE.testUrl(selected);
            test = new DetectionResult(selected, ServerConfig.INSTANCE.isLanUrl(selected), test2.getLatencyMs(), test2.getSuccess());
        }
        final DetectionResult detection = test;
        ServerConfig.INSTANCE.setActiveUrl(this$0, detection.getUrl());
        this$0.isCurrentLan = detection.isLan();
        if (ServerConfig.INSTANCE.getAuthToken(this$0).length() == 0) {
            try {
                Result.m381boximpl(NasApiClient.INSTANCE.m373login0E7RQCE(this$0, "admin", "Admin@NAS2026!"));
            } catch (Exception e) {
            }
        }
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.initConnectionAndLoad$lambda$29$lambda$28(MainActivity.this, detection);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void initConnectionAndLoad$lambda$29$lambda$28(MainActivity this$0, DetectionResult $detection) {
        this$0.updateServerBadge($detection.isLan(), $detection.getLatencyMs());
        this$0.loadFiles(true, true);
        this$0.loadStorage();
    }

    private final void updateServerBadge(boolean isLan, long latencyMs) {
        View view = this.serverStatusDot;
        TextView textView = null;
        if (isLan) {
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serverStatusDot");
                view = null;
            }
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lan_green));
            TextView textView2 = this.txtServerBadge;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtServerBadge");
            } else {
                textView = textView2;
            }
            textView.setText(latencyMs > 0 ? "LAN (" + latencyMs + "ms)" : "LAN 1Gbps");
            return;
        }
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("serverStatusDot");
            view = null;
        }
        view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.wan_blue));
        TextView textView3 = this.txtServerBadge;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtServerBadge");
        } else {
            textView = textView3;
        }
        textView.setText(latencyMs > 0 ? "Cloudflare (" + latencyMs + "ms)" : "Cloudflare");
    }

    private final String getCacheKey(String folder, String search, String filter, String sort, String order) {
        return folder + "|" + search + "|" + filter + "|" + sort + "|" + order;
    }

    static /* synthetic */ void loadFiles$default(MainActivity mainActivity, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        mainActivity.loadFiles(z, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadFiles(boolean showLoading, boolean bypassCache) {
        updateBreadcrumbs();
        LinearProgressIndicator linearProgressIndicator = null;
        if (this.isVaultMode) {
            Call call = this.currentFilesCall;
            if (call != null) {
                call.cancel();
            }
            this.activeRequestId++;
            final long reqId = this.activeRequestId;
            if (showLoading) {
                LinearProgressIndicator linearProgressIndicator2 = this.pageProgressBar;
                if (linearProgressIndicator2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
                } else {
                    linearProgressIndicator = linearProgressIndicator2;
                }
                linearProgressIndicator.setVisibility(0);
            }
            this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda103
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.loadFiles$lambda$34(MainActivity.this, reqId);
                }
            });
            return;
        }
        final String targetFolder = this.currentFolder;
        final String targetSearch = this.currentSearch;
        final String targetFilter = this.currentFilter;
        final String targetSort = this.currentSort;
        final String targetOrder = this.currentOrder;
        final String cacheKey = getCacheKey(targetFolder, targetSearch, targetFilter, targetSort, targetOrder);
        List cachedItems = !bypassCache ? get(cacheKey) : null;
        final boolean hasCache = cachedItems != null;
        if (hasCache) {
            Intrinsics.checkNotNull(cachedItems);
            displayItems(cachedItems);
            if (showLoading) {
                LinearProgressIndicator linearProgressIndicator3 = this.pageProgressBar;
                if (linearProgressIndicator3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
                } else {
                    linearProgressIndicator = linearProgressIndicator3;
                }
                linearProgressIndicator.setVisibility(0);
            }
        } else if (showLoading) {
            LinearProgressIndicator linearProgressIndicator4 = this.pageProgressBar;
            if (linearProgressIndicator4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            } else {
                linearProgressIndicator = linearProgressIndicator4;
            }
            linearProgressIndicator.setVisibility(0);
        }
        Call call2 = this.currentFilesCall;
        if (call2 != null) {
            call2.cancel();
        }
        this.activeRequestId++;
        final long reqId2 = this.activeRequestId;
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda104
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.loadFiles$lambda$40(MainActivity.this, targetFolder, targetSearch, targetFilter, targetSort, targetOrder, reqId2, cacheKey, hasCache);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadFiles$lambda$34(final MainActivity this$0, final long $reqId) {
        final Object res = NasApiClient.INSTANCE.m369fetchVaultFilesgIAlus(this$0, this$0.currentFolder);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.loadFiles$lambda$34$lambda$33($reqId, this$0, res);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadFiles$lambda$34$lambda$33(long $reqId, final MainActivity this$0, Object $res) {
        if ($reqId != this$0.activeRequestId) {
            return;
        }
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        MaterialButton materialButton = null;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        SwipeRefreshLayout swipeRefreshLayout = this$0.swipeRefreshLayout;
        if (swipeRefreshLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swipeRefreshLayout");
            swipeRefreshLayout = null;
        }
        swipeRefreshLayout.setRefreshing(false);
        if (Result.m389isSuccessimpl($res)) {
            this$0.displayItems((List) $res);
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            if (m385exceptionOrNullimpl instanceof VaultLockedException) {
                this$0.showVaultUnlockPrompt();
                return;
            }
            String message = m385exceptionOrNullimpl.getMessage();
            if (!(message != null && StringsKt.contains$default((CharSequence) message, (CharSequence) "401", false, 2, (Object) null))) {
                String message2 = m385exceptionOrNullimpl.getMessage();
                if (!(message2 != null && StringsKt.contains$default((CharSequence) message2, (CharSequence) "Chưa đăng nhập", false, 2, (Object) null))) {
                    RecyclerView recyclerView = this$0.rvFiles;
                    if (recyclerView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                        recyclerView = null;
                    }
                    recyclerView.setVisibility(8);
                    LinearLayout linearLayout = this$0.emptyStateView;
                    if (linearLayout == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("emptyStateView");
                        linearLayout = null;
                    }
                    linearLayout.setVisibility(0);
                    TextView textView = this$0.txtEmptyTitle;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("txtEmptyTitle");
                        textView = null;
                    }
                    textView.setText("Thư mục bí mật");
                    TextView textView2 = this$0.txtEmptySubtitle;
                    if (textView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("txtEmptySubtitle");
                        textView2 = null;
                    }
                    String message3 = m385exceptionOrNullimpl.getMessage();
                    if (message3 == null) {
                        message3 = "Chưa thể truy cập Thư mục bí mật";
                    }
                    textView2.setText(message3);
                    MaterialButton materialButton2 = this$0.btnEmptyRetry;
                    if (materialButton2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("btnEmptyRetry");
                    } else {
                        materialButton = materialButton2;
                    }
                    materialButton.setVisibility(0);
                    return;
                }
            }
            this$0.showLoginDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda54
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.loadFiles$lambda$34$lambda$33$lambda$32$lambda$31(MainActivity.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit loadFiles$lambda$34$lambda$33$lambda$32$lambda$31(MainActivity this$0) {
        this$0.loadFiles(true, true);
        this$0.loadStorage();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadFiles$lambda$40(final MainActivity this$0, String $targetFolder, String $targetSearch, String $targetFilter, String $targetSort, String $targetOrder, final long $reqId, final String $cacheKey, final boolean $hasCache) {
        Object m382constructorimpl;
        Call call = NasApiClient.INSTANCE.newFilesCall(this$0, $targetFolder, $targetSearch, $targetFilter, $targetSort, $targetOrder);
        this$0.currentFilesCall = call;
        try {
            Response execute = call.execute();
            try {
                m382constructorimpl = NasApiClient.INSTANCE.m375parseFilesResponseIoAF18A(execute);
                CloseableKt.closeFinally(execute, null);
            } finally {
            }
        } catch (IOException e) {
            if (call.getCanceled()) {
                return;
            }
            Result.Companion companion = Result.INSTANCE;
            m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(e));
        } catch (Exception e2) {
            Result.Companion companion2 = Result.INSTANCE;
            m382constructorimpl = Result.m382constructorimpl(ResultKt.createFailure(e2));
        }
        final Object result = m382constructorimpl;
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.loadFiles$lambda$40$lambda$39($reqId, this$0, result, $cacheKey, $hasCache);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadFiles$lambda$40$lambda$39(long $reqId, final MainActivity this$0, Object $result, String $cacheKey, boolean $hasCache) {
        if ($reqId != this$0.activeRequestId) {
            return;
        }
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        MaterialButton materialButton = null;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        SwipeRefreshLayout swipeRefreshLayout = this$0.swipeRefreshLayout;
        if (swipeRefreshLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swipeRefreshLayout");
            swipeRefreshLayout = null;
        }
        swipeRefreshLayout.setRefreshing(false);
        if (Result.m389isSuccessimpl($result)) {
            List list = (List) $result;
            this$0.folderCache.put($cacheKey, list);
            this$0.displayItems(list);
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($result);
        if (m385exceptionOrNullimpl != null) {
            String message = m385exceptionOrNullimpl.getMessage();
            if (!(message != null && StringsKt.contains$default((CharSequence) message, (CharSequence) "401", false, 2, (Object) null))) {
                String message2 = m385exceptionOrNullimpl.getMessage();
                if (!(message2 != null && StringsKt.contains$default((CharSequence) message2, (CharSequence) "Chưa đăng nhập", false, 2, (Object) null))) {
                    if (!$hasCache) {
                        RecyclerView recyclerView = this$0.rvFiles;
                        if (recyclerView == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                            recyclerView = null;
                        }
                        recyclerView.setVisibility(8);
                        LinearLayout linearLayout = this$0.emptyStateView;
                        if (linearLayout == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("emptyStateView");
                            linearLayout = null;
                        }
                        linearLayout.setVisibility(0);
                        TextView textView = this$0.txtEmptyTitle;
                        if (textView == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("txtEmptyTitle");
                            textView = null;
                        }
                        textView.setText("Không thể kết nối máy chủ");
                        TextView textView2 = this$0.txtEmptySubtitle;
                        if (textView2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("txtEmptySubtitle");
                            textView2 = null;
                        }
                        textView2.setText(m385exceptionOrNullimpl.getMessage() + "\nĐịa chỉ: " + NasApiClient.INSTANCE.getBaseUrl(this$0));
                        MaterialButton materialButton2 = this$0.btnEmptyRetry;
                        if (materialButton2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("btnEmptyRetry");
                        } else {
                            materialButton = materialButton2;
                        }
                        materialButton.setVisibility(0);
                        return;
                    }
                    return;
                }
            }
            this$0.showLoginDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda59
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.loadFiles$lambda$40$lambda$39$lambda$38$lambda$37(MainActivity.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit loadFiles$lambda$40$lambda$39$lambda$38$lambda$37(MainActivity this$0) {
        this$0.loadFiles(true, true);
        this$0.loadStorage();
        return Unit.INSTANCE;
    }

    private final void displayItems(List<NasItem> items) {
        TextView textView = null;
        if (items.isEmpty()) {
            RecyclerView recyclerView = this.rvFiles;
            if (recyclerView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                recyclerView = null;
            }
            recyclerView.setVisibility(8);
            LinearLayout linearLayout = this.emptyStateView;
            if (linearLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("emptyStateView");
                linearLayout = null;
            }
            linearLayout.setVisibility(0);
            MaterialButton materialButton = this.btnEmptyRetry;
            if (materialButton == null) {
                Intrinsics.throwUninitializedPropertyAccessException("btnEmptyRetry");
                materialButton = null;
            }
            materialButton.setVisibility(8);
            boolean z = this.currentSearch.length() > 0;
            TextView textView2 = this.txtEmptyTitle;
            if (z) {
                if (textView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtEmptyTitle");
                    textView2 = null;
                }
                textView2.setText("Không tìm thấy kết quả");
                TextView textView3 = this.txtEmptySubtitle;
                if (textView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("txtEmptySubtitle");
                } else {
                    textView = textView3;
                }
                textView.setText("Không có tệp nào khớp với từ khóa \"" + this.currentSearch + "\"");
                return;
            }
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtEmptyTitle");
                textView2 = null;
            }
            textView2.setText("Thư mục trống");
            TextView textView4 = this.txtEmptySubtitle;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtEmptySubtitle");
            } else {
                textView = textView4;
            }
            textView.setText("Bấm dấu + ở góc dưới để tải tệp hoặc chụp ảnh lên máy chủ");
            return;
        }
        LinearLayout linearLayout2 = this.emptyStateView;
        if (linearLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("emptyStateView");
            linearLayout2 = null;
        }
        linearLayout2.setVisibility(8);
        RecyclerView recyclerView2 = this.rvFiles;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView2 = null;
        }
        recyclerView2.setVisibility(0);
        NasFilesAdapter nasFilesAdapter = this.filesAdapter;
        if (nasFilesAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("filesAdapter");
            nasFilesAdapter = null;
        }
        NasFilesAdapter.submitList$default(nasFilesAdapter, items, null, 2, null);
    }

    private final void loadStorage() {
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.loadStorage$lambda$43(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadStorage$lambda$43(final MainActivity this$0) {
        Object res = NasApiClient.INSTANCE.m368fetchStorageIoAF18A(this$0);
        if (Result.m389isSuccessimpl(res)) {
            final StorageInfo storageInfo = (StorageInfo) res;
            this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.loadStorage$lambda$43$lambda$42$lambda$41(MainActivity.this, storageInfo);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void loadStorage$lambda$43$lambda$42$lambda$41(MainActivity this$0, StorageInfo $info) {
        TextView textView = this$0.txtStorageLabel;
        LinearProgressIndicator linearProgressIndicator = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtStorageLabel");
            textView = null;
        }
        textView.setText("☁️ Đã dùng " + $info.getUsedGb() + " GB / " + $info.getTotalGb() + " GB");
        TextView textView2 = this$0.txtStoragePercent;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtStoragePercent");
            textView2 = null;
        }
        textView2.setText($info.getUsedPercent() + "%");
        LinearProgressIndicator linearProgressIndicator2 = this$0.storageProgressBar;
        if (linearProgressIndicator2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("storageProgressBar");
        } else {
            linearProgressIndicator = linearProgressIndicator2;
        }
        linearProgressIndicator.setProgress($info.getUsedPercent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateBreadcrumbs() {
        LinearLayout linearLayout = this.breadcrumbsContainer;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("breadcrumbsContainer");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        if (this.isVaultMode) {
            addBreadcrumbView("🔒 Thư mục bí mật", "vault", true);
            return;
        }
        addBreadcrumbView$default(this, "Tệp của tôi", "", false, 4, null);
        if (this.currentFolder.length() > 0) {
            List<String> parts = StringsKt.split$default((CharSequence) this.currentFolder, new String[]{"/"}, false, 0, 6, (Object) null);
            String accumulated = "";
            int i = 0;
            for (String part : parts) {
                int index = i;
                i++;
                accumulated = accumulated.length() == 0 ? part : accumulated + "/" + part;
                boolean isLast = index == parts.size() - 1;
                addBreadcrumbView(part, accumulated, isLast);
            }
        }
    }

    static /* synthetic */ void addBreadcrumbView$default(MainActivity mainActivity, String str, String str2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        mainActivity.addBreadcrumbView(str, str2, z);
    }

    private final void addBreadcrumbView(String label, final String path, boolean isLast) {
        LayoutInflater from = LayoutInflater.from(this);
        int i = R.layout.item_breadcrumb;
        LinearLayout linearLayout = this.breadcrumbsContainer;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("breadcrumbsContainer");
            linearLayout = null;
        }
        View view = from.inflate(i, (ViewGroup) linearLayout, false);
        View findViewById = view.findViewById(R.id.txtCrumbName);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        TextView txtName = (TextView) findViewById;
        View findViewById2 = view.findViewById(R.id.imgCrumbArrow);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
        ImageView imgArrow = (ImageView) findViewById2;
        txtName.setText(label);
        imgArrow.setVisibility(isLast ? 8 : 0);
        if (isLast) {
            txtName.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        } else {
            txtName.setTextColor(ContextCompat.getColor(this, R.color.primary));
            txtName.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda21
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    MainActivity.addBreadcrumbView$lambda$44(MainActivity.this, path, view2);
                }
            });
        }
        LinearLayout linearLayout3 = this.breadcrumbsContainer;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("breadcrumbsContainer");
        } else {
            linearLayout2 = linearLayout3;
        }
        linearLayout2.addView(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void addBreadcrumbView$lambda$44(MainActivity this$0, String $path, View it) {
        this$0.vibrate();
        this$0.currentFolder = $path;
        if (this$0.currentSearch.length() > 0) {
            this$0.currentSearch = "";
            EditText editText = this$0.edtSearch;
            if (editText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                editText = null;
            }
            editText.setText("");
        }
        this$0.updateBreadcrumbs();
        RecyclerView recyclerView = this$0.rvFiles;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView = null;
        }
        recyclerView.scrollToPosition(0);
        loadFiles$default(this$0, true, false, 2, null);
    }

    private final void handleItemClick(NasItem item) {
        vibrate();
        RecyclerView recyclerView = null;
        if (item.isVault()) {
            if (item.isFolder()) {
                this.currentFolder = item.getPath();
                if (this.currentSearch.length() > 0) {
                    this.currentSearch = "";
                    EditText editText = this.edtSearch;
                    if (editText == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                        editText = null;
                    }
                    editText.setText("");
                }
                updateBreadcrumbs();
                RecyclerView recyclerView2 = this.rvFiles;
                if (recyclerView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                    recyclerView2 = null;
                }
                recyclerView2.scrollToPosition(0);
                loadFiles$default(this, true, false, 2, null);
                return;
            }
            if (Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_VIDEO) || Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_AUDIO)) {
                String streamUrl = NasApiClient.INSTANCE.getVaultStreamUrl(this, item.getVaultId());
                String downloadUrl = NasApiClient.INSTANCE.getVaultDownloadUrl(this, item.getVaultId());
                Intent intent = new Intent(this, (Class<?>) VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, streamUrl);
                intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, item.getName());
                intent.putExtra(VideoPlayerActivity.EXTRA_DOWNLOAD_URL, downloadUrl);
                intent.putExtra(VideoPlayerActivity.EXTRA_SERVER_TYPE, this.isCurrentLan ? "LAN" : "WAN");
                startActivity(intent);
                return;
            }
            if (Intrinsics.areEqual(item.getType(), "image")) {
                showImageViewerDialog(item);
                return;
            }
            RecyclerView recyclerView3 = this.rvFiles;
            if (recyclerView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            } else {
                recyclerView = recyclerView3;
            }
            showItemOptionsMenu(item, recyclerView);
            return;
        }
        if (item.isFolder()) {
            this.currentFolder = item.getPath();
            if (this.currentSearch.length() > 0) {
                this.currentSearch = "";
                EditText editText2 = this.edtSearch;
                if (editText2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                    editText2 = null;
                }
                editText2.setText("");
            }
            updateBreadcrumbs();
            RecyclerView recyclerView4 = this.rvFiles;
            if (recyclerView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                recyclerView4 = null;
            }
            recyclerView4.scrollToPosition(0);
            loadFiles$default(this, true, false, 2, null);
            return;
        }
        if (Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_VIDEO) || Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_AUDIO)) {
            String streamUrl2 = NasApiClient.INSTANCE.getStreamUrl(this, item.getPath());
            String downloadUrl2 = NasApiClient.INSTANCE.getDownloadUrl(this, item.getPath());
            Intent intent2 = new Intent(this, (Class<?>) VideoPlayerActivity.class);
            intent2.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, streamUrl2);
            intent2.putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, item.getName());
            intent2.putExtra(VideoPlayerActivity.EXTRA_DOWNLOAD_URL, downloadUrl2);
            intent2.putExtra(VideoPlayerActivity.EXTRA_SERVER_TYPE, this.isCurrentLan ? "LAN" : "WAN");
            startActivity(intent2);
            return;
        }
        if (Intrinsics.areEqual(item.getType(), "image")) {
            showImageViewerDialog(item);
            return;
        }
        RecyclerView recyclerView5 = this.rvFiles;
        if (recyclerView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
        } else {
            recyclerView = recyclerView5;
        }
        showItemOptionsMenu(item, recyclerView);
    }

    private final void showImageViewerDialog(final NasItem item) {
        ImageView imageView;
        Object imageUrl;
        final Dialog dialog = new Dialog(this, android.R.style.Theme.Black.NoTitleBar.Fullscreen);
        FrameLayout rootLayout = new FrameLayout(this);
        rootLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        final ImageView imageView2 = new ImageView(this);
        imageView2.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView2.setAdjustViewBounds(true);
        imageView2.setClickable(true);
        imageView2.setFocusable(true);
        rootLayout.addView(imageView2);
        final ProgressBar progressBar = new ProgressBar(this);
        int i = (int) (48.0f * progressBar.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i);
        layoutParams.gravity = 17;
        progressBar.setLayoutParams(layoutParams);
        rootLayout.addView(progressBar);
        final LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(0);
        topBar.setGravity(16);
        topBar.setBackgroundColor(Color.parseColor("#99000000"));
        int i2 = (int) (16.0f * topBar.getResources().getDisplayMetrics().density);
        int i3 = (int) (topBar.getResources().getDisplayMetrics().density * 12.0f);
        topBar.setPadding(i2, i3, i2, i3);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        layoutParams2.gravity = 48;
        topBar.setLayoutParams(layoutParams2);
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.ic_close);
        imageView3.setColorFilter(-1);
        int i4 = (int) (imageView3.getResources().getDisplayMetrics().density * 32.0f);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(i4, i4);
        layoutParams3.setMarginEnd((int) (imageView3.getResources().getDisplayMetrics().density * 12.0f));
        imageView3.setLayoutParams(layoutParams3);
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda89
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        topBar.addView(imageView3);
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(1);
        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        TextView textView = new TextView(this);
        textView.setText((item.isVault() ? "🔒 " : "") + item.getName());
        textView.setTextColor(-1);
        textView.setTextSize(15.0f);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        TextView textView2 = new TextView(this);
        textView2.setText(formatFileSize(item.getSize()));
        textView2.setTextColor(Color.parseColor("#CCCCCC"));
        textView2.setTextSize(12.0f);
        titleLayout.addView(textView);
        titleLayout.addView(textView2);
        topBar.addView(titleLayout);
        final ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.ic_fluent_more_vertical_24_regular);
        imageView4.setColorFilter(-1);
        int i5 = (int) (imageView4.getResources().getDisplayMetrics().density * 32.0f);
        imageView4.setLayoutParams(new LinearLayout.LayoutParams(i5, i5));
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda90
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.showItemOptionsMenu(item, imageView4);
            }
        });
        topBar.addView(imageView4);
        rootLayout.addView(topBar);
        final Ref.FloatRef scaleFactor = new Ref.FloatRef();
        scaleFactor.element = 1.0f;
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.nasdrive.app.MainActivity$showImageViewerDialog$scaleDetector$1
            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector detector) {
                Intrinsics.checkNotNullParameter(detector, "detector");
                Ref.FloatRef.this.element *= detector.getScaleFactor();
                Ref.FloatRef.this.element = RangesKt.coerceIn(Ref.FloatRef.this.element, 0.8f, 5.0f);
                imageView2.setScaleX(Ref.FloatRef.this.element);
                imageView2.setScaleY(Ref.FloatRef.this.element);
                return true;
            }
        });
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() { // from class: com.nasdrive.app.MainActivity$showImageViewerDialog$gestureDetector$1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent e) {
                Intrinsics.checkNotNullParameter(e, "e");
                Ref.FloatRef.this.element = Ref.FloatRef.this.element > 1.2f ? 1.0f : 2.5f;
                imageView2.animate().scaleX(Ref.FloatRef.this.element).scaleY(Ref.FloatRef.this.element).translationX(0.0f).translationY(0.0f).setDuration(200L).start();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Intrinsics.checkNotNullParameter(e, "e");
                topBar.setVisibility(topBar.getVisibility() == 0 ? 8 : 0);
                return true;
            }
        });
        imageView2.setOnTouchListener(new View.OnTouchListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda91
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return MainActivity.showImageViewerDialog$lambda$61(scaleDetector, gestureDetector, view, motionEvent);
            }
        });
        if (item.isVault()) {
            imageView = imageView2;
            imageUrl = NasApiClient.INSTANCE.getVaultStreamUrl(this, item.getVaultId());
        } else {
            imageView = imageView2;
            imageUrl = NasApiClient.INSTANCE.getStreamUrl(this, item.getPath());
        }
        progressBar.setVisibility(0);
        Object obj = imageUrl;
        ImageView imageView5 = imageView;
        ImageLoader imageLoader = Coil.imageLoader(imageView5.getContext());
        ImageRequest.Builder target = new ImageRequest.Builder(imageView5.getContext()).data(obj).target(imageView5);
        target.crossfade(true);
        target.listener(new ImageRequest.Listener() { // from class: com.nasdrive.app.MainActivity$showImageViewerDialog$lambda$64$$inlined$listener$default$1
            @Override // coil.request.ImageRequest.Listener
            public void onStart(ImageRequest request) {
            }

            @Override // coil.request.ImageRequest.Listener
            public void onCancel(ImageRequest request) {
            }

            @Override // coil.request.ImageRequest.Listener
            public void onError(ImageRequest request, ErrorResult result) {
                progressBar.setVisibility(8);
                Toast.makeText(this, "Lỗi tải ảnh: " + result.getThrowable().getMessage(), 0).show();
            }

            @Override // coil.request.ImageRequest.Listener
            public void onSuccess(ImageRequest request, SuccessResult result) {
                progressBar.setVisibility(8);
            }
        });
        imageLoader.enqueue(target.build());
        dialog.setContentView(rootLayout);
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean showImageViewerDialog$lambda$61(ScaleGestureDetector $scaleDetector, GestureDetector $gestureDetector, View view, MotionEvent event) {
        $scaleDetector.onTouchEvent(event);
        $gestureDetector.onTouchEvent(event);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showItemOptionsMenu(final NasItem item, View anchor) {
        String[] options;
        if (item.isVault()) {
            if (item.isFolder()) {
                options = new String[]{"📁 Di chuyển tới...", "📤 Chuyển về Drive", "🗑️ Xóa thư mục bí mật"};
            } else {
                options = new String[]{"📁 Di chuyển tới...", "📤 Chuyển về Drive", "📥 Tải xuống máy", "🗑️ Xóa vĩnh viễn"};
            }
            new MaterialAlertDialogBuilder(this).setTitle((CharSequence) ("🔒 " + item.getName())).setItems((CharSequence[]) options, new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda11
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.showItemOptionsMenu$lambda$65(NasItem.this, this, dialogInterface, i);
                }
            }).show();
            return;
        }
        if (item.isFolder()) {
            String[] options2 = {"📁 Di chuyển tới...", "🔒 Chuyển sang Thư mục bí mật", "🗑️ Xóa thư mục"};
            new MaterialAlertDialogBuilder(this).setTitle((CharSequence) ("📁 " + item.getName())).setItems((CharSequence[]) options2, new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda12
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.showItemOptionsMenu$lambda$66(MainActivity.this, item, dialogInterface, i);
                }
            }).show();
        } else {
            String[] options3 = {"📁 Di chuyển tới...", "🔒 Chuyển sang Thư mục bí mật", "📥 Tải xuống máy", "🔗 Chia sẻ liên kết", "🗑️ Xóa tệp"};
            new MaterialAlertDialogBuilder(this).setTitle((CharSequence) item.getName()).setItems((CharSequence[]) options3, new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.showItemOptionsMenu$lambda$67(MainActivity.this, item, dialogInterface, i);
                }
            }).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showItemOptionsMenu$lambda$65(NasItem $item, MainActivity this$0, DialogInterface dialogInterface, int which) {
        if ($item.isFolder()) {
            switch (which) {
                case 0:
                    this$0.showMovePickerBottomSheet($item);
                    return;
                case 1:
                    this$0.confirmExportVault($item);
                    return;
                case 2:
                    this$0.confirmDeleteVaultItem($item);
                    return;
                default:
                    return;
            }
        }
        switch (which) {
            case 0:
                this$0.showMovePickerBottomSheet($item);
                return;
            case 1:
                this$0.confirmExportVault($item);
                return;
            case 2:
                this$0.confirmDownloadVaultItem($item);
                return;
            case 3:
                this$0.confirmDeleteVaultItem($item);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showItemOptionsMenu$lambda$66(MainActivity this$0, NasItem $item, DialogInterface dialogInterface, int which) {
        switch (which) {
            case 0:
                this$0.showMovePickerBottomSheet($item);
                return;
            case 1:
                this$0.moveToVault($item);
                return;
            case 2:
                this$0.confirmDeleteItem($item);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showItemOptionsMenu$lambda$67(MainActivity this$0, NasItem $item, DialogInterface dialogInterface, int which) {
        switch (which) {
            case 0:
                this$0.showMovePickerBottomSheet($item);
                return;
            case 1:
                this$0.moveToVault($item);
                return;
            case 2:
                this$0.startDownload($item);
                return;
            case 3:
                this$0.shareLink($item);
                return;
            case 4:
                this$0.confirmDeleteItem($item);
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Type inference failed for: r0v25, types: [T, com.nasdrive.app.MainActivity$$ExternalSyntheticLambda69] */
    private final void showMovePickerBottomSheet(final NasItem item) {
        String name;
        StringBuilder sb;
        String str;
        Function1 function1;
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_move_picker, (ViewGroup) null);
        sheetDialog.setContentView(sheetView);
        TextView txtTitle = (TextView) sheetView.findViewById(R.id.txtMoveTitle);
        ImageButton btnClose = (ImageButton) sheetView.findViewById(R.id.btnMoveClose);
        final ImageButton btnParent = (ImageButton) sheetView.findViewById(R.id.btnMoveParent);
        final TextView txtPath = (TextView) sheetView.findViewById(R.id.txtMoveCurrentPath);
        final LinearProgressIndicator pbLoading = (LinearProgressIndicator) sheetView.findViewById(R.id.pbMoveLoading);
        final RecyclerView rvFolders = (RecyclerView) sheetView.findViewById(R.id.rvMoveFolders);
        final TextView txtEmpty = (TextView) sheetView.findViewById(R.id.txtMoveEmpty);
        MaterialButton btnNewFolder = (MaterialButton) sheetView.findViewById(R.id.btnMoveNewFolder);
        final MaterialButton btnConfirm = (MaterialButton) sheetView.findViewById(R.id.btnMoveConfirm);
        if (item.isFolder()) {
            name = item.getName();
            sb = new StringBuilder();
            str = "thư mục \"";
        } else {
            name = item.getName();
            sb = new StringBuilder();
            str = "tệp \"";
        }
        final String nameDesc = sb.append(str).append(name).append("\"").toString();
        txtTitle.setText("Di chuyển " + nameDesc);
        final Ref.ObjectRef currentTargetFolder = new Ref.ObjectRef();
        currentTargetFolder.element = "";
        final Ref.ObjectRef currentParentFolder = new Ref.ObjectRef();
        currentParentFolder.element = "";
        final Ref.ObjectRef loadPickerFolders = new Ref.ObjectRef();
        final FolderPickerAdapter folderAdapter = new FolderPickerAdapter(null, new Function1() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.showMovePickerBottomSheet$lambda$68(Ref.ObjectRef.this, (NasFolderItem) obj);
            }
        }, 1, null);
        rvFolders.setLayoutManager(new LinearLayoutManager(this));
        rvFolders.setAdapter(folderAdapter);
        loadPickerFolders.element = new Function1() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda69
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.showMovePickerBottomSheet$lambda$72(Ref.ObjectRef.this, pbLoading, rvFolders, txtEmpty, item, txtPath, btnParent, this, currentParentFolder, folderAdapter, (String) obj);
            }
        };
        btnParent.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda70
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMovePickerBottomSheet$lambda$73(Ref.ObjectRef.this, currentParentFolder, view);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda71
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BottomSheetDialog.this.dismiss();
            }
        });
        btnNewFolder.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda72
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMovePickerBottomSheet$lambda$79(MainActivity.this, currentTargetFolder, item, loadPickerFolders, view);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda73
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showMovePickerBottomSheet$lambda$82(NasItem.this, currentTargetFolder, this, nameDesc, btnConfirm, pbLoading, sheetDialog, view);
            }
        });
        if (loadPickerFolders.element == 0) {
            Intrinsics.throwUninitializedPropertyAccessException("loadPickerFolders");
            function1 = null;
        } else {
            function1 = (Function1) loadPickerFolders.element;
        }
        function1.invoke("");
        sheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit showMovePickerBottomSheet$lambda$68(Ref.ObjectRef $loadPickerFolders, NasFolderItem clickedFolder) {
        Function1 function1;
        Intrinsics.checkNotNullParameter(clickedFolder, "clickedFolder");
        if ($loadPickerFolders.element == 0) {
            Intrinsics.throwUninitializedPropertyAccessException("loadPickerFolders");
            function1 = null;
        } else {
            function1 = (Function1) $loadPickerFolders.element;
        }
        function1.invoke(clickedFolder.getPath());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final Unit showMovePickerBottomSheet$lambda$72(Ref.ObjectRef $currentTargetFolder, final LinearProgressIndicator $pbLoading, final RecyclerView $rvFolders, final TextView $txtEmpty, final NasItem $item, TextView $txtPath, ImageButton $btnParent, final MainActivity this$0, final Ref.ObjectRef $currentParentFolder, final FolderPickerAdapter $folderAdapter, final String folderPath) {
        Intrinsics.checkNotNullParameter(folderPath, "folderPath");
        $currentTargetFolder.element = folderPath;
        $pbLoading.setVisibility(0);
        $rvFolders.setVisibility(4);
        $txtEmpty.setVisibility(8);
        String rootLabel = $item.isVault() ? "Thư mục bí mật" : "Drive của tôi";
        if (folderPath.length() == 0) {
            $txtPath.setText("📁 " + rootLabel);
            $btnParent.setVisibility(8);
        } else {
            $txtPath.setText("📁 " + rootLabel + " / " + folderPath);
            $btnParent.setVisibility(0);
        }
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showMovePickerBottomSheet$lambda$72$lambda$71(MainActivity.this, folderPath, $item, $pbLoading, $rvFolders, $currentParentFolder, $folderAdapter, $txtEmpty);
            }
        });
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$72$lambda$71(final MainActivity this$0, String $folderPath, final NasItem $item, final LinearProgressIndicator $pbLoading, final RecyclerView $rvFolders, final Ref.ObjectRef $currentParentFolder, final FolderPickerAdapter $folderAdapter, final TextView $txtEmpty) {
        final Object res = NasApiClient.INSTANCE.m367fetchFolders0E7RQCE(this$0, $folderPath, $item.isVault());
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showMovePickerBottomSheet$lambda$72$lambda$71$lambda$70(LinearProgressIndicator.this, $rvFolders, res, $currentParentFolder, $folderAdapter, $txtEmpty, this$0, $item);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0084, code lost:
    
        if (kotlin.text.StringsKt.startsWith$default(r14.getPath(), r25.getPath() + "/", false, 2, (java.lang.Object) null) == false) goto L22;
     */
    /* JADX WARN: Type inference failed for: r6v0, types: [T, java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void showMovePickerBottomSheet$lambda$72$lambda$71$lambda$70(com.google.android.material.progressindicator.LinearProgressIndicator r18, androidx.recyclerview.widget.RecyclerView r19, java.lang.Object r20, kotlin.jvm.internal.Ref.ObjectRef r21, com.nasdrive.app.FolderPickerAdapter r22, android.widget.TextView r23, com.nasdrive.app.MainActivity r24, com.nasdrive.app.NasItem r25) {
        /*
            r0 = 8
            r1 = r18
            r1.setVisibility(r0)
            r2 = 0
            r3 = r19
            r3.setVisibility(r2)
            boolean r4 = kotlin.Result.m389isSuccessimpl(r20)
            if (r4 == 0) goto Lb5
            boolean r4 = kotlin.Result.m388isFailureimpl(r20)
            if (r4 == 0) goto L1b
            r4 = 0
            goto L1d
        L1b:
            r4 = r20
        L1d:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            com.nasdrive.app.FolderListResult r4 = (com.nasdrive.app.FolderListResult) r4
            java.lang.String r6 = r4.getParentFolder()
            r7 = r21
            r7.element = r6
            java.util.List r6 = r4.getFolders()
            java.lang.Iterable r6 = (java.lang.Iterable) r6
            r8 = 0
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.Collection r9 = (java.util.Collection) r9
            r10 = r6
            r11 = 0
            java.util.Iterator r12 = r10.iterator()
        L3e:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L9a
            java.lang.Object r13 = r12.next()
            r14 = r13
            com.nasdrive.app.NasFolderItem r14 = (com.nasdrive.app.NasFolderItem) r14
            r15 = 0
            boolean r16 = r25.isFolder()
            r17 = 1
            if (r16 == 0) goto L8b
            java.lang.String r0 = r14.getPath()
            java.lang.String r2 = r25.getPath()
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r2)
            if (r0 != 0) goto L87
            java.lang.String r0 = r14.getPath()
            java.lang.String r2 = r25.getPath()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.StringBuilder r2 = r5.append(r2)
            java.lang.String r5 = "/"
            java.lang.StringBuilder r2 = r2.append(r5)
            java.lang.String r2 = r2.toString()
            r5 = 2
            r1 = 0
            r3 = 0
            boolean r0 = kotlin.text.StringsKt.startsWith$default(r0, r2, r3, r5, r1)
            if (r0 != 0) goto L88
            goto L8c
        L87:
            r1 = 0
        L88:
            r17 = 0
            goto L8c
        L8b:
            r1 = 0
        L8c:
            if (r17 == 0) goto L92
            r9.add(r13)
        L92:
            r0 = 8
            r2 = 0
            r1 = r18
            r3 = r19
            goto L3e
        L9a:
            r0 = r9
            java.util.List r0 = (java.util.List) r0
            r2 = r22
            r2.submitList(r0)
            boolean r1 = r0.isEmpty()
            if (r1 == 0) goto Lad
            r1 = 0
            goto Laf
        Lad:
            r1 = 8
        Laf:
            r3 = r23
            r3.setVisibility(r1)
            goto Le9
        Lb5:
            r7 = r21
            r2 = r22
            r3 = r23
            r1 = 0
            r0 = r24
            android.content.Context r0 = (android.content.Context) r0
            java.lang.Throwable r4 = kotlin.Result.m385exceptionOrNullimpl(r20)
            if (r4 == 0) goto Lcb
            java.lang.String r5 = r4.getMessage()
            goto Lcc
        Lcb:
            r5 = r1
        Lcc:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "Không thể tải danh sách thư mục: "
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r1 = r1.toString()
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            r4 = 0
            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r1, r4)
            r0.show()
        Le9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.MainActivity.showMovePickerBottomSheet$lambda$72$lambda$71$lambda$70(com.google.android.material.progressindicator.LinearProgressIndicator, androidx.recyclerview.widget.RecyclerView, java.lang.Object, kotlin.jvm.internal.Ref$ObjectRef, com.nasdrive.app.FolderPickerAdapter, android.widget.TextView, com.nasdrive.app.MainActivity, com.nasdrive.app.NasItem):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$73(Ref.ObjectRef $loadPickerFolders, Ref.ObjectRef $currentParentFolder, View it) {
        Function1 function1;
        if ($loadPickerFolders.element == 0) {
            Intrinsics.throwUninitializedPropertyAccessException("loadPickerFolders");
            function1 = null;
        } else {
            function1 = (Function1) $loadPickerFolders.element;
        }
        function1.invoke($currentParentFolder.element);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$79(final MainActivity this$0, final Ref.ObjectRef $currentTargetFolder, final NasItem $item, final Ref.ObjectRef $loadPickerFolders, View it) {
        final EditText editText = new EditText(this$0);
        editText.setHint("Tên thư mục mới");
        int i = (int) (16.0f * editText.getResources().getDisplayMetrics().density);
        editText.setPadding(i, i, i, i);
        new MaterialAlertDialogBuilder(this$0).setTitle((CharSequence) "Tạo thư mục mới").setView((View) editText).setPositiveButton((CharSequence) "Tạo", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda105
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showMovePickerBottomSheet$lambda$79$lambda$78(editText, this$0, $currentTargetFolder, $item, $loadPickerFolders, dialogInterface, i2);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$79$lambda$78(EditText $input, final MainActivity this$0, final Ref.ObjectRef $currentTargetFolder, final NasItem $item, final Ref.ObjectRef $loadPickerFolders, DialogInterface dialogInterface, int i) {
        final String name = StringsKt.trim((CharSequence) $input.getText().toString()).toString();
        if (name.length() > 0) {
            this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.showMovePickerBottomSheet$lambda$79$lambda$78$lambda$77(MainActivity.this, $currentTargetFolder, name, $item, $loadPickerFolders);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final void showMovePickerBottomSheet$lambda$79$lambda$78$lambda$77(final MainActivity this$0, final Ref.ObjectRef $currentTargetFolder, final String $name, NasItem $item, final Ref.ObjectRef $loadPickerFolders) {
        final Object createRes = NasApiClient.INSTANCE.m362createFolderBWLJW6A(this$0, (String) $currentTargetFolder.element, $name, $item.isVault());
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showMovePickerBottomSheet$lambda$79$lambda$78$lambda$77$lambda$76(createRes, this$0, $name, $loadPickerFolders, $currentTargetFolder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$79$lambda$78$lambda$77$lambda$76(Object $createRes, MainActivity this$0, String $name, Ref.ObjectRef $loadPickerFolders, Ref.ObjectRef $currentTargetFolder) {
        Function1 function1;
        if (Result.m389isSuccessimpl($createRes)) {
            Toast.makeText(this$0, "Đã tạo thư mục: " + $name, 0).show();
            if ($loadPickerFolders.element == 0) {
                Intrinsics.throwUninitializedPropertyAccessException("loadPickerFolders");
                function1 = null;
            } else {
                function1 = (Function1) $loadPickerFolders.element;
            }
            function1.invoke($currentTargetFolder.element);
            return;
        }
        Toast.makeText(this$0, "Lỗi tạo thư mục", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$82(final NasItem $item, final Ref.ObjectRef $currentTargetFolder, final MainActivity this$0, String $nameDesc, final MaterialButton $btnConfirm, final LinearProgressIndicator $pbLoading, final BottomSheetDialog $sheetDialog, View it) {
        String itemParent = StringsKt.contains$default((CharSequence) $item.getPath(), '/', false, 2, (Object) null) ? StringsKt.substringBeforeLast$default($item.getPath(), '/', (String) null, 2, (Object) null) : "";
        if (Intrinsics.areEqual($currentTargetFolder.element, itemParent)) {
            Toast.makeText(this$0, $nameDesc + " đã nằm trong thư mục này rồi", 0).show();
            return;
        }
        $btnConfirm.setEnabled(false);
        $btnConfirm.setText("Đang di chuyển...");
        $pbLoading.setVisibility(0);
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showMovePickerBottomSheet$lambda$82$lambda$81(MainActivity.this, $item, $currentTargetFolder, $sheetDialog, $btnConfirm, $pbLoading);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static final void showMovePickerBottomSheet$lambda$82$lambda$81(final MainActivity this$0, NasItem $item, Ref.ObjectRef $currentTargetFolder, final BottomSheetDialog $sheetDialog, final MaterialButton $btnConfirm, final LinearProgressIndicator $pbLoading) {
        final Object moveRes = NasApiClient.INSTANCE.m374moveItemsBWLJW6A(this$0, CollectionsKt.listOf($item.getPath()), (String) $currentTargetFolder.element, $item.isVault());
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showMovePickerBottomSheet$lambda$82$lambda$81$lambda$80(moveRes, $sheetDialog, this$0, $btnConfirm, $pbLoading);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showMovePickerBottomSheet$lambda$82$lambda$81$lambda$80(Object $moveRes, BottomSheetDialog $sheetDialog, MainActivity this$0, MaterialButton $btnConfirm, LinearProgressIndicator $pbLoading) {
        String errMsg;
        if (Result.m389isSuccessimpl($moveRes)) {
            Object obj = Result.m388isFailureimpl($moveRes) ? null : $moveRes;
            Intrinsics.checkNotNull(obj);
            MoveResult result = (MoveResult) obj;
            $sheetDialog.dismiss();
            this$0.vibrate();
            if (result.getMovedCount() > 0) {
                Toast.makeText(this$0, "Đã di chuyển thành công!", 0).show();
            } else if (!result.getErrors().isEmpty()) {
                Toast.makeText(this$0, (CharSequence) CollectionsKt.first((List) result.getErrors()), 1).show();
            }
            this$0.folderCache.evictAll();
            this$0.loadFiles(false, true);
            return;
        }
        $btnConfirm.setEnabled(true);
        $btnConfirm.setText("Di chuyển vào đây");
        $pbLoading.setVisibility(8);
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($moveRes);
        if (m385exceptionOrNullimpl == null || (errMsg = m385exceptionOrNullimpl.getMessage()) == null) {
            errMsg = "Lỗi di chuyển tệp";
        }
        Toast.makeText(this$0, errMsg, 1).show();
    }

    private final void confirmDownloadVaultItem(final NasItem item) {
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "📥 Tải xuống máy").setMessage((CharSequence) ("Tệp bí mật \"" + item.getName() + "\" sẽ được tải về thư mục Downloads của điện thoại (không còn được bảo mật bởi mật khẩu Thư mục bí mật).\n\nBạn có chắc chắn muốn tải?")).setPositiveButton((CharSequence) "Tải xuống", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda25
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.startDownload(item);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    private final void moveToVault(NasItem item) {
        StringBuilder append;
        String str;
        final List targetPaths = CollectionsKt.listOf(item.getPath());
        if (item.isFolder()) {
            append = new StringBuilder().append("thư mục \"").append(item.getName());
            str = "\" và toàn bộ tệp bên trong";
        } else {
            append = new StringBuilder().append("tệp \"").append(item.getName());
            str = "\"";
        }
        String nameDesc = append.append(str).toString();
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "🔒 Chuyển sang Thư mục bí mật").setMessage((CharSequence) ("Bạn có chắc muốn chuyển " + nameDesc + " sang Thư mục bí mật?\n\nTệp/thư mục này sẽ được ẩn khỏi Drive và bảo vệ an toàn bằng mật khẩu Thư mục bí mật.")).setPositiveButton((CharSequence) "Chuyển ngay", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda75
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.checkVaultAndImport(targetPaths);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkVaultAndImport(final List<String> paths) {
        LinearProgressIndicator linearProgressIndicator = this.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda99
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.checkVaultAndImport$lambda$90(MainActivity.this, paths);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void checkVaultAndImport$lambda$90(final MainActivity this$0, final List $paths) {
        final Object statusRes = NasApiClient.INSTANCE.m370fetchVaultStatusIoAF18A(this$0);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.checkVaultAndImport$lambda$90$lambda$89(MainActivity.this, statusRes, $paths);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void checkVaultAndImport$lambda$90$lambda$89(final MainActivity this$0, Object $statusRes, final List $paths) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($statusRes)) {
            VaultStatus vaultStatus = (VaultStatus) $statusRes;
            if (!vaultStatus.isConfigured()) {
                this$0.showVaultSetupDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda47
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.checkVaultAndImport$lambda$90$lambda$89$lambda$87$lambda$85(MainActivity.this, $paths);
                    }
                });
            } else if (!vaultStatus.isUnlocked()) {
                this$0.showVaultUnlockDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda48
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.checkVaultAndImport$lambda$90$lambda$89$lambda$87$lambda$86(MainActivity.this, $paths);
                    }
                });
            } else {
                this$0.executeVaultImport($paths);
            }
        }
        if (Result.m385exceptionOrNullimpl($statusRes) != null) {
            this$0.executeVaultImport($paths);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit checkVaultAndImport$lambda$90$lambda$89$lambda$87$lambda$85(MainActivity this$0, List $paths) {
        this$0.executeVaultImport($paths);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit checkVaultAndImport$lambda$90$lambda$89$lambda$87$lambda$86(MainActivity this$0, List $paths) {
        this$0.executeVaultImport($paths);
        return Unit.INSTANCE;
    }

    private final void executeVaultImport(final List<String> paths) {
        LinearProgressIndicator linearProgressIndicator = this.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.executeVaultImport$lambda$95(MainActivity.this, paths);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void executeVaultImport$lambda$95(final MainActivity this$0, final List $paths) {
        final Object res = NasApiClient.INSTANCE.m371importToVaultgIAlus(this$0, $paths);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.executeVaultImport$lambda$95$lambda$94(MainActivity.this, res, $paths);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void executeVaultImport$lambda$95$lambda$94(final MainActivity this$0, Object $res, final List $paths) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            Toast.makeText(this$0, "🔒 Đã chuyển thành công " + ((Number) $res).intValue() + " mục vào Thư mục bí mật!", 1).show();
            this$0.folderCache.evictAll();
            this$0.loadFiles(false, true);
            this$0.loadStorage();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            if (m385exceptionOrNullimpl instanceof VaultLockedException) {
                this$0.showVaultUnlockDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.executeVaultImport$lambda$95$lambda$94$lambda$93$lambda$92(MainActivity.this, $paths);
                    }
                });
            } else {
                Toast.makeText(this$0, "Lỗi chuyển tệp: " + m385exceptionOrNullimpl.getMessage(), 1).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit executeVaultImport$lambda$95$lambda$94$lambda$93$lambda$92(MainActivity this$0, List $paths) {
        this$0.executeVaultImport($paths);
        return Unit.INSTANCE;
    }

    private final void confirmExportVault(final NasItem item) {
        String name;
        StringBuilder sb;
        String str;
        if (item.isFolder()) {
            name = item.getName();
            sb = new StringBuilder();
            str = "thư mục \"";
        } else {
            name = item.getName();
            sb = new StringBuilder();
            str = "tệp \"";
        }
        final String nameDesc = sb.append(str).append(name).append("\"").toString();
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "📤 Chuyển về Drive").setMessage((CharSequence) ("Chuyển " + nameDesc + " về lại thư mục Drive?")).setPositiveButton((CharSequence) "Chuyển về", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda63
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.confirmExportVault$lambda$100(MainActivity.this, item, nameDesc, dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmExportVault$lambda$100(final MainActivity this$0, final NasItem $item, final String $nameDesc, DialogInterface dialogInterface, int i) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.confirmExportVault$lambda$100$lambda$99(MainActivity.this, $item, $nameDesc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmExportVault$lambda$100$lambda$99(final MainActivity this$0, NasItem $item, final String $nameDesc) {
        final Object res = NasApiClient.m357exportFromVault0E7RQCE$default(NasApiClient.INSTANCE, this$0, CollectionsKt.listOf($item.getVaultId()), null, 4, null);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.confirmExportVault$lambda$100$lambda$99$lambda$98(MainActivity.this, res, $nameDesc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmExportVault$lambda$100$lambda$99$lambda$98(MainActivity this$0, Object $res, String $nameDesc) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            ((Number) $res).intValue();
            Toast.makeText(this$0, "Đã chuyển " + $nameDesc + " về Drive thành công!", 0).show();
            this$0.loadFiles(false, true);
            this$0.loadStorage();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            Toast.makeText(this$0, "Lỗi chuyển về Drive: " + m385exceptionOrNullimpl.getMessage(), 1).show();
        }
    }

    private final void confirmDeleteVaultItem(final NasItem item) {
        String name;
        StringBuilder sb;
        String str;
        if (item.isFolder()) {
            name = item.getName();
            sb = new StringBuilder();
            str = "thư mục \"";
        } else {
            name = item.getName();
            sb = new StringBuilder();
            str = "tệp \"";
        }
        final String nameDesc = sb.append(str).append(name).append("\"").toString();
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "🗑️ Xóa vĩnh viễn").setMessage((CharSequence) ("Bạn có chắc muốn xóa vĩnh viễn " + nameDesc + "? Dữ liệu sẽ bị xóa hoàn toàn khỏi Thư mục bí mật.")).setPositiveButton((CharSequence) "Xóa vĩnh viễn", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda61
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.confirmDeleteVaultItem$lambda$105(MainActivity.this, item, nameDesc, dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmDeleteVaultItem$lambda$105(final MainActivity this$0, final NasItem $item, final String $nameDesc, DialogInterface dialogInterface, int i) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.confirmDeleteVaultItem$lambda$105$lambda$104(MainActivity.this, $item, $nameDesc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmDeleteVaultItem$lambda$105$lambda$104(final MainActivity this$0, NasItem $item, final String $nameDesc) {
        final Object res = NasApiClient.INSTANCE.m364deleteVaultItemgIAlus(this$0, $item.getVaultId());
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.confirmDeleteVaultItem$lambda$105$lambda$104$lambda$103(MainActivity.this, res, $nameDesc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmDeleteVaultItem$lambda$105$lambda$104$lambda$103(MainActivity this$0, Object $res, String $nameDesc) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            ((Boolean) $res).booleanValue();
            Toast.makeText(this$0, "Đã xóa " + $nameDesc + " khỏi Thư mục bí mật", 0).show();
            this$0.loadFiles(false, true);
            this$0.loadStorage();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            Toast.makeText(this$0, "Lỗi xóa: " + m385exceptionOrNullimpl.getMessage(), 1).show();
        }
    }

    private final void showVaultSetupDialog(final Function0<Unit> onSuccess) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(1);
        int i = (int) (24.0f * container.getResources().getDisplayMetrics().density);
        container.setPadding(i, i / 2, i, 0);
        final EditText editText = new EditText(this);
        editText.setHint("Tạo mật khẩu Két sắt (tối thiểu 6 ký tự)");
        editText.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        editText.setSingleLine();
        final EditText editText2 = new EditText(this);
        editText2.setHint("Xác nhận lại mật khẩu");
        editText2.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        editText2.setSingleLine();
        container.addView(editText);
        container.addView(editText2);
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "🔒 Thiết lập Thư mục bí mật").setMessage((CharSequence) "Tạo mật khẩu riêng để bảo vệ chìa khóa mã hóa AES-256-GCM. Hãy ghi nhớ thật kỹ mật khẩu này!").setView((View) container).setPositiveButton((CharSequence) "Khởi tạo", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda66
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showVaultSetupDialog$lambda$113(editText, editText2, this, onSuccess, dialogInterface, i2);
            }
        }).setNegativeButton((CharSequence) "Hủy", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda67
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showVaultSetupDialog$lambda$114(MainActivity.this, dialogInterface, i2);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultSetupDialog$lambda$113(EditText $edtPass, EditText $edtConfirm, final MainActivity this$0, final Function0 $onSuccess, DialogInterface dialogInterface, int i) {
        final String p1 = StringsKt.trim((CharSequence) $edtPass.getText().toString()).toString();
        String p2 = StringsKt.trim((CharSequence) $edtConfirm.getText().toString()).toString();
        if (p1.length() < 6) {
            Toast.makeText(this$0, "Mật khẩu phải từ 6 ký tự trở lên", 0).show();
            this$0.showVaultSetupDialog($onSuccess);
        } else {
            if (!Intrinsics.areEqual(p1, p2)) {
                Toast.makeText(this$0, "Xác nhận mật khẩu không trùng khớp", 0).show();
                this$0.showVaultSetupDialog($onSuccess);
                return;
            }
            LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
            if (linearProgressIndicator == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
                linearProgressIndicator = null;
            }
            linearProgressIndicator.setVisibility(0);
            this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda87
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.showVaultSetupDialog$lambda$113$lambda$112(MainActivity.this, p1, $onSuccess);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultSetupDialog$lambda$113$lambda$112(final MainActivity this$0, String $p1, final Function0 $onSuccess) {
        final Object res = NasApiClient.INSTANCE.m376setupVaultgIAlus(this$0, $p1);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showVaultSetupDialog$lambda$113$lambda$112$lambda$111(MainActivity.this, res, $onSuccess);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultSetupDialog$lambda$113$lambda$112$lambda$111(MainActivity this$0, Object $res, Function0 $onSuccess) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            ((Boolean) $res).booleanValue();
            Toast.makeText(this$0, "Đã thiết lập Thư mục bí mật thành công!", 0).show();
            $onSuccess.invoke();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            Toast.makeText(this$0, "Lỗi: " + m385exceptionOrNullimpl.getMessage(), 1).show();
            this$0.showVaultSetupDialog($onSuccess);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultSetupDialog$lambda$114(MainActivity this$0, DialogInterface dialogInterface, int i) {
        if (this$0.isVaultMode) {
            this$0.isVaultMode = false;
            this$0.updateBreadcrumbs();
            loadFiles$default(this$0, true, false, 2, null);
        }
    }

    private final void showVaultUnlockDialog(final Function0<Unit> onSuccess) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(1);
        int i = (int) (24.0f * container.getResources().getDisplayMetrics().density);
        container.setPadding(i, i / 2, i, 0);
        final EditText editText = new EditText(this);
        editText.setHint("Nhập mật khẩu Thư mục bí mật");
        editText.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        editText.setSingleLine();
        container.addView(editText);
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "🔒 Mở khóa Thư mục bí mật").setMessage((CharSequence) "Dữ liệu được mã hóa chuẩn quân sự AES-256-GCM. Nhập mật khẩu để tiếp tục.").setView((View) container).setPositiveButton((CharSequence) "Mở khóa", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda101
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showVaultUnlockDialog$lambda$121(editText, this, onSuccess, dialogInterface, i2);
            }
        }).setNegativeButton((CharSequence) "Hủy", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda102
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showVaultUnlockDialog$lambda$122(MainActivity.this, dialogInterface, i2);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockDialog$lambda$121(EditText $edtPass, final MainActivity this$0, final Function0 $onSuccess, DialogInterface dialogInterface, int i) {
        final String p = StringsKt.trim((CharSequence) $edtPass.getText().toString()).toString();
        if (p.length() == 0) {
            Toast.makeText(this$0, "Vui lòng nhập mật khẩu", 0).show();
            this$0.showVaultUnlockDialog($onSuccess);
            return;
        }
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda100
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showVaultUnlockDialog$lambda$121$lambda$120(MainActivity.this, p, $onSuccess);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockDialog$lambda$121$lambda$120(final MainActivity this$0, String $p, final Function0 $onSuccess) {
        final Object res = NasApiClient.INSTANCE.m377unlockVaultgIAlus(this$0, $p);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showVaultUnlockDialog$lambda$121$lambda$120$lambda$119(MainActivity.this, res, $onSuccess);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockDialog$lambda$121$lambda$120$lambda$119(MainActivity this$0, Object $res, Function0 $onSuccess) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            ((Boolean) $res).booleanValue();
            Toast.makeText(this$0, "Đã mở khóa Thư mục bí mật!", 0).show();
            $onSuccess.invoke();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            Toast.makeText(this$0, "Mật khẩu không đúng: " + m385exceptionOrNullimpl.getMessage(), 1).show();
            this$0.showVaultUnlockDialog($onSuccess);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockDialog$lambda$122(MainActivity this$0, DialogInterface dialogInterface, int i) {
        if (this$0.isVaultMode) {
            this$0.isVaultMode = false;
            this$0.updateBreadcrumbs();
            loadFiles$default(this$0, true, false, 2, null);
        }
    }

    private final void showVaultUnlockPrompt() {
        LinearProgressIndicator linearProgressIndicator = this.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(0);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showVaultUnlockPrompt$lambda$129(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockPrompt$lambda$129(final MainActivity this$0) {
        final Object statusRes = NasApiClient.INSTANCE.m370fetchVaultStatusIoAF18A(this$0);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showVaultUnlockPrompt$lambda$129$lambda$128(MainActivity.this, statusRes);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showVaultUnlockPrompt$lambda$129$lambda$128(final MainActivity this$0, Object $statusRes) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($statusRes)) {
            if (!((VaultStatus) $statusRes).isConfigured()) {
                this$0.showVaultSetupDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda84
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.showVaultUnlockPrompt$lambda$129$lambda$128$lambda$125$lambda$123(MainActivity.this);
                    }
                });
            } else {
                this$0.showVaultUnlockDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda85
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.showVaultUnlockPrompt$lambda$129$lambda$128$lambda$125$lambda$124(MainActivity.this);
                    }
                });
            }
        }
        if (Result.m385exceptionOrNullimpl($statusRes) != null) {
            this$0.showVaultUnlockDialog(new Function0() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda86
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.showVaultUnlockPrompt$lambda$129$lambda$128$lambda$127$lambda$126(MainActivity.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit showVaultUnlockPrompt$lambda$129$lambda$128$lambda$125$lambda$123(MainActivity this$0) {
        this$0.loadFiles(true, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit showVaultUnlockPrompt$lambda$129$lambda$128$lambda$125$lambda$124(MainActivity this$0) {
        this$0.loadFiles(true, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit showVaultUnlockPrompt$lambda$129$lambda$128$lambda$127$lambda$126(MainActivity this$0) {
        this$0.loadFiles(true, true);
        return Unit.INSTANCE;
    }

    private final void showLoginDialog(final Function0<Unit> onSuccess) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(1);
        int i = (int) (24.0f * container.getResources().getDisplayMetrics().density);
        container.setPadding(i, i / 2, i, 0);
        final EditText editText = new EditText(this);
        editText.setHint("Tên đăng nhập (mặc định: admin)");
        editText.setText(ServerConfig.INSTANCE.getUsername(this));
        editText.setSingleLine();
        final EditText editText2 = new EditText(this);
        editText2.setHint("Mật khẩu (mặc định: Admin@NAS2026!)");
        editText2.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        editText2.setSingleLine();
        container.addView(editText);
        container.addView(editText2);
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "🔐 Đăng nhập máy chủ NAS").setMessage((CharSequence) "Nhập thông tin tài khoản để truy cập dữ liệu và Két sắt bí mật.").setView((View) container).setCancelable(false).setPositiveButton((CharSequence) "Đăng nhập", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showLoginDialog$lambda$137(editText, editText2, this, onSuccess, dialogInterface, i2);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showLoginDialog$lambda$137(EditText $edtUser, EditText $edtPass, final MainActivity this$0, final Function0 $onSuccess, DialogInterface dialogInterface, int i) {
        final String u = StringsKt.trim((CharSequence) $edtUser.getText().toString()).toString();
        final String p = $edtPass.getText().toString();
        if (!(u.length() == 0)) {
            if (!(p.length() == 0)) {
                LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
                if (linearProgressIndicator == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
                    linearProgressIndicator = null;
                }
                linearProgressIndicator.setVisibility(0);
                this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda107
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.showLoginDialog$lambda$137$lambda$136(MainActivity.this, u, p, $onSuccess);
                    }
                });
                return;
            }
        }
        Toast.makeText(this$0, "Vui lòng nhập tài khoản và mật khẩu", 0).show();
        this$0.showLoginDialog($onSuccess);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showLoginDialog$lambda$137$lambda$136(final MainActivity this$0, String $u, String $p, final Function0 $onSuccess) {
        final Object res = NasApiClient.INSTANCE.m373login0E7RQCE(this$0, $u, $p);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showLoginDialog$lambda$137$lambda$136$lambda$135(MainActivity.this, res, $onSuccess);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showLoginDialog$lambda$137$lambda$136$lambda$135(MainActivity this$0, Object $res, Function0 $onSuccess) {
        LinearProgressIndicator linearProgressIndicator = this$0.pageProgressBar;
        if (linearProgressIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageProgressBar");
            linearProgressIndicator = null;
        }
        linearProgressIndicator.setVisibility(8);
        if (Result.m389isSuccessimpl($res)) {
            Toast.makeText(this$0, "Đăng nhập thành công!", 0).show();
            $onSuccess.invoke();
        }
        Throwable m385exceptionOrNullimpl = Result.m385exceptionOrNullimpl($res);
        if (m385exceptionOrNullimpl != null) {
            Toast.makeText(this$0, "Lỗi đăng nhập: " + m385exceptionOrNullimpl.getMessage(), 1).show();
            this$0.showLoginDialog($onSuccess);
        }
    }

    private final void openVaultView() {
        this.isVaultMode = true;
        this.currentFolder = "";
        this.currentSearch = "";
        EditText editText = this.edtSearch;
        RecyclerView recyclerView = null;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
            editText = null;
        }
        editText.setText("");
        RecyclerView recyclerView2 = this.rvFiles;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
        } else {
            recyclerView = recyclerView2;
        }
        recyclerView.scrollToPosition(0);
        loadFiles(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startDownload(NasItem item) {
        String url;
        if (item.isVault()) {
            url = NasApiClient.INSTANCE.getVaultDownloadUrl(this, item.getVaultId());
        } else {
            url = NasApiClient.INSTANCE.getDownloadUrl(this, item.getPath());
        }
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(item.getName());
            request.setDescription(item.isVault() ? "Đang tải từ Thư mục bí mật" : "Đang tải từ NAS Drive");
            request.setNotificationVisibility(1);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, item.getName());
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            Object systemService = getSystemService("download");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.DownloadManager");
            DownloadManager dm = (DownloadManager) systemService;
            dm.enqueue(request);
            Toast.makeText(this, "Bắt đầu tải xuống: " + item.getName(), 0).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tải xuống: " + e.getMessage(), 0).show();
        }
    }

    private final void shareLink(NasItem item) {
        String url;
        if (item.isVault()) {
            url = NasApiClient.INSTANCE.getVaultStreamUrl(this, item.getVaultId());
        } else {
            url = NasApiClient.INSTANCE.getDownloadUrl(this, item.getPath());
        }
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("text/plain");
        shareIntent.putExtra("android.intent.extra.SUBJECT", item.getName());
        shareIntent.putExtra("android.intent.extra.TEXT", item.getName() + "\n" + url);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ tệp qua"));
    }

    private final void confirmDeleteItem(final NasItem item) {
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "Xác nhận xóa").setMessage((CharSequence) ("Bạn có chắc muốn chuyển \"" + item.getName() + "\" vào thùng rác?")).setPositiveButton((CharSequence) "Xóa", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda18
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                r0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda98
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.confirmDeleteItem$lambda$142$lambda$141(MainActivity.this, r2);
                    }
                });
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmDeleteItem$lambda$142$lambda$141(final MainActivity this$0, NasItem $item) {
        final Object res = NasApiClient.INSTANCE.m363deleteItemgIAlus(this$0, $item.getPath());
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.confirmDeleteItem$lambda$142$lambda$141$lambda$140(res, this$0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void confirmDeleteItem$lambda$142$lambda$141$lambda$140(Object $res, MainActivity this$0) {
        if (Result.m389isSuccessimpl($res)) {
            Toast.makeText(this$0, "Đã chuyển vào thùng rác", 0).show();
            this$0.folderCache.evictAll();
            this$0.loadFiles(false, true);
            this$0.loadStorage();
            return;
        }
        Toast.makeText(this$0, "Lỗi xóa tệp", 0).show();
    }

    private final void showNewFolderDialog() {
        final EditText editText = new EditText(this);
        editText.setHint("Tên thư mục mới");
        editText.setSingleLine(true);
        int i = (int) (20.0f * editText.getResources().getDisplayMetrics().density);
        editText.setPadding(i, i, i, i);
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "Tạo thư mục mới").setView((View) editText).setPositiveButton((CharSequence) "Tạo", new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda20
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                MainActivity.showNewFolderDialog$lambda$146(editText, this, dialogInterface, i2);
            }
        }).setNegativeButton((CharSequence) "Hủy", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showNewFolderDialog$lambda$146(EditText $input, final MainActivity this$0, DialogInterface dialogInterface, int i) {
        final String name = StringsKt.trim((CharSequence) $input.getText().toString()).toString();
        if (name.length() > 0) {
            this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda106
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.showNewFolderDialog$lambda$146$lambda$145(MainActivity.this, name);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showNewFolderDialog$lambda$146$lambda$145(final MainActivity this$0, final String $name) {
        final Object res = NasApiClient.m356createFolderBWLJW6A$default(NasApiClient.INSTANCE, this$0, this$0.currentFolder, $name, false, 8, null);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showNewFolderDialog$lambda$146$lambda$145$lambda$144(res, this$0, $name);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showNewFolderDialog$lambda$146$lambda$145$lambda$144(Object $res, MainActivity this$0, String $name) {
        if (Result.m389isSuccessimpl($res)) {
            Toast.makeText(this$0, "Đã tạo thư mục: " + $name, 0).show();
            this$0.folderCache.evictAll();
            this$0.loadFiles(false, true);
            return;
        }
        Toast.makeText(this$0, "Lỗi tạo thư mục", 0).show();
    }

    private final void showSortDialog() {
        String[] sortOptions = {"Tên (A-Z)", "Tên (Z-A)", "Mới nhất", "Cũ nhất", "Dung lượng (Lớn nhất)", "Dung lượng (Nhỏ nhất)"};
        new MaterialAlertDialogBuilder(this).setTitle((CharSequence) "Sắp xếp theo").setItems((CharSequence[]) sortOptions, new DialogInterface.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda22
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.showSortDialog$lambda$147(MainActivity.this, dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showSortDialog$lambda$147(MainActivity this$0, DialogInterface dialogInterface, int which) {
        switch (which) {
            case 0:
                this$0.currentSort = "name";
                this$0.currentOrder = "asc";
                break;
            case 1:
                this$0.currentSort = "name";
                this$0.currentOrder = "desc";
                break;
            case 2:
                this$0.currentSort = "date";
                this$0.currentOrder = "desc";
                break;
            case 3:
                this$0.currentSort = "date";
                this$0.currentOrder = "asc";
                break;
            case 4:
                this$0.currentSort = "size";
                this$0.currentOrder = "desc";
                break;
            case 5:
                this$0.currentSort = "size";
                this$0.currentOrder = "asc";
                break;
        }
        this$0.loadFiles(true, false);
    }

    private final void launchCameraCapture() {
        try {
            File photoFile = new File(getCacheDir(), "camera_" + System.currentTimeMillis() + ".jpg");
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            this.cameraTempUri = uri;
            ActivityResultLauncher<Uri> activityResultLauncher = this.cameraCaptureLauncher;
            Intrinsics.checkNotNull(uri);
            activityResultLauncher.launch(uri);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở máy ảnh: " + e.getMessage(), 0).show();
        }
    }

    private final void uploadFiles(final List<? extends Uri> uris) {
        final String uploadServer = NasApiClient.INSTANCE.getBaseUrl(this);
        LinearLayout linearLayout = this.uploadProgressCard;
        ProgressBar progressBar = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uploadProgressCard");
            linearLayout = null;
        }
        linearLayout.setVisibility(0);
        ProgressBar progressBar2 = this.pbUploadProgress;
        if (progressBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pbUploadProgress");
        } else {
            progressBar = progressBar2;
        }
        progressBar.setProgress(0);
        this.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.uploadFiles$lambda$153(uris, this, uploadServer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0176, code lost:
    
        if (r0 == null) goto L48;
     */
    /* JADX WARN: Type inference failed for: r0v17, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v89, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void uploadFiles$lambda$153(java.util.List r33, final com.nasdrive.app.MainActivity r34, java.lang.String r35) {
        /*
            Method dump skipped, instructions count: 692
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.MainActivity.uploadFiles$lambda$153(java.util.List, com.nasdrive.app.MainActivity, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void uploadFiles$lambda$153$lambda$148(MainActivity this$0, int $index, int $total, String $fileName) {
        TextView textView = this$0.txtUploadFileName;
        TextView textView2 = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtUploadFileName");
            textView = null;
        }
        textView.setText("Đang tải (" + ($index + 1) + "/" + $total + "): " + $fileName);
        TextView textView3 = this$0.txtUploadSpeed;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("txtUploadSpeed");
        } else {
            textView2 = textView3;
        }
        textView2.setText("⚡ Đang truyền");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void uploadFiles$lambda$153$lambda$150$lambda$149(boolean $isRenamed, MainActivity this$0, Ref.ObjectRef $finalName, double $elapsedSec, byte[] $bytes) {
        TextView textView = null;
        if ($isRenamed) {
            TextView textView2 = this$0.txtUploadFileName;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtUploadFileName");
                textView2 = null;
            }
            textView2.setText("✓ Đã đổi tên: " + $finalName.element);
            TextView textView3 = this$0.txtUploadSpeed;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtUploadSpeed");
            } else {
                textView = textView3;
            }
            textView.setText("⚡ Trùng tệp");
            return;
        }
        if ($elapsedSec > 0.0d) {
            double speedMBps = ($bytes.length / 1048576.0d) / $elapsedSec;
            TextView textView4 = this$0.txtUploadSpeed;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("txtUploadSpeed");
            } else {
                textView = textView4;
            }
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(Locale.getDefault(), "⚡ %.1f MB/s", Arrays.copyOf(new Object[]{Double.valueOf(speedMBps)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "format(...)");
            textView.setText(format);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void uploadFiles$lambda$153$lambda$151(MainActivity this$0, int $progressPercent) {
        ProgressBar progressBar = this$0.pbUploadProgress;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pbUploadProgress");
            progressBar = null;
        }
        progressBar.setProgress($progressPercent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void uploadFiles$lambda$153$lambda$152(MainActivity this$0, Ref.IntRef $successCount, Ref.BooleanRef $anyRenamed, int $total) {
        String msg;
        LinearLayout linearLayout = this$0.uploadProgressCard;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uploadProgressCard");
            linearLayout = null;
        }
        linearLayout.setVisibility(8);
        if ($successCount.element > 0) {
            if ($anyRenamed.element) {
                msg = "Đã tải lên thành công " + $successCount.element + "/" + $total + " tệp (tự động đổi tên tệp trùng)!";
            } else {
                msg = "Đã tải lên thành công " + $successCount.element + "/" + $total + " tệp!";
            }
            Toast.makeText(this$0, msg, 1).show();
            this$0.folderCache.evictAll();
            this$0.loadFiles(false, true);
            this$0.loadStorage();
            return;
        }
        Toast.makeText(this$0, "Tải lên tệp thất bại", 0).show();
    }

    private final String getFileNameFromUri(Uri uri) {
        String str = "upload_" + System.currentTimeMillis();
        Cursor query = getContentResolver().query(uri, null, null, null, null);
        if (query != null) {
            Cursor cursor = query;
            try {
                Cursor cursor2 = cursor;
                int columnIndex = cursor2.getColumnIndex("_display_name");
                if (columnIndex != -1 && cursor2.moveToFirst()) {
                    String string = cursor2.getString(columnIndex);
                    Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                    str = string;
                }
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(cursor, null);
            } finally {
            }
        }
        return str;
    }

    private final void handleIncomingShareIntent(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        if (Intrinsics.areEqual("android.intent.action.SEND", action)) {
            Uri fileUri = (Uri) intent.getParcelableExtra("android.intent.extra.STREAM");
            if (fileUri != null) {
                uploadFiles(CollectionsKt.listOf(fileUri));
                return;
            }
            return;
        }
        if (Intrinsics.areEqual("android.intent.action.SEND_MULTIPLE", action)) {
            ArrayList fileUris = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            ArrayList arrayList = fileUris;
            if (!(arrayList == null || arrayList.isEmpty())) {
                uploadFiles(fileUris);
            }
        }
    }

    private final void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback() { // from class: com.nasdrive.app.MainActivity$setupBackNavigation$1
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(true);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // androidx.activity.OnBackPressedCallback
            public void handleOnBackPressed() {
                String str;
                String str2;
                boolean z;
                BottomNavigationView bottomNavigationView;
                long j;
                BottomNavigationView bottomNavigationView2;
                BottomNavigationView bottomNavigationView3;
                RecyclerView recyclerView;
                String str3;
                RecyclerView recyclerView2;
                String str4;
                EditText editText;
                str = MainActivity.this.currentSearch;
                Object[] objArr = str.length() > 0;
                MainActivity mainActivity = MainActivity.this;
                String str5 = "";
                BottomNavigationView bottomNavigationView4 = null;
                EditText editText2 = null;
                RecyclerView recyclerView3 = null;
                RecyclerView recyclerView4 = null;
                if (objArr != false) {
                    editText = mainActivity.edtSearch;
                    if (editText == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("edtSearch");
                    } else {
                        editText2 = editText;
                    }
                    editText2.setText("");
                    MainActivity.this.currentSearch = "";
                    MainActivity.this.loadFiles(true, false);
                    return;
                }
                str2 = mainActivity.currentFolder;
                Object[] objArr2 = str2.length() > 0;
                MainActivity mainActivity2 = MainActivity.this;
                if (objArr2 != false) {
                    str3 = mainActivity2.currentFolder;
                    int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) str3, '/', 0, false, 6, (Object) null);
                    MainActivity mainActivity3 = MainActivity.this;
                    if (lastIndexOf$default != -1) {
                        str4 = MainActivity.this.currentFolder;
                        str5 = str4.substring(0, lastIndexOf$default);
                        Intrinsics.checkNotNullExpressionValue(str5, "substring(...)");
                    }
                    mainActivity3.currentFolder = str5;
                    MainActivity.this.updateBreadcrumbs();
                    recyclerView2 = MainActivity.this.rvFiles;
                    if (recyclerView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                    } else {
                        recyclerView3 = recyclerView2;
                    }
                    recyclerView3.scrollToPosition(0);
                    MainActivity.this.loadFiles(true, false);
                    return;
                }
                z = mainActivity2.isVaultMode;
                MainActivity mainActivity4 = MainActivity.this;
                if (z) {
                    mainActivity4.isVaultMode = false;
                    MainActivity.this.updateBreadcrumbs();
                    recyclerView = MainActivity.this.rvFiles;
                    if (recyclerView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                    } else {
                        recyclerView4 = recyclerView;
                    }
                    recyclerView4.scrollToPosition(0);
                    MainActivity.this.loadFiles(true, true);
                    return;
                }
                bottomNavigationView = mainActivity4.bottomNavigationView;
                if (bottomNavigationView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bottomNavigationView");
                    bottomNavigationView = null;
                }
                if (bottomNavigationView.getSelectedItemId() != R.id.nav_files) {
                    bottomNavigationView2 = MainActivity.this.bottomNavigationView;
                    if (bottomNavigationView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bottomNavigationView");
                        bottomNavigationView2 = null;
                    }
                    if (bottomNavigationView2.getSelectedItemId() != R.id.nav_home) {
                        bottomNavigationView3 = MainActivity.this.bottomNavigationView;
                        if (bottomNavigationView3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bottomNavigationView");
                        } else {
                            bottomNavigationView4 = bottomNavigationView3;
                        }
                        bottomNavigationView4.setSelectedItemId(R.id.nav_files);
                        return;
                    }
                }
                long currentTimeMillis = System.currentTimeMillis();
                j = MainActivity.this.backPressedTime;
                long j2 = currentTimeMillis - j;
                MainActivity mainActivity5 = MainActivity.this;
                if (j2 >= ExoPlayer.DEFAULT_DETACH_SURFACE_TIMEOUT_MS) {
                    mainActivity5.backPressedTime = currentTimeMillis;
                    Toast.makeText(MainActivity.this, "Vuốt hoặc nhấn lần nữa để thoát ứng dụng", 0).show();
                } else {
                    mainActivity5.finish();
                }
            }
        });
    }

    private final void setupSwipeBackGesture() {
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() { // from class: com.nasdrive.app.MainActivity$setupSwipeBackGesture$gestureDetector$1
            private final int SWIPE_THRESHOLD = 120;
            private final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                String str;
                boolean z;
                RecyclerView recyclerView;
                RecyclerView recyclerView2;
                RecyclerView recyclerView3;
                String str2;
                String str3;
                RecyclerView recyclerView4;
                String str4;
                Intrinsics.checkNotNullParameter(e2, "e2");
                if (e1 == null) {
                    return false;
                }
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (diffX > this.SWIPE_THRESHOLD && Math.abs(diffX) > Math.abs(diffY) * 1.3f && Math.abs(velocityX) > this.SWIPE_VELOCITY_THRESHOLD) {
                    str = MainActivity.this.currentFolder;
                    boolean z2 = str.length() > 0;
                    MainActivity mainActivity = MainActivity.this;
                    RecyclerView recyclerView5 = null;
                    if (z2) {
                        recyclerView3 = mainActivity.rvFiles;
                        if (recyclerView3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                            recyclerView3 = null;
                        }
                        recyclerView3.performHapticFeedback(3);
                        str2 = MainActivity.this.currentFolder;
                        int lastSlash = StringsKt.lastIndexOf$default((CharSequence) str2, '/', 0, false, 6, (Object) null);
                        MainActivity mainActivity2 = MainActivity.this;
                        if (lastSlash != -1) {
                            str4 = MainActivity.this.currentFolder;
                            str3 = str4.substring(0, lastSlash);
                            Intrinsics.checkNotNullExpressionValue(str3, "substring(...)");
                        } else {
                            str3 = "";
                        }
                        mainActivity2.currentFolder = str3;
                        MainActivity.this.updateBreadcrumbs();
                        recyclerView4 = MainActivity.this.rvFiles;
                        if (recyclerView4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                        } else {
                            recyclerView5 = recyclerView4;
                        }
                        recyclerView5.scrollToPosition(0);
                        MainActivity.this.loadFiles(true, false);
                        return true;
                    }
                    z = mainActivity.isVaultMode;
                    if (z) {
                        recyclerView = MainActivity.this.rvFiles;
                        if (recyclerView == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                            recyclerView = null;
                        }
                        recyclerView.performHapticFeedback(3);
                        MainActivity.this.isVaultMode = false;
                        MainActivity.this.updateBreadcrumbs();
                        recyclerView2 = MainActivity.this.rvFiles;
                        if (recyclerView2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
                        } else {
                            recyclerView5 = recyclerView2;
                        }
                        recyclerView5.scrollToPosition(0);
                        MainActivity.this.loadFiles(true, true);
                        return true;
                    }
                }
                return false;
            }
        });
        RecyclerView recyclerView = this.rvFiles;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rvFiles");
            recyclerView = null;
        }
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() { // from class: com.nasdrive.app.MainActivity$setupSwipeBackGesture$1
            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Intrinsics.checkNotNullParameter(rv, "rv");
                Intrinsics.checkNotNullParameter(e, "e");
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Intrinsics.checkNotNullParameter(rv, "rv");
                Intrinsics.checkNotNullParameter(e, "e");
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void checkPermissions() {
        List permissions = new ArrayList();
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
                permissions.add("android.permission.POST_NOTIFICATIONS");
            }
            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                permissions.add("android.permission.READ_MEDIA_IMAGES");
            }
            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_VIDEO") != 0) {
                permissions.add("android.permission.READ_MEDIA_VIDEO");
            }
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            permissions.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (!permissions.isEmpty()) {
            this.requestPermissionLauncher.launch(permissions.toArray(new String[0]));
        }
    }

    private final void showServerBottomSheet() {
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_server, (ViewGroup) null);
        sheetDialog.setContentView(sheetView);
        RadioGroup rg = (RadioGroup) sheetView.findViewById(R.id.rgServerOptions);
        final RadioButton rbAuto = (RadioButton) sheetView.findViewById(R.id.rbAutoDetect);
        final RadioButton rbLan88 = (RadioButton) sheetView.findViewById(R.id.rbServerLan88);
        final RadioButton rbLan1 = (RadioButton) sheetView.findViewById(R.id.rbServerLan1);
        final RadioButton rbNasLan = (RadioButton) sheetView.findViewById(R.id.rbServerDomain);
        final RadioButton rbWan = (RadioButton) sheetView.findViewById(R.id.rbServerWan);
        final RadioButton rbCustom = (RadioButton) sheetView.findViewById(R.id.rbServerCustom);
        final TextInputLayout tilCustom = (TextInputLayout) sheetView.findViewById(R.id.tilCustomUrl);
        final EditText edtCustom = (EditText) sheetView.findViewById(R.id.edtCustomUrl);
        final TextView txtPing = (TextView) sheetView.findViewById(R.id.txtPingResults);
        MaterialButton btnPing = (MaterialButton) sheetView.findViewById(R.id.btnSheetPingTest);
        MaterialButton btnApply = (MaterialButton) sheetView.findViewById(R.id.btnSheetApply);
        boolean isAuto = ServerConfig.INSTANCE.isAutoMode(this);
        String currentSelected = ServerConfig.INSTANCE.getSelectedUrl(this);
        boolean z = true;
        if (!isAuto) {
            switch (currentSelected.hashCode()) {
                case -2030100064:
                    if (currentSelected.equals(ServerConfig.URL_LAN_1)) {
                        rbLan1.setChecked(true);
                        break;
                    }
                    z = true;
                    rbCustom.setChecked(z);
                    tilCustom.setVisibility(0);
                    edtCustom.setText(currentSelected);
                    break;
                case -1751689953:
                    if (currentSelected.equals(ServerConfig.URL_WAN_CLOUDFLARE)) {
                        rbWan.setChecked(true);
                        break;
                    }
                    z = true;
                    rbCustom.setChecked(z);
                    tilCustom.setVisibility(0);
                    edtCustom.setText(currentSelected);
                    break;
                case 823829841:
                    if (currentSelected.equals(ServerConfig.URL_LAN_DOMAIN)) {
                        rbNasLan.setChecked(true);
                        break;
                    }
                    z = true;
                    rbCustom.setChecked(z);
                    tilCustom.setVisibility(0);
                    edtCustom.setText(currentSelected);
                    break;
                case 1056239800:
                    if (currentSelected.equals(ServerConfig.URL_LAN_88)) {
                        rbLan88.setChecked(true);
                        break;
                    }
                    z = true;
                    rbCustom.setChecked(z);
                    tilCustom.setVisibility(0);
                    edtCustom.setText(currentSelected);
                    break;
                default:
                    rbCustom.setChecked(z);
                    tilCustom.setVisibility(0);
                    edtCustom.setText(currentSelected);
                    break;
            }
        } else {
            rbAuto.setChecked(true);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda50
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                MainActivity.showServerBottomSheet$lambda$155(TextInputLayout.this, radioGroup, i);
            }
        });
        btnPing.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda51
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showServerBottomSheet$lambda$158(txtPing, this, rbLan88, rbLan1, rbNasLan, rbWan, view);
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda52
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showServerBottomSheet$lambda$159(rbAuto, this, rbLan88, rbLan1, rbNasLan, rbWan, rbCustom, edtCustom, sheetDialog, view);
            }
        });
        sheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showServerBottomSheet$lambda$155(TextInputLayout $tilCustom, RadioGroup radioGroup, int checkedId) {
        Intrinsics.checkNotNullParameter(radioGroup, "<unused var>");
        $tilCustom.setVisibility(checkedId == R.id.rbServerCustom ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showServerBottomSheet$lambda$158(final TextView $txtPing, final MainActivity this$0, final RadioButton $rbLan88, final RadioButton $rbLan1, final RadioButton $rbNasLan, final RadioButton $rbWan, View it) {
        $txtPing.setVisibility(0);
        $txtPing.setText("Đang kiểm tra độ trễ các máy chủ...");
        this$0.backgroundExecutor.execute(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showServerBottomSheet$lambda$158$lambda$157(MainActivity.this, $rbLan88, $rbLan1, $rbNasLan, $rbWan, $txtPing);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showServerBottomSheet$lambda$158$lambda$157(MainActivity this$0, final RadioButton $rbLan88, final RadioButton $rbLan1, final RadioButton $rbNasLan, final RadioButton $rbWan, final TextView $txtPing) {
        final DetectionResult ping88 = ServerDetector.INSTANCE.testUrl(ServerConfig.URL_LAN_88);
        final DetectionResult ping1 = ServerDetector.INSTANCE.testUrl(ServerConfig.URL_LAN_1);
        final DetectionResult pingDomain = ServerDetector.INSTANCE.testUrl(ServerConfig.URL_LAN_DOMAIN);
        final DetectionResult pingWan = ServerDetector.INSTANCE.testUrl(ServerConfig.URL_WAN_CLOUDFLARE);
        this$0.mainHandler.post(new Runnable() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.showServerBottomSheet$lambda$158$lambda$157$lambda$156($rbLan88, ping88, $rbLan1, ping1, $rbNasLan, pingDomain, $rbWan, pingWan, $txtPing);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showServerBottomSheet$lambda$158$lambda$157$lambda$156(RadioButton $rbLan88, DetectionResult $ping88, RadioButton $rbLan1, DetectionResult $ping1, RadioButton $rbNasLan, DetectionResult $pingDomain, RadioButton $rbWan, DetectionResult $pingWan, TextView $txtPing) {
        String str;
        String str2;
        String str3;
        String str4 = "🔴 Không kết nối";
        if ($ping88.getSuccess()) {
            str = "🟢 " + $ping88.getLatencyMs() + "ms";
        } else {
            str = "🔴 Không kết nối";
        }
        $rbLan88.setText("⚡ LAN Lớp 2: 192.168.88.230 " + str);
        if ($ping1.getSuccess()) {
            str2 = "🟢 " + $ping1.getLatencyMs() + "ms";
        } else {
            str2 = "🔴 Không kết nối";
        }
        $rbLan1.setText("⚡ LAN Lớp 1: 192.168.1.8 " + str2);
        if ($pingDomain.getSuccess()) {
            str3 = "🟢 " + $pingDomain.getLatencyMs() + "ms";
        } else {
            str3 = "🔴 Không kết nối";
        }
        $rbNasLan.setText("🏠 Tên miền: nas.lan " + str3);
        if ($pingWan.getSuccess()) {
            str4 = "🔵 " + $pingWan.getLatencyMs() + "ms";
        }
        $rbWan.setText("🌐 Cloudflare WAN " + str4);
        $txtPing.setText("Kiểm tra hoàn tất!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void showServerBottomSheet$lambda$159(RadioButton $rbAuto, MainActivity this$0, RadioButton $rbLan88, RadioButton $rbLan1, RadioButton $rbNasLan, RadioButton $rbWan, RadioButton $rbCustom, EditText $edtCustom, BottomSheetDialog $sheetDialog, View it) {
        if ($rbAuto.isChecked()) {
            ServerConfig.INSTANCE.setAutoMode(this$0, true);
        } else {
            ServerConfig.INSTANCE.setAutoMode(this$0, false);
            boolean isChecked = $rbLan88.isChecked();
            String text = ServerConfig.URL_LAN_88;
            if (!isChecked) {
                if ($rbLan1.isChecked()) {
                    text = ServerConfig.URL_LAN_1;
                } else if ($rbNasLan.isChecked()) {
                    text = ServerConfig.URL_LAN_DOMAIN;
                } else if ($rbWan.isChecked()) {
                    text = ServerConfig.URL_WAN_CLOUDFLARE;
                } else if ($rbCustom.isChecked()) {
                    text = StringsKt.trim((CharSequence) $edtCustom.getText().toString()).toString();
                    if (!StringsKt.startsWith$default(text, "http://", false, 2, (Object) null) && !StringsKt.startsWith$default(text, "https://", false, 2, (Object) null)) {
                        text = "http://" + text;
                    }
                }
            }
            ServerConfig.INSTANCE.setSelectedUrl(this$0, text);
        }
        $sheetDialog.dismiss();
        this$0.initConnectionAndLoad();
    }

    private final void showDownloadsBottomSheet() {
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_downloads, (ViewGroup) null);
        sheetDialog.setContentView(sheetView);
        final RecyclerView rv = (RecyclerView) sheetView.findViewById(R.id.rvDownloadedFiles);
        final TextView txtEmpty = (TextView) sheetView.findViewById(R.id.txtEmptyDownloads);
        ImageButton btnRefreshSheet = (ImageButton) sheetView.findViewById(R.id.btnRefreshDownloads);
        rv.setLayoutManager(new LinearLayoutManager(this));
        showDownloadsBottomSheet$loadLocalFiles(txtEmpty, rv, this, sheetDialog);
        btnRefreshSheet.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.MainActivity$$ExternalSyntheticLambda83
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.showDownloadsBottomSheet$loadLocalFiles(txtEmpty, rv, this, sheetDialog);
            }
        });
        sheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x007b, code lost:
    
        if (r3 == null) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0056 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void showDownloadsBottomSheet$loadLocalFiles(android.widget.TextView r25, androidx.recyclerview.widget.RecyclerView r26, final com.nasdrive.app.MainActivity r27, final com.google.android.material.bottomsheet.BottomSheetDialog r28) {
        /*
            Method dump skipped, instructions count: 307
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nasdrive.app.MainActivity.showDownloadsBottomSheet$loadLocalFiles(android.widget.TextView, androidx.recyclerview.widget.RecyclerView, com.nasdrive.app.MainActivity, com.google.android.material.bottomsheet.BottomSheetDialog):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit showDownloadsBottomSheet$loadLocalFiles$lambda$165(BottomSheetDialog $sheetDialog, MainActivity this$0, DownloadedItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        $sheetDialog.dismiss();
        if (item.isVideo()) {
            Intent intent = new Intent(this$0, (Class<?>) VideoPlayerActivity.class);
            intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, Uri.fromFile(item.getFile()).toString());
            intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, item.getName());
            intent.putExtra(VideoPlayerActivity.EXTRA_SERVER_TYPE, "OFFLINE");
            this$0.startActivity(intent);
        } else {
            Uri uri = FileProvider.getUriForFile(this$0, this$0.getPackageName() + ".fileprovider", item.getFile());
            Intent openIntent = new Intent("android.intent.action.VIEW");
            String type = this$0.getContentResolver().getType(uri);
            if (type == null) {
                type = "*/*";
            }
            openIntent.setDataAndType(uri, type);
            openIntent.addFlags(1);
            try {
                this$0.startActivity(openIntent);
            } catch (Exception e) {
                Toast.makeText(this$0, "Không có ứng dụng để mở tệp này", 0).show();
            }
        }
        return Unit.INSTANCE;
    }

    private final boolean isVideoExtension(String fileName) {
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault(...)");
        String lower = fileName.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(lower, "toLowerCase(...)");
        return StringsKt.endsWith$default(lower, ".mp4", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".mkv", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".webm", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".mov", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".avi", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".ts", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".flv", false, 2, (Object) null) || StringsKt.endsWith$default(lower, ".m4v", false, 2, (Object) null);
    }

    private final String formatFileSize(long bytes) {
        if (bytes <= 0) {
            return "0 B";
        }
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024.0d));
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String format = String.format(Locale.getDefault(), "%.1f %s", Arrays.copyOf(new Object[]{Double.valueOf(bytes / Math.pow(1024.0d, digitGroups)), units[digitGroups]}, 2));
        Intrinsics.checkNotNullExpressionValue(format, "format(...)");
        return format;
    }

    private final void vibrate() {
        try {
            getWindow().getDecorView().performHapticFeedback(6);
        } catch (Exception e) {
        }
    }
}

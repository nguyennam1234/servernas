package com.nasdrive.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MimeTypes;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import coil.util.CoilUtils;
import com.nasdrive.app.NasFilesAdapter;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: NasFilesAdapter.kt */
@Metadata(d1 = {"\u0000a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b*\u0001\u0014\u0018\u0000 ,2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003,-.BG\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b\u0012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\n0\f¢\u0006\u0004\b\u000e\u0010\u000fJ \u0010\u0019\u001a\u00020\n2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u001b2\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001dJ\b\u0010!\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"H\u0016J\u0018\u0010%\u001a\u00020\u00022\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\"H\u0016J\u0018\u0010)\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u00022\u0006\u0010$\u001a\u00020\"H\u0016J\u0010\u0010+\u001a\u00020\n2\u0006\u0010*\u001a\u00020\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\n0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0015R\u001c\u0010\u0016\u001a\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\t0\t0\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\u001b8F¢\u0006\u0006\u001a\u0004\b\u001f\u0010 ¨\u0006/"}, d2 = {"Lcom/nasdrive/app/NasFilesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "context", "Landroid/content/Context;", "isGridMode", "", "onItemClick", "Lkotlin/Function1;", "Lcom/nasdrive/app/NasItem;", "", "onMoreClick", "Lkotlin/Function2;", "Landroid/view/View;", "<init>", "(Landroid/content/Context;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "()Z", "setGridMode", "(Z)V", "diffCallback", "com/nasdrive/app/NasFilesAdapter$diffCallback$1", "Lcom/nasdrive/app/NasFilesAdapter$diffCallback$1;", "differ", "Landroidx/recyclerview/widget/AsyncListDiffer;", "kotlin.jvm.PlatformType", "submitList", "newItems", "", "commitCallback", "Ljava/lang/Runnable;", "currentList", "getCurrentList", "()Ljava/util/List;", "getItemCount", "", "getItemViewType", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onBindViewHolder", "holder", "onViewRecycled", "Companion", "GridViewHolder", "ListViewHolder", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class NasFilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GRID = 1;
    private static final int TYPE_LIST = 2;
    private final Context context;
    private final NasFilesAdapter$diffCallback$1 diffCallback;
    private final AsyncListDiffer<NasItem> differ;
    private boolean isGridMode;
    private final Function1<NasItem, Unit> onItemClick;
    private final Function2<NasItem, View, Unit> onMoreClick;

    public /* synthetic */ NasFilesAdapter(Context context, boolean z, Function1 function1, Function2 function2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? true : z, function1, function2);
    }

    /* renamed from: isGridMode, reason: from getter */
    public final boolean getIsGridMode() {
        return this.isGridMode;
    }

    public final void setGridMode(boolean z) {
        this.isGridMode = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.nasdrive.app.NasFilesAdapter$diffCallback$1] */
    public NasFilesAdapter(Context context, boolean isGridMode, Function1<? super NasItem, Unit> onItemClick, Function2<? super NasItem, ? super View, Unit> onMoreClick) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onItemClick, "onItemClick");
        Intrinsics.checkNotNullParameter(onMoreClick, "onMoreClick");
        this.context = context;
        this.isGridMode = isGridMode;
        this.onItemClick = onItemClick;
        this.onMoreClick = onMoreClick;
        this.diffCallback = new DiffUtil.ItemCallback<NasItem>() { // from class: com.nasdrive.app.NasFilesAdapter$diffCallback$1
            @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
            public boolean areItemsTheSame(NasItem oldItem, NasItem newItem) {
                Intrinsics.checkNotNullParameter(oldItem, "oldItem");
                Intrinsics.checkNotNullParameter(newItem, "newItem");
                return Intrinsics.areEqual(oldItem.getPath(), newItem.getPath());
            }

            @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
            public boolean areContentsTheSame(NasItem oldItem, NasItem newItem) {
                Intrinsics.checkNotNullParameter(oldItem, "oldItem");
                Intrinsics.checkNotNullParameter(newItem, "newItem");
                return Intrinsics.areEqual(oldItem, newItem);
            }
        };
        this.differ = new AsyncListDiffer<>(this, this.diffCallback);
    }

    public static /* synthetic */ void submitList$default(NasFilesAdapter nasFilesAdapter, List list, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        nasFilesAdapter.submitList(list, runnable);
    }

    public final void submitList(List<NasItem> newItems, Runnable commitCallback) {
        Intrinsics.checkNotNullParameter(newItems, "newItems");
        this.differ.submitList(newItems, commitCallback);
    }

    public final List<NasItem> getCurrentList() {
        List<NasItem> currentList = this.differ.getCurrentList();
        Intrinsics.checkNotNullExpressionValue(currentList, "getCurrentList(...)");
        return currentList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.differ.getCurrentList().size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.isGridMode ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            View v = inflater.inflate(R.layout.item_nas_grid, parent, false);
            Intrinsics.checkNotNull(v);
            return new GridViewHolder(this, v);
        }
        View v2 = inflater.inflate(R.layout.item_nas_list, parent, false);
        Intrinsics.checkNotNull(v2);
        return new ListViewHolder(this, v2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        NasItem item = this.differ.getCurrentList().get(position);
        if (holder instanceof GridViewHolder) {
            Intrinsics.checkNotNull(item);
            ((GridViewHolder) holder).bind(item);
        } else if (holder instanceof ListViewHolder) {
            Intrinsics.checkNotNull(item);
            ((ListViewHolder) holder).bind(item);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        super.onViewRecycled(holder);
        if (holder instanceof GridViewHolder) {
            ((GridViewHolder) holder).unbind();
        } else if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).unbind();
        }
    }

    /* compiled from: NasFilesAdapter.kt */
    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/nasdrive/app/NasFilesAdapter$GridViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "<init>", "(Lcom/nasdrive/app/NasFilesAdapter;Landroid/view/View;)V", "imgThumb", "Landroid/widget/ImageView;", "imgTypeIcon", "imgPlayBadge", "txtBadge", "Landroid/widget/TextView;", "txtName", "txtMeta", "btnMore", "Landroid/widget/ImageButton;", "unbind", "", "bind", "item", "Lcom/nasdrive/app/NasItem;", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes3.dex */
    public final class GridViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnMore;
        private final ImageView imgPlayBadge;
        private final ImageView imgThumb;
        private final ImageView imgTypeIcon;
        final /* synthetic */ NasFilesAdapter this$0;
        private final TextView txtBadge;
        private final TextView txtMeta;
        private final TextView txtName;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public GridViewHolder(NasFilesAdapter this$0, View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            this.this$0 = this$0;
            View findViewById = view.findViewById(R.id.imgGridThumb);
            Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
            this.imgThumb = (ImageView) findViewById;
            View findViewById2 = view.findViewById(R.id.imgTypeIcon);
            Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
            this.imgTypeIcon = (ImageView) findViewById2;
            View findViewById3 = view.findViewById(R.id.imgPlayBadge);
            Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
            this.imgPlayBadge = (ImageView) findViewById3;
            View findViewById4 = view.findViewById(R.id.txtGridBadge);
            Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
            this.txtBadge = (TextView) findViewById4;
            View findViewById5 = view.findViewById(R.id.txtGridName);
            Intrinsics.checkNotNullExpressionValue(findViewById5, "findViewById(...)");
            this.txtName = (TextView) findViewById5;
            View findViewById6 = view.findViewById(R.id.txtGridMeta);
            Intrinsics.checkNotNullExpressionValue(findViewById6, "findViewById(...)");
            this.txtMeta = (TextView) findViewById6;
            View findViewById7 = view.findViewById(R.id.btnGridMore);
            Intrinsics.checkNotNullExpressionValue(findViewById7, "findViewById(...)");
            this.btnMore = (ImageButton) findViewById7;
        }

        public final void unbind() {
            CoilUtils.dispose(this.imgThumb);
        }

        public final void bind(final NasItem item) {
            Intrinsics.checkNotNullParameter(item, "item");
            this.txtName.setText(item.getName());
            this.txtMeta.setText(item.isFolder() ? item.getFormattedDate() : item.getFormattedSize() + " • " + item.getFormattedDate());
            View view = this.itemView;
            final NasFilesAdapter nasFilesAdapter = this.this$0;
            view.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.NasFilesAdapter$GridViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    NasFilesAdapter.this.onItemClick.invoke(item);
                }
            });
            ImageButton imageButton = this.btnMore;
            final NasFilesAdapter nasFilesAdapter2 = this.this$0;
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.NasFilesAdapter$GridViewHolder$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    NasFilesAdapter.GridViewHolder.bind$lambda$1(NasFilesAdapter.this, item, view2);
                }
            });
            if (!item.isFolder()) {
                if (Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_VIDEO)) {
                    this.imgThumb.setVisibility(0);
                    this.imgTypeIcon.setVisibility(8);
                    this.imgPlayBadge.setVisibility(0);
                    this.txtBadge.setVisibility(0);
                    boolean isVault = item.isVault();
                    TextView textView = this.txtBadge;
                    if (isVault) {
                        textView.setText("🔒 BÍ MẬT");
                    } else {
                        String upperCase = item.getExt().toUpperCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
                        textView.setText(StringsKt.removePrefix(upperCase, (CharSequence) "."));
                    }
                    String thumbUrl = NasApiClient.INSTANCE.getThumbnailUrl(this.this$0.context, item.getPath());
                    ImageView imageView = this.imgThumb;
                    ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
                    ImageRequest.Builder target = new ImageRequest.Builder(imageView.getContext()).data(thumbUrl).target(imageView);
                    target.crossfade(150);
                    target.error(R.drawable.ic_fluent_video_24_regular);
                    imageLoader.enqueue(target.build());
                    return;
                }
                String thumbUrl2 = item.getType();
                if (Intrinsics.areEqual(thumbUrl2, "image")) {
                    this.imgThumb.setVisibility(0);
                    this.imgTypeIcon.setVisibility(8);
                    this.imgPlayBadge.setVisibility(8);
                    this.txtBadge.setVisibility(item.isVault() ? 0 : 8);
                    if (item.isVault()) {
                        this.txtBadge.setText("🔒 BÍ MẬT");
                    }
                    Object streamUrl = item.isVault() ? NasApiClient.INSTANCE.getVaultStreamUrl(this.this$0.context, item.getVaultId()) : NasApiClient.INSTANCE.getStreamUrl(this.this$0.context, item.getPath());
                    ImageView imageView2 = this.imgThumb;
                    ImageLoader imageLoader2 = Coil.imageLoader(imageView2.getContext());
                    ImageRequest.Builder target2 = new ImageRequest.Builder(imageView2.getContext()).data(streamUrl).target(imageView2);
                    target2.crossfade(150);
                    target2.error(R.drawable.ic_fluent_album_24_regular);
                    imageLoader2.enqueue(target2.build());
                    return;
                }
                CoilUtils.dispose(this.imgThumb);
                this.imgThumb.setVisibility(8);
                this.imgTypeIcon.setVisibility(0);
                this.imgTypeIcon.setImageResource(item.isVault() ? R.drawable.ic_lock : R.drawable.ic_file);
                this.imgTypeIcon.setColorFilter(Color.parseColor(item.isVault() ? "#E11D48" : "#605E5C"));
                this.imgPlayBadge.setVisibility(8);
                this.txtBadge.setVisibility(item.isVault() ? 0 : 8);
                if (item.isVault()) {
                    this.txtBadge.setText("🔒 BÍ MẬT");
                    return;
                }
                return;
            }
            CoilUtils.dispose(this.imgThumb);
            this.imgThumb.setVisibility(8);
            this.imgTypeIcon.setVisibility(0);
            this.imgTypeIcon.setImageResource(R.drawable.ic_fluent_folder_24_filled);
            this.imgTypeIcon.setColorFilter(Color.parseColor("#FFB900"));
            this.imgPlayBadge.setVisibility(8);
            this.txtBadge.setVisibility(8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static final void bind$lambda$1(NasFilesAdapter this$0, NasItem $item, View it) {
            Function2 function2 = this$0.onMoreClick;
            Intrinsics.checkNotNull(it);
            function2.invoke($item, it);
        }
    }

    /* compiled from: NasFilesAdapter.kt */
    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/nasdrive/app/NasFilesAdapter$ListViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "<init>", "(Lcom/nasdrive/app/NasFilesAdapter;Landroid/view/View;)V", "imgThumb", "Landroid/widget/ImageView;", "imgTypeIcon", "imgPlayBadge", "txtName", "Landroid/widget/TextView;", "txtMeta", "btnMore", "Landroid/widget/ImageButton;", "unbind", "", "bind", "item", "Lcom/nasdrive/app/NasItem;", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes3.dex */
    public final class ListViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnMore;
        private final ImageView imgPlayBadge;
        private final ImageView imgThumb;
        private final ImageView imgTypeIcon;
        final /* synthetic */ NasFilesAdapter this$0;
        private final TextView txtMeta;
        private final TextView txtName;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ListViewHolder(NasFilesAdapter this$0, View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            this.this$0 = this$0;
            View findViewById = view.findViewById(R.id.imgListThumb);
            Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
            this.imgThumb = (ImageView) findViewById;
            View findViewById2 = view.findViewById(R.id.imgListTypeIcon);
            Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
            this.imgTypeIcon = (ImageView) findViewById2;
            View findViewById3 = view.findViewById(R.id.imgListPlayBadge);
            Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
            this.imgPlayBadge = (ImageView) findViewById3;
            View findViewById4 = view.findViewById(R.id.txtListName);
            Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
            this.txtName = (TextView) findViewById4;
            View findViewById5 = view.findViewById(R.id.txtListMeta);
            Intrinsics.checkNotNullExpressionValue(findViewById5, "findViewById(...)");
            this.txtMeta = (TextView) findViewById5;
            View findViewById6 = view.findViewById(R.id.btnListMore);
            Intrinsics.checkNotNullExpressionValue(findViewById6, "findViewById(...)");
            this.btnMore = (ImageButton) findViewById6;
        }

        public final void unbind() {
            CoilUtils.dispose(this.imgThumb);
        }

        public final void bind(final NasItem item) {
            String str;
            Intrinsics.checkNotNullParameter(item, "item");
            this.txtName.setText(item.getName());
            TextView textView = this.txtMeta;
            if (item.isFolder()) {
                str = item.getFormattedDate();
            } else {
                str = item.getFormattedSize() + " • " + item.getFormattedDate();
            }
            textView.setText(str);
            View view = this.itemView;
            final NasFilesAdapter nasFilesAdapter = this.this$0;
            view.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.NasFilesAdapter$ListViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    NasFilesAdapter.this.onItemClick.invoke(item);
                }
            });
            ImageButton imageButton = this.btnMore;
            final NasFilesAdapter nasFilesAdapter2 = this.this$0;
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.NasFilesAdapter$ListViewHolder$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    NasFilesAdapter.ListViewHolder.bind$lambda$1(NasFilesAdapter.this, item, view2);
                }
            });
            if (!item.isFolder()) {
                if (Intrinsics.areEqual(item.getType(), MimeTypes.BASE_TYPE_VIDEO)) {
                    this.imgThumb.setVisibility(0);
                    this.imgTypeIcon.setVisibility(8);
                    this.imgPlayBadge.setVisibility(0);
                    String thumbUrl = NasApiClient.INSTANCE.getThumbnailUrl(this.this$0.context, item.getPath());
                    ImageView imageView = this.imgThumb;
                    ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
                    ImageRequest.Builder target = new ImageRequest.Builder(imageView.getContext()).data(thumbUrl).target(imageView);
                    target.crossfade(150);
                    target.error(R.drawable.ic_fluent_video_24_regular);
                    imageLoader.enqueue(target.build());
                    return;
                }
                String thumbUrl2 = item.getType();
                if (Intrinsics.areEqual(thumbUrl2, "image")) {
                    this.imgThumb.setVisibility(0);
                    this.imgTypeIcon.setVisibility(8);
                    this.imgPlayBadge.setVisibility(8);
                    Object streamUrl = item.isVault() ? NasApiClient.INSTANCE.getVaultStreamUrl(this.this$0.context, item.getVaultId()) : NasApiClient.INSTANCE.getStreamUrl(this.this$0.context, item.getPath());
                    ImageView imageView2 = this.imgThumb;
                    ImageLoader imageLoader2 = Coil.imageLoader(imageView2.getContext());
                    ImageRequest.Builder target2 = new ImageRequest.Builder(imageView2.getContext()).data(streamUrl).target(imageView2);
                    target2.crossfade(150);
                    target2.error(R.drawable.ic_fluent_album_24_regular);
                    imageLoader2.enqueue(target2.build());
                    return;
                }
                CoilUtils.dispose(this.imgThumb);
                this.imgThumb.setVisibility(8);
                this.imgTypeIcon.setVisibility(0);
                this.imgTypeIcon.setImageResource(item.isVault() ? R.drawable.ic_lock : R.drawable.ic_file);
                this.imgTypeIcon.setColorFilter(Color.parseColor(item.isVault() ? "#E11D48" : "#605E5C"));
                this.imgPlayBadge.setVisibility(8);
                return;
            }
            CoilUtils.dispose(this.imgThumb);
            this.imgThumb.setVisibility(8);
            this.imgTypeIcon.setVisibility(0);
            this.imgTypeIcon.setImageResource(R.drawable.ic_fluent_folder_24_filled);
            this.imgTypeIcon.setColorFilter(Color.parseColor("#FFB900"));
            this.imgPlayBadge.setVisibility(8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static final void bind$lambda$1(NasFilesAdapter this$0, NasItem $item, View it) {
            Function2 function2 = this$0.onMoreClick;
            Intrinsics.checkNotNull(it);
            function2.invoke($item, it);
        }
    }
}

package com.nasdrive.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadedFilesAdapter.kt */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0016B)\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\b\u0010\u0013\u001a\u00020\u000fH\u0016J\u0014\u0010\u0014\u001a\u00020\b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/nasdrive/app/DownloadedFilesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/nasdrive/app/DownloadedFilesAdapter$ViewHolder;", "items", "", "Lcom/nasdrive/app/DownloadedItem;", "onItemClick", "Lkotlin/Function1;", "", "<init>", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "", "onBindViewHolder", "holder", "position", "getItemCount", "updateList", "newItems", "ViewHolder", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
/* loaded from: classes3.dex */
public final class DownloadedFilesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<DownloadedItem> items;
    private final Function1<DownloadedItem, Unit> onItemClick;

    /* JADX WARN: Multi-variable type inference failed */
    public DownloadedFilesAdapter(List<DownloadedItem> items, Function1<? super DownloadedItem, Unit> onItemClick) {
        Intrinsics.checkNotNullParameter(items, "items");
        Intrinsics.checkNotNullParameter(onItemClick, "onItemClick");
        this.items = items;
        this.onItemClick = onItemClick;
    }

    /* compiled from: DownloadedFilesAdapter.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0010\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0014"}, d2 = {"Lcom/nasdrive/app/DownloadedFilesAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "<init>", "(Landroid/view/View;)V", "imgFileIcon", "Landroid/widget/ImageView;", "getImgFileIcon", "()Landroid/widget/ImageView;", "txtFileName", "Landroid/widget/TextView;", "getTxtFileName", "()Landroid/widget/TextView;", "txtFileSizeDate", "getTxtFileSizeDate", "btnOpenFile", "Landroid/widget/ImageButton;", "getBtnOpenFile", "()Landroid/widget/ImageButton;", "app"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    /* loaded from: classes3.dex */
    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnOpenFile;
        private final ImageView imgFileIcon;
        private final TextView txtFileName;
        private final TextView txtFileSizeDate;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ViewHolder(View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(R.id.imgFileIcon);
            Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
            this.imgFileIcon = (ImageView) findViewById;
            View findViewById2 = view.findViewById(R.id.txtFileName);
            Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
            this.txtFileName = (TextView) findViewById2;
            View findViewById3 = view.findViewById(R.id.txtFileSizeDate);
            Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
            this.txtFileSizeDate = (TextView) findViewById3;
            View findViewById4 = view.findViewById(R.id.btnOpenFile);
            Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
            this.btnOpenFile = (ImageButton) findViewById4;
        }

        public final ImageView getImgFileIcon() {
            return this.imgFileIcon;
        }

        public final TextView getTxtFileName() {
            return this.txtFileName;
        }

        public final TextView getTxtFileSizeDate() {
            return this.txtFileSizeDate;
        }

        public final ImageButton getBtnOpenFile() {
            return this.btnOpenFile;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_file, parent, false);
        Intrinsics.checkNotNull(view);
        return new ViewHolder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        final DownloadedItem item = this.items.get(position);
        holder.getTxtFileName().setText(item.getName());
        holder.getTxtFileSizeDate().setText(item.getSizeFormatted() + " • " + item.getDateFormatted());
        if (item.isVideo()) {
            holder.getImgFileIcon().setImageResource(R.drawable.ic_play_arrow);
            holder.getBtnOpenFile().setImageResource(R.drawable.ic_play_arrow);
        } else {
            holder.getImgFileIcon().setImageResource(R.drawable.ic_file);
            holder.getBtnOpenFile().setImageResource(R.drawable.ic_download);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.DownloadedFilesAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DownloadedFilesAdapter.this.onItemClick.invoke(item);
            }
        });
        holder.getBtnOpenFile().setOnClickListener(new View.OnClickListener() { // from class: com.nasdrive.app.DownloadedFilesAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DownloadedFilesAdapter.this.onItemClick.invoke(item);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    public final void updateList(List<DownloadedItem> newItems) {
        Intrinsics.checkNotNullParameter(newItems, "newItems");
        this.items = newItems;
        notifyDataSetChanged();
    }
}

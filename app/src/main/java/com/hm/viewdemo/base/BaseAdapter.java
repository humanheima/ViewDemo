package com.hm.viewdemo.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by chenchao on 16/9/27.
 * cc@cchao.org
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "LoadMoreAdapter";

    private final int FOOTER_VIEW_ITEM = -200;
    private final int HEADER_VIEW_ITEM = -300;
    private final int DEFAULT_VIEW_ITEM = -400;

    private RecyclerView recyclerView;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private View headerView = null;
    private View footerView = null;

    /**
     * 设置点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加底部
     * @param view
     */
    public void addFooterView(View view) {
        if (view == null) {
            throw new NullPointerException("FooterView is null!");
        }
        if (footerView != null) {
            return;
        }
        footerView = view;
        footerView.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 移除底部View
     */
    public void removeFooterView() {
        if (footerView != null) {
            footerView = null;
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * 添加头部
     * @param view
     */
    public void addHeaderView(View view) {
        if (view == null) {
            throw new NullPointerException("HeadView is null");
        }
        if (headerView != null) {
            return;
        }
        headerView = view;
        headerView.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        notifyDataSetChanged();
    }

    public void removeHeaderView() {
        if (headerView != null) {
            headerView = null;
            notifyDataSetChanged();
        }
    }


    @Override
    public final int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return HEADER_VIEW_ITEM;
        }
        if (footerView != null && position >= (headerView == null ? getCount() : getCount() + 1)) {
            return FOOTER_VIEW_ITEM;
        }
        return getItemType(headerView == null ? position : position - 1);
    }

    public int getItemType(int position) {
        return DEFAULT_VIEW_ITEM;
    }

    @Override
    public final int getItemCount() {
        int count = getCount();
        if (count == 0) {
            return 0;
        }
        if (footerView != null) {
            count++;
        }
        if (headerView != null) {
            count++;
        }
        return count;
    }

    protected abstract int getCount();

    protected abstract RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType);

    protected abstract void onBindView(RecyclerView.ViewHolder holder, int position);

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW_ITEM) {
            return new SpecialHolder(footerView);
        } else if (viewType == HEADER_VIEW_ITEM) {
            return new SpecialHolder(headerView);
        } else {
            return onCreateView(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (headerView != null) {
            position = position - 1;
        }
        if (onItemClickListener != null && !(holder instanceof SpecialHolder)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    if (headerView != null) {
                        pos = pos - 1;
                    }
                    onItemClickListener.onItemClick(view, pos);
                }
            });
        }
        if (onItemLongClickListener != null && !(holder instanceof SpecialHolder)) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getLayoutPosition();
                    if (headerView != null) {
                        pos = pos - 1;
                    }
                    onItemLongClickListener.onItemLongClick(view, pos);
                    return false;
                }
            });
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (holder instanceof SpecialHolder && manager instanceof StaggeredGridLayoutManager) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    layoutParams.setFullSpan(true);
                    holder.itemView.setLayoutParams(layoutParams);
                }
            });
        }
        if (!(holder instanceof SpecialHolder)) {
            onBindView(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView1) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView == null) {
            recyclerView = recyclerView1;
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == HEADER_VIEW_ITEM
                            || viewType == FOOTER_VIEW_ITEM) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    private class SpecialHolder extends RecyclerView.ViewHolder {

        public SpecialHolder(View itemView) {
            super(itemView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}

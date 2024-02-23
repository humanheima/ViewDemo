package com.hm.viewdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.hm.viewdemo.databinding.ItemDragSlopBinding;
import com.hm.viewdemo.inter.OnItemClickListener;
import java.util.List;

/**
 * Created by dumingwei on 2017/3/12.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> dataList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private View footerView;
    protected static final int NORMAL_TYPE = 100;
    protected static final int FOOT_VIEW_TYPE = 200;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecycleViewAdapter(List<String> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (footerView != null) {
            return dataList.size() + 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (footerView != null && position >= getItemCount() - 1) {
            return FOOT_VIEW_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_VIEW_TYPE) {
            return new SpecialHolder(footerView);
        } else {
            ItemDragSlopBinding binding = ItemDragSlopBinding.inflate(LayoutInflater.from(context), parent, false);
            return new VH(binding);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VH) {
            ((VH) holder).text1.setText(dataList.get(position));
            if (onItemClickListener != null) {
                (holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    /**
     * 添加底部
     *
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
        footerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        notifyItemInserted(getItemCount());
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


    static class VH extends RecyclerView.ViewHolder {

        TextView text1;

        public VH(ItemDragSlopBinding itemDragSlopBinding) {
            super(itemDragSlopBinding.getRoot());
            text1 = itemDragSlopBinding.itemTextBookName;

        }
    }

    static class SpecialHolder extends RecyclerView.ViewHolder {

        public SpecialHolder(View itemView) {
            super(itemView);
        }
    }

}

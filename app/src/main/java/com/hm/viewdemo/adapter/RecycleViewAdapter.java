package com.hm.viewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.inter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        if (footerView != null)
            return dataList.size() + 1;
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_drag_slop, parent, false);
            return new VH(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VH) {
            ((VH) holder).text1.setText(dataList.get(position));
            if (onItemClickListener != null) {
                ((VH) holder).text1.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.item_text_book_name)
        TextView text1;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SpecialHolder extends RecyclerView.ViewHolder {

        public SpecialHolder(View itemView) {
            super(itemView);
        }
    }

}

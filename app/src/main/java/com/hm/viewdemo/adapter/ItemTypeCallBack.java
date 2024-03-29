package com.hm.viewdemo.adapter;

import androidx.annotation.NonNull;

/**
 * Created by dumingwei on 2017/4/19.
 */
public interface ItemTypeCallBack<T> {

    /**
     * 返回一个layoutId,例如R.id.item_diff
     *
     * @param position RecyclerView 中 的position
     * @param t        对应position上的数据
     * @return
     */
    int getHolderType(int position, @NonNull T t);
}

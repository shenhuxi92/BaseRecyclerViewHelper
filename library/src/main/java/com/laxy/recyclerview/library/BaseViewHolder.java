package com.laxy.recyclerview.library;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by YongShu on 2019/9/28
 * Last modification : 2019/9/28
 * Modification Version: 1
 * Explain: 基础 ViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews = new SparseArray<>();

    BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 查找指定控件
     */
    @SuppressWarnings("unchecked")
    public <T> T findViewById(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.append(id,view);
        }
        return (T) view;
    }

}

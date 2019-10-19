package com.laxy.recyclerview.library;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Created by YongShu on 2019/9/28
 * Last modification : 2019/9/28
 * Modification Version: 1
 * Explain: 基础的 RecyclerView Adapter
 *          实现了空数据视图显示以及相关点击事件
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int NOT_DATA = 0;
    private static final int NORMAL_DATA = 1;

    private int mEmptyView = R.layout.item_empty_data;
    private List<T> mData;

    //事件监听器
    private OnItemClickListener mItemClick;
    private OnItemLongClickListener mItemLongClick;
    private SparseArray<OnItemViewClickListener> mItemViewClicks = new SparseArray<>();

    public BaseAdapter(List<T> data) {
        mData = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new View(parent.getContext());
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NORMAL_DATA :
                view = inflater.inflate(setLayout(),parent,false);
                break;
            case NOT_DATA:
                view = inflater.inflate(mEmptyView,parent,false);
                break;
        }
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case NORMAL_DATA :
                //绑定 Item 数据
                bindViewData(holder,mData.get(position),position);
                //设置点击事件监听
                if (mItemClick != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClick.onItemClick(view,position);
                        }
                    });
                }
                //设置长按事件监听
                if (mItemLongClick != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            mItemLongClick.onItemLongClick(view,position);
                            return false;
                        }
                    });
                }
                //设置子控件点击事件监听
                for (int i = 0; i < mItemViewClicks.size(); i++) {
                    final int key = mItemViewClicks.keyAt(i);
                    View view = holder.findViewById(key);
                    if (view != null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mItemViewClicks.get(key).onItemViewClick(view,position);
                            }
                        });
                    }
                }
                break;
            case NOT_DATA:
                //绑定空数据视图
                bindEmptyView(holder.itemView);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.size() != 0) {
            return NORMAL_DATA;
        }else {
            return NOT_DATA;
        }
    }

    @Override
    public int getItemCount() {
            return mData.size()==0 ? 1 : mData.size();
    }

    /**
     * 获取数据
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 更新数据
     */
    public void updateData(List<T> data) {
        notifyItemRangeRemoved(0,mData.size());
        mData.clear();
        mData.addAll(data);
        notifyItemRangeInserted(0,mData.size());
    }

    /**
     * 添加数据
     */
    public void addData(List<T> data) {
        int position = mData.size();
        mData.addAll(data);
        notifyItemRangeChanged(position,mData.size());
    }

    /**
     * 移除数据
     */
    public void removeData(int position) {
        if (position <= mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
            //通知列表改变，否则会导致列表 Position 错误
            notifyItemRangeChanged(position,mData.size());
        }else {
            throw new RuntimeException("The value passed in cannot be greater than the length of the data collection");
        }
    }

    /**
     * 设置空数据视图
     */
    public void setEmptyLayout(@LayoutRes int resId) {
        this.mEmptyView = resId;
    }

    /**
     * 设置布局文件
     */
    @LayoutRes
    protected abstract int setLayout();

    /**
     * 绑定正常视图的数据
     */
    protected abstract void bindViewData(@NonNull BaseViewHolder holder, T data, int position);

    /**
     * 绑定空数据视图
     */
    public void bindEmptyView(View itemView) {
        //什么都不做，用来初始化空数据视图
        //比如设置空数据视图的事件监听
        //或者空数据开始动画播放等
    }

    /**
     * 设置 Item 点击事件接口
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClick = listener;
    }

    /**
     * 设置 Item 子 View 点击事件接口
     */
    public void setOnItemViewClickListener(@IdRes int idRes, OnItemViewClickListener listener) {
        mItemViewClicks.put(idRes,listener);
    }

    /**
     * 设置 Item 长按点击事件接口
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClick = listener;
    }

    /**
     * Item 点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Item 子 View 点击事件接口
     */
    public interface OnItemViewClickListener {
        void onItemViewClick(View view, int position);
    }

    /**
     * Item 长按点击事件接口
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}

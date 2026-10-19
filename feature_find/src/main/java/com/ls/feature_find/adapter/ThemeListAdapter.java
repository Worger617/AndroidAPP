package com.ls.feature_find.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.feature_find.bean.ResThemeData;
import com.ls.feature_find.databinding.ItemAnchorInfoBinding;

import java.util.List;

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ViewHolder> {
    private List<ResThemeData> mDatas;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAnchorInfoBinding binding = ItemAnchorInfoBinding.inflate(inflater, parent, false);
        return new ThemeListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResThemeData data = mDatas.get(position);
        holder.binding.setData(data);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setDatas(List<ResThemeData> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAnchorInfoBinding binding;

        public ViewHolder(ItemAnchorInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //点击第一个视频
            binding.group1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击第一个视频时，获取对应item内的数据，并从数据内拿到下标为0的视频id
                    if (mDatas != null && onItemClickListener != null) {
                        ResThemeData data = mDatas.get(getLayoutPosition());
                        ResThemeData.ListsBean bean = data.getLists().get(0);
                        int id = bean.getId();//视频id
                        onItemClickListener.onVideoClick(id);
                    }
                }
            });
            //点击第二个视频
            binding.group2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击第一个视频时，获取对应item内的数据，并从数据内拿到下标为1的视频id
                    if (mDatas != null && onItemClickListener != null) {
                        ResThemeData data = mDatas.get(getLayoutPosition());
                        ResThemeData.ListsBean bean = data.getLists().get(1);
                        int id = bean.getId();//视频id
                        onItemClickListener.onVideoClick(id);
                    }
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * 点击视频
         *
         * @param videoId 跳转到视频详情
         */
        void onVideoClick(int videoId);
    }
}

package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.mediaplayer.databinding.ItemCategoryVideoBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryVideosAdapter extends RecyclerView.Adapter<CategoryVideosAdapter.ViewHolder> {
    private List<ResCategoryVideoDetail> mDatas = new ArrayList<>();
    private ItemClickListenner mItemClickListenner;

    @NonNull
    @Override
    public CategoryVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryVideoBinding binding = ItemCategoryVideoBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    public void setDatas(List<ResCategoryVideoDetail> date) {
        this.mDatas = date;
        notifyDataSetChanged();
    }

    public void setItemClickListenner(ItemClickListenner itemClickListenner) {
        this.mItemClickListenner = itemClickListenner;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVideosAdapter.ViewHolder holder, int position) {
        ResCategoryVideoDetail info = mDatas.get(position);
        holder.binding.setData(info);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryVideoBinding binding;

        public ViewHolder(ItemCategoryVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResCategoryVideoDetail info = mDatas.get(getLayoutPosition());
                    mItemClickListenner.onItemClick(info.getId());
                }
            });
            binding.ivLikes.setOnClickListener(v -> {
                mItemClickListenner.onLikeClick(getLayoutPosition());
            });
            binding.ivCollection.setOnClickListener(v -> {
                mItemClickListenner.onCollectionClick(getLayoutPosition());
            });
            binding.ivComments.setOnClickListener(v -> {
                mItemClickListenner.onCommentClick(getLayoutPosition());
            });
        }
    }

    public interface ItemClickListenner {
        /**
         * 点击后传递被点击的视频id
         *
         * @param id
         */
        void onItemClick(int id);

        /**
         * @param position 传递数据在列表中的位置
         */
        void onLikeClick(int position);

        void onCollectionClick(int position);

        void onCommentClick(int position);
    }
}

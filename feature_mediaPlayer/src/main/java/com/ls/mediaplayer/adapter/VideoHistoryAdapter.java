package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.mediaplayer.databinding.ItemVideoHistoryBinding;
import com.ls.mediaplayer.db.VideoHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录适配器
 */
public class VideoHistoryAdapter extends RecyclerView.Adapter<VideoHistoryAdapter.ViewHolder> {
    private List<VideoHistory> mDatas = new ArrayList<>();
    private ItemClickListenner itemClickListenner;
    private Boolean mSelectStatus;

    @NonNull
    @Override
    public VideoHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVideoHistoryBinding binding = ItemVideoHistoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    public void setDatas(List<VideoHistory> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHistoryAdapter.ViewHolder holder, int position) {
        VideoHistory data = mDatas.get(position);
        holder.binding.setHistroy(data);
        holder.binding.setIsSelectStatus(mSelectStatus);
        //tag需要加入一个#号
        String tag = "# " + data.getTag();
        holder.binding.setTag(tag);

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void updateSelectStatus(Boolean selectStatus) {
        mSelectStatus = selectStatus;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoHistoryBinding binding;

        public ViewHolder(ItemVideoHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListenner != null) {
                        VideoHistory bean = mDatas.get(getLayoutPosition());
                        //这里传递的是videoId，视频id，别搞错了！！
                        itemClickListenner.onItemClick(bean.getVideoId());
                    }
                }
            });

            binding.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (itemClickListenner != null) {

                        VideoHistory videoHistory = mDatas.get(getLayoutPosition());
                        itemClickListenner.onDelSelect(videoHistory, isChecked);
                    }
                }
            });
        }
    }

    public void setItemClickListenner(ItemClickListenner itemClickListenner) {
        this.itemClickListenner = itemClickListenner;
    }

    public interface ItemClickListenner {
        /**
         * 点击后进入视频详情页
         *
         * @param videoId 被点击的视频id
         */
        void onItemClick(int videoId);

        /**
         * 编辑勾选
         *
         * @param history
         * @param isSelect
         */
        void onDelSelect(VideoHistory history, boolean isSelect);
    }
}

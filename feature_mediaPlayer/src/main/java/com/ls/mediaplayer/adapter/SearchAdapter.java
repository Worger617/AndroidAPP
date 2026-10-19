package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.utils.TimeUtils;
import com.ls.mediaplayer.databinding.ItemSearchBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页、收藏页样式相同，因此共用一个adapter
 * 但是要注意，收藏列表接口返回的数据，不是
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<ResVideoDetail.ArchivesInfoBean> mDatas = new ArrayList<>();
    private ItemClickListenner itemClickListenner;

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSearchBinding binding = ItemSearchBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    public void setDatas(List<ResVideoDetail.ArchivesInfoBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        ResVideoDetail.ArchivesInfoBean data = mDatas.get(position);
        holder.binding.setSearch(data);

        //tag需要加入一个#号
        String tag = "# " + data.getTag();
        holder.binding.setTag(tag);

        //服务端返回的时间转成年月日的形式
        String time = TimeUtils.convertTimestampToDate(data.getCreatetime());
        holder.binding.setTime(time);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSearchBinding binding;

        public ViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListenner != null) {
                        ResVideoDetail.ArchivesInfoBean bean = mDatas.get(getLayoutPosition());
                        itemClickListenner.onItemClick(bean.getId());
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
    }
}

package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResVideo;
import com.ls.mediaplayer.databinding.ItemVideoBinding;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


    private List<ResVideo> mVideos;
    private ItemClickListenner mClickListenner;
    private boolean mItemWhite;//是否要把字体改成白色

    public VideoAdapter(ItemClickListenner clickListenner) {
        this.mClickListenner = clickListenner;
//        mVideos = videos;
    }

    public void setVideos(List<ResVideo> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemVideoBinding binding = ItemVideoBinding.inflate(inflater, parent, false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ResVideo video = mVideos.get(position);
        holder.binding.setVideo(video);
        holder.binding.setWhite(mItemWhite);
        holder.binding.executePendingBindings();//实时更新数据

//        GlideUtils.loadImage(video.getImage(), holder.binding.ivBackground);
//        GlideUtils.loadCircleImage(video.getAvatar(), holder.binding.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.size();
    }

    public void setItemWhite(boolean b) {
        mItemWhite = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemVideoBinding binding;

        public ViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    ResVideo video = mVideos.get(position);
                    mClickListenner.onVideoItemClick(video.getId());
                }
            });
        }
    }

    public interface ItemClickListenner {
        /**
         * 点击后传递被点击的视频id
         *
         * @param id
         */
        void onVideoItemClick(int id);
    }
}

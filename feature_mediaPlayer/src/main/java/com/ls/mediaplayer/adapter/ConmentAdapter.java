package com.ls.mediaplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.data_video.bean.ResComment;
import com.ls.mediaplayer.databinding.ItemConmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ConmentAdapter extends RecyclerView.Adapter<ConmentAdapter.ConmentHolder> {

    private onItemClickListenner mOnItemClickListenner;
    private List<ResComment> mComments = new ArrayList<>();

    @NonNull
    @Override
    public ConmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemConmentBinding binding = ItemConmentBinding.inflate(inflater, parent, false);
        return new ConmentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConmentHolder holder, int position) {
        ResComment comments = mComments.get(position);
        holder.binding.setContents(comments);
        holder.binding.executePendingBindings();
    }

    public void setList(List<ResComment> list) {
        this.mComments = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mComments.isEmpty() ? 0 : mComments.size();
    }

    public class ConmentHolder extends RecyclerView.ViewHolder {
        private ItemConmentBinding binding;

        public ConmentHolder(ItemConmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            //长按弹出删除
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ResComment resComment = mComments.get(getLayoutPosition());
                    mOnItemClickListenner.onItemLongClick(resComment);
                    return false;
                }
            });
        }
    }

    public void setOnItemClickListenner(onItemClickListenner listenner) {
        this.mOnItemClickListenner = listenner;
    }

    public interface onItemClickListenner {
        void onItemLongClick(ResComment comment);

    }
}

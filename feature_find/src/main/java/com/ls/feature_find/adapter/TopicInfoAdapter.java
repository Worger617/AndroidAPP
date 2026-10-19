package com.ls.feature_find.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.feature_find.bean.ResTopic;
import com.ls.feature_find.databinding.ItemTopicInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class TopicInfoAdapter extends RecyclerView.Adapter<TopicInfoAdapter.ViewHolder> {
    private List<ResTopic> mDatas = new ArrayList<>();

    public void setDatas(List<ResTopic> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTopicInfoBinding binding = ItemTopicInfoBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResTopic restopic = mDatas.get(position);
        holder.binding.setTopicinfo(restopic);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDatas.isEmpty() ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTopicInfoBinding binding;

        public ViewHolder(ItemTopicInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

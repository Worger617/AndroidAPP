package com.ls.video.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MainFragmentStateAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;

    public MainFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public void setFragments(List<Fragment> fragments) {
        this.mFragments = fragments;
    }
}

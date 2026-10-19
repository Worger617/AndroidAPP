package com.ls.feature_find.ui.themelist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.adapter.ThemeListAdapter;
import com.ls.feature_find.databinding.ActivityThemeListBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

/**
 * 主题播单
 */
@Route(path = ARouterPath.Find.ACTIVITY_THEME_LIST)
public class ThemeListActivity extends BaseActivity<ActivityThemeListBinding, ThemeListViewModel> implements ThemeListAdapter.OnItemClickListener {

    private ThemeListAdapter mAdapter;

    @Override
    protected ThemeListViewModel getViewModel() {
        return new ViewModelProvider(this).get(ThemeListViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_theme_list;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.tvTitle);

        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ThemeListAdapter();
        mAdapter.setOnItemClickListener(this);
        mDataBinding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        //关联数据到recyclerview适配器
        mViewModel.getDatas().observe(this, datas -> {
            mAdapter.setDatas(datas);
        });
        //请求主题播单的数据
        mViewModel.requestData();
    }

    @Override
    public void onVideoClick(int videoId) {
        //点击列表中的视频  跳转到播放页
        ARouter.getInstance()
                .build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                .withInt(ARouterPath.Video.KEY_VIDEO_ID, videoId)
                .navigation();
    }
}
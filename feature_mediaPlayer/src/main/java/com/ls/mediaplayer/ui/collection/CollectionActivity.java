package com.ls.mediaplayer.ui.collection;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.SearchAdapter;
import com.ls.mediaplayer.databinding.ActivityCollectionBinding;

import java.util.List;

/**
 * 收藏页
 */
@Route(path = ARouterPath.Video.ACTIVITY_COLLECTION)
public class CollectionActivity extends BaseActivity<ActivityCollectionBinding, CollectionViewModel> implements SearchAdapter.ItemClickListenner {

    private SearchAdapter mAdapter;

    @Override
    protected CollectionViewModel getViewModel() {
        return new ViewModelProvider(this).get(CollectionViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_collection;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.tvTitle);

        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter();
        mAdapter.setItemClickListenner(this);
        mDataBinding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {

        mViewModel.getDatas().observe(this, new Observer<List<ResVideoDetail.ArchivesInfoBean>>() {
            @Override
            public void onChanged(List<ResVideoDetail.ArchivesInfoBean> archivesInfoBeans) {
                mAdapter.setDatas(archivesInfoBeans);
            }
        });
        mViewModel.requestDatas();
    }

    @Override
    public void onItemClick(int videoId) {
        //跳转到视频详情页
        ARouter.getInstance()
                .build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                .withInt(ARouterPath.Video.KEY_VIDEO_ID, videoId)
                .navigation();
    }
}
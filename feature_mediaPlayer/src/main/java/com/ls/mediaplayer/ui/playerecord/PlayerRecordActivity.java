package com.ls.mediaplayer.ui.playerecord;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.VideoHistoryAdapter;
import com.ls.mediaplayer.databinding.ActivityPlayerRecordBinding;
import com.ls.mediaplayer.db.VideoHistory;

import java.util.List;

@Route(path = ARouterPath.Video.ACTIVITY_PLAYRECORD)
public class PlayerRecordActivity extends BaseActivity<ActivityPlayerRecordBinding, PlayerRecordViewModel> implements VideoHistoryAdapter.ItemClickListenner {


    private VideoHistoryAdapter mAdapter;

    @Override
    protected PlayerRecordViewModel getViewModel() {
        return new ViewModelProvider(this).get(PlayerRecordViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_player_record;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.tvTitle);

        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new VideoHistoryAdapter();
        mAdapter.setItemClickListenner(this);
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mViewModel.getDatas().observe(this, new Observer<List<VideoHistory>>() {
            @Override
            public void onChanged(List<VideoHistory> videoHistories) {
                mAdapter.setDatas(videoHistories);
            }
        });

        mViewModel.getSelectStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean selectStatus) {
                mAdapter.updateSelectStatus(selectStatus);
            }
        });
        mViewModel.requestHistory();
    }

    @Override
    public void onItemClick(int videoId) {
        //跳转到视频详情页
        ARouter.getInstance()
                .build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                .withInt(ARouterPath.Video.KEY_VIDEO_ID, videoId)
                .navigation();

    }

    @Override
    public void onDelSelect(VideoHistory history, boolean isSelect) {
        mViewModel.updateDelSelectDatas(history,isSelect);
    }
}
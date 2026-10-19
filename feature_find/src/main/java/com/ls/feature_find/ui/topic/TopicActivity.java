package com.ls.feature_find.ui.topic;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.adapter.TopicInfoAdapter;
import com.ls.feature_find.databinding.ActivityTopicBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

//话题广场
@Route(path = ARouterPath.Find.ACTIVITY_TOPIC)
public class TopicActivity extends BaseActivity<ActivityTopicBinding, TopicViewModel> {

    private TopicInfoAdapter mAdapter;

    @Override
    protected TopicViewModel getViewModel() {
        return new ViewModelProvider(this).get(TopicViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivFinish, mDataBinding.ivLogo);

        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TopicInfoAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mViewModel.getDatas().observe(this, datas -> {
            mAdapter.setDatas(datas);
        });
        //请求数据
        mViewModel.requestData();
    }
}
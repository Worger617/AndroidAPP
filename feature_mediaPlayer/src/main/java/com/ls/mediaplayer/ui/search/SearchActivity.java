package com.ls.mediaplayer.ui.search;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.SearchAdapter;
import com.ls.mediaplayer.databinding.ActivitySearchBinding;

/**
 * 搜索页
 */
@Route(path = ARouterPath.Video.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel> implements SearchAdapter.ItemClickListenner {

    private SearchAdapter mAdapter;

    @Override
    protected SearchViewModel getViewModel() {
        return new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        //初始化列表
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter();
        mAdapter.setItemClickListenner(this);
        mDataBinding.recyclerView.setAdapter(mAdapter);

        //按下回车键开始搜索
        mDataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //搜索
                    mViewModel.searchByKeyword();
                    return true;
                } else {
                    return false;
                }
            }
        });

        mViewModel.getSearchKeyword().observe(this, keyword -> {
            //对搜索关键词做出监听，只要长度大于0，就改变清空按钮的显示状态
            mViewModel.update(keyword != null && keyword.length() > 0);
        });
    }

    @Override
    protected void initData() {

        //搜索后的数据绑定到适配器
        mViewModel.getDatas().observe(this, datas -> {
            mAdapter.setDatas(datas);
        });
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
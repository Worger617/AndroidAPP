package com.ls.feature_find.ui.find;


import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.bean.ResFindAnchor;
import com.ls.feature_find.databinding.LayoutFragmentFindBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.List;

@Route(path = ARouterPath.Find.FRAGMENT_FIND)
public class FindFragment extends BaseFragment<LayoutFragmentFindBinding, FindViewModel> implements CategoryAdapter.CategoryListenner {
    private static final String TAG = "FindFragment";
    private CategoryAdapter mCategoryAdapter;
    private AnchorAdapter mAnchorAdapter;

    @Override
    protected FindViewModel getViewModel() {
        return new ViewModelProvider(this).get(FindViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_find;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        Log.i(TAG, "initView");

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mDataBinding.rvCategory.setLayoutManager(layoutManager);

        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setListenner(this);
        mDataBinding.rvCategory.setAdapter(mCategoryAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//指定为横向
        mDataBinding.rvAnchor.setLayoutManager(linearLayoutManager);

        mAnchorAdapter = new AnchorAdapter();
        mAnchorAdapter.setOnItemClick(new AnchorAdapter.OnItemClickListener() {
            @Override
            public void onThemeListClick() {
                //点击主题播单的item后也跳转到主题播单页，不需要传递任何参数
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_THEME_LIST).navigation();
            }
        });
        mDataBinding.rvAnchor.setAdapter(mAnchorAdapter);

        mViewModel.getAction().observe(this, action -> {
            if (action == FindViewModel.FindAction.NAVIGATION_TO_THEME_LIST) {
                //跳转到主题播单
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_THEME_LIST).navigation();
            } else if (action == FindViewModel.FindAction.NAVIGATION_TO_TOPIC) {
                //跳转到话题广场
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_TOPIC).navigation();
            }
        });

        mDataBinding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索页
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_SEARCH).navigation();
            }
        });
    }

    @Override
    protected void initData() {

        mViewModel.loadFindData();//请求数据

        mViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<List<ResFindCategory>>() {
            @Override
            public void onChanged(List<ResFindCategory> category) {
                mCategoryAdapter.setDatas(category);
            }
        });

        mViewModel.getAnchor().observe(getViewLifecycleOwner(), new Observer<List<ResFindAnchor>>() {
            @Override
            public void onChanged(List<ResFindAnchor> anchors) {
                mAnchorAdapter.setDatas(anchors);
            }
        });

    }

    /**
     * 分类点击
     *
     * @param category 被点击的分类数据
     */
    @Override
    public void onCategroyItemClick(ResFindCategory category) {
        //跳转到分类详情，并且传递对应的分类id
        ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
                .withParcelable(ARouterPath.Find.KEY_CATEGORY_DATA, category)
                .navigation();
    }
}

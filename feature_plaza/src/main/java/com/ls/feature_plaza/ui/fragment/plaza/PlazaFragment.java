package com.ls.feature_plaza.ui.fragment.plaza;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_plaza.R;
import com.ls.feature_plaza.adapter.PlazaApater;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.feature_plaza.databinding.LayoutFragmentPlazaBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

@Route(path = ARouterPath.Plaza.FRAGMENT_PLAZA)
public class PlazaFragment extends BaseFragment<LayoutFragmentPlazaBinding, PlazaViewModel> {
    private static final String TAG = "PlazaFragment";

    private PlazaApater mAapter;

    @Override
    protected PlazaViewModel getViewModel() {
        return new ViewModelProvider(this).get(PlazaViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_plaza;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
        Log.i(TAG, "initView");
        RecyclerView recyclerView = mDataBinding.layoutRecycler.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;//如果是第一行，那就独占1行
                } else {
                    return 1;//如果是普通的item类型，那就2个item占1行
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        mAapter = new PlazaApater();
        recyclerView.setAdapter(mAapter);

        SmartRefreshLayout smartRefreshLayout = mDataBinding.layoutRecycler.smartRefreshLayout;
        //不需要加载更多
        smartRefreshLayout.setEnableLoadMore(false);
        //刷新监听
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.requestDatas();//请求数据
            }
        });

        //item点击
        mAapter.setListener(new PlazaApater.PlazaItemListener() {
            @Override
            public void onBannerClick(ResPlaza.PlazaDetail detail) {
                //FRAGMENT_CATEGORY_DETAIL页面需要ResFindCategory类型。因此在这里做数据类型转换
                ResFindCategory category = new ResFindCategory();
                category.setId(detail.getId());
                category.setName(detail.getName());
                category.setImage(detail.getImage());
                category.setDescription(detail.getDescription());
                category.setIcon(detail.getIcon());
                category.setFullurl(detail.getFullurl());
                category.setUrl(detail.getUrl());
                //跳转到分类详情，并且传递对应的分类id
                ARouter.getInstance().build(ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
                        .withParcelable(ARouterPath.Find.KEY_CATEGORY_DATA, category)
                        .navigation();
            }

            @Override
            public void onImageClick(ResPlaza.PlazaDetail detail) {
                ARouter.getInstance().build(ARouterPath.Plaza.IMAGE_ACTIVITY)
                        .withParcelable(ARouterPath.Plaza.KEY_IMAGE_DATA, detail)
                        .navigation();
                //指定页面打开时 从底部弹出的过渡动画
                getActivity().overridePendingTransition(R.anim.anim_activity_bottom2top, 0);
            }
        });

        mDataBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索页
                ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_SEARCH).navigation();
            }
        });
    }

    @Override
    protected void initData() {
        mViewModel.getDatas().observe(getViewLifecycleOwner(), new Observer<List<ResPlaza>>() {
            @Override
            public void onChanged(List<ResPlaza> data) {
                if (mDataBinding.layoutRecycler.smartRefreshLayout.isRefreshing()) {
                    mDataBinding.layoutRecycler.smartRefreshLayout.finishRefresh();
                }
                //数据请求成功
                mAapter.setDatas(data);
            }
        });

        mViewModel.getErrorCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (mDataBinding.layoutRecycler.smartRefreshLayout.isRefreshing()) {
                    mDataBinding.layoutRecycler.smartRefreshLayout.finishRefresh();
                }
            }
        });

        mViewModel.requestDatas();//请求数据
    }
}

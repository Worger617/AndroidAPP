package com.ls.mediaplayer.ui.categorylist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.libbase.base.list.BaseListFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.adapter.CategoryVideosAdapter;

import java.util.List;

@Route(path = ARouterPath.Video.FRAGMENT_CATEGORY_LIST)
public class CategoryListFragment extends BaseListFragment<ResCategoryVideoDetail> implements CategoryVideosAdapter.ItemClickListenner {

    @Autowired(name = ARouterPath.Video.KEY_CATEGORY_TYPE)
    public int mType;//区分是热门发布，还是最新发布
    @Autowired(name = ARouterPath.Video.KEY_CATEGORY_ID)
    public int mChannelId;//分类id
    private CategoryVideosAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();//记得调用super
    }

    @Override
    protected void initData() {
        //把页面type和channelid传到model，准备发起数据请求
        CategoryListViewModel viewModel = (CategoryListViewModel) mViewModel;
        viewModel.setArgments(mType, mChannelId);
        super.initData();//记得调用super
    }

    @Override
    protected CategoryListViewModel getViewModel() {
        CategoryListViewModel viewModel = new ViewModelProvider(this).get(CategoryListViewModel.class);
        return viewModel;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new CategoryVideosAdapter();
        mAdapter.setItemClickListenner(this);
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected void onDatasRequestSuccess(List<ResCategoryVideoDetail> list) {
        mAdapter.setDatas(list);
    }

    /**
     * item点击后
     *
     * @param id
     */
    @Override
    public void onItemClick(int id) {
        //跳转到视频详情页
        ARouter.getInstance()
                .build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                .withInt(ARouterPath.Video.KEY_VIDEO_ID, id)
                .navigation();
    }

    @Override
    public void onLikeClick(int position) {
        CategoryListViewModel viewModel = (CategoryListViewModel) mViewModel;
        viewModel.onLikeClick(position);
    }

    @Override
    public void onCollectionClick(int position) {

    }

    @Override
    public void onCommentClick(int position) {

    }
}

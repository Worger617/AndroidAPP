package com.ls.mediaplayer.ui.videolist;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResVideo;
import com.ls.libbase.base.list.BaseListFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.adapter.VideoAdapter;
//import com.ls.data_video.bean.ResVideo;

import java.util.List;

/**
 * 首页视频列表页fragment  列表相关的功能全部在BaseListFragment中已完成
 * 当前页面只需要做下面几件事：
 * 1、返回具体的ViewModel
 * 2、如果有重写initView、initData，记得调用super父类对应的方法，因为子类重写方法会覆盖父类中的方法
 * 3、返回recyclerview的布局管理器
 * 4、onDatasRequestSuccess中为适配器设置请求成功的数据
 */
@Route(path = ARouterPath.Video.FRAGMENT_VIDEO_LIST)
public class VideoListFragment extends BaseListFragment<ResVideo> implements VideoAdapter.ItemClickListenner {

    @Autowired(name = ARouterPath.Video.KEY_VIDEO_LIST_TYPE)
    public int mPageType;
    @Autowired(name = ARouterPath.Video.KEY_VIDEO_LIST_STYLE)
    public boolean mStyle;//是否需要把item中的文本改成白色
    private VideoAdapter mAdapter;

    @Override
    protected VideoListViewModel getViewModel() {
        return new ViewModelProvider(this).get(VideoListViewModel.class);
    }

    /**
     * 在列表形式的页面中 这个方法用处不大，可以不写
     *
     * @return
     */
    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();//记得调用super
    }

    @Override
    protected void initData() {
        VideoListViewModel model = (VideoListViewModel) mViewModel;
        model.setPageType(mPageType);
        super.initData();//记得调用super
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new VideoAdapter(this);
        if (mStyle) {
            mAdapter.setItemWhite(true);
        }
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected void onDatasRequestSuccess(List<ResVideo> list) {
        mAdapter.setVideos(list);
    }

    /**
     * 行点击
     *
     * @param id 被点击的视频id
     */
    @Override
    public void onVideoItemClick(int id) {
        //视频详情页
        ARouter.getInstance()
                .build(ARouterPath.Video.ACTIVITY_VIDEODETAIL)
                .withInt(ARouterPath.Video.KEY_VIDEO_ID, id)
                .navigation();
    }

//
//    /**
//     * 测试用的方法， 用来测试错误码，并不是实际存在的功能
//     *
//     * @param value 自定义错误码
//     */
//    public void setErrorCode(Integer value) {
//        mViewModel.getErrorCode().setValue(value);
//    }
}

package com.ls.mediaplayer.ui.videolist;

import com.ls.libbase.base.list.BaseListViewModel;
import com.ls.data_video.bean.ResVideo;

/**
 * 视频页的viewModel，在这里只需要调用父类的构造方法，并且指定Model即可
 *
 */
public class VideoListViewModel extends BaseListViewModel<ResVideo, VideoListModel> {


    private int mPageType;


    /**
     *
     */
    public VideoListViewModel() {
        //调用父类的构造方法，并且指定Model
        super(new VideoListModel());
    }

    public void setPageType(int pageType) {
        mPageType = pageType;
        mModel.setPageType(mPageType);
    }
}

package com.ls.mediaplayer.ui.videolist;

import com.ls.libbase.base.list.BaseListModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.api.MediaApiService;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.data_video.bean.ResVideo;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;

import retrofit2.Call;

public class VideoListModel extends BaseListModel {

    private static final String TAG = "VideoListModel";


    private int mPageType;

    /**
     * 请求推荐页、日报页的数据
     *
     * @param isFirst 是不是第一次加载？
     */
    @Override
    public void requestDatas(boolean isFirst) {
        if (isFirst) {
            mPage = 1;
        } else {
            mPage++;
        }
        MediaApiService apiService = MediaApiServiceProvider.getApiService();
        Call<ResBase<ResList<ResVideo>>> call;
        if (mPageType == ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND) {
            call = apiService.getRecommend(mPage, mLimit);
        } else {
            call = apiService.getDaily(mPage, mLimit);
        }

        ApiCall.enqueueLists(call, new ApiCall.ApiListsCallback() {
            @Override
            public void onSuccess(ResList result) {
                mListenner.onLoadFinish(isFirst, result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                mListenner.onLoadFailure(errorCode);
            }
        });
    }

    /**
     * 设置当前页面是推荐页 还是日报页
     *
     * @param pageType 页面的类型，可选值如下：
     *                 {@link ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND}
     *                 {@link ARouterPath.Video.VIDEO_LIST_FRAGMENT_DAILY}
     */
    public void setPageType(int pageType) {
        mPageType = pageType;
    }
}

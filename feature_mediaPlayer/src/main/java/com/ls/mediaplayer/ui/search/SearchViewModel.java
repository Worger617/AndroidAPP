package com.ls.mediaplayer.ui.search;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class SearchViewModel extends BaseViewModel {


    private static final String TAG = "SearchViewModel";

    //搜索关键词
    private MutableLiveData<String> mSearchKeyword = new MutableLiveData<>();

    //是否显示清除搜索内容的按钮 默认不显示
    private MutableLiveData<Integer> mShowCleanButton = new MutableLiveData<>(View.INVISIBLE);

    //搜索过的数据
    private MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> mDatas = new MutableLiveData<>();

    private final SearchModel mModel;

    public SearchViewModel() {
        mModel = new SearchModel();


    }

    /**
     * 清除关键词
     */
    public void cleanSearchKeyword() {
        mSearchKeyword.setValue("");
    }


    /**
     * 搜索
     */
    public void searchByKeyword() {
        String keyword = mSearchKeyword.getValue();
        if (keyword != null && keyword.length() > 0) {
            showLoading(true);
            mModel.searchByKeyword(keyword, new IRequestCallback<List<ResVideoDetail.ArchivesInfoBean>>() {
                @Override
                public void onLoadFinish(List<ResVideoDetail.ArchivesInfoBean> datas) {
                    mDatas.setValue(datas);
                    showLoading(false);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });

        } else {
            showToast("请输入搜索关键词后再试！");
        }
    }


    public MutableLiveData<String> getSearchKeyword() {
        return mSearchKeyword;
    }

    public MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> getDatas() {
        return mDatas;
    }

    public void update(boolean isShow) {
        mShowCleanButton.setValue(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public MutableLiveData<Integer> getShowCleanButton() {
        return mShowCleanButton;
    }
}

package com.ls.feature_find.ui.topic;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResTopic;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class TopicViewModel extends BaseViewModel {

    private final TopicModel mModel;
    private MutableLiveData<List<ResTopic>> mDatas = new MutableLiveData<>();

    public TopicViewModel() {
        mModel = new TopicModel();
    }

    public void requestData() {
        showLoading(true);
        mModel.requestData(new IRequestCallback<List<ResTopic>>() {
            @Override
            public void onLoadFinish(List<ResTopic> datas) {
                mDatas.setValue(datas);
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }

    public MutableLiveData<List<ResTopic>> getDatas() {
        return mDatas;
    }
}

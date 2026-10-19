package com.ls.mediaplayer.ui.collection;

import androidx.lifecycle.MutableLiveData;

import com.ls.data_video.bean.ResCollection;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class CollectionViewModel extends BaseViewModel {

    private final CollectionModel mModel;
    protected MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> mDatas = new MutableLiveData<>();

    public CollectionViewModel() {
        mModel = new CollectionModel();
    }

    public void requestDatas() {
        showLoading(true);
        mModel.requestCollectionDatas(new IRequestCallback<ResCollection>() {
            @Override
            public void onLoadFinish(ResCollection datas) {
                showLoading(false);

                List<ResVideoDetail.ArchivesInfoBean> lists = datas.getCollectionList().getData();
                mDatas.setValue(lists);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }

    public MutableLiveData<List<ResVideoDetail.ArchivesInfoBean>> getDatas() {
        return mDatas;
    }
}

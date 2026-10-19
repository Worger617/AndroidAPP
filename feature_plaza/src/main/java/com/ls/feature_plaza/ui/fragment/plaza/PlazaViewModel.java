package com.ls.feature_plaza.ui.fragment.plaza;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;

import java.util.List;

public class PlazaViewModel extends BaseViewModel implements IRequestCallback<List<ResPlaza>> {

    private final PlazaModel mModel;

    private MutableLiveData<List<ResPlaza>> mDatas = new MutableLiveData<>();

    public PlazaViewModel() {

        mModel = new PlazaModel();
    }

    /**
     * 请求广场的数据
     */
    public void requestDatas() {
        mModel.requestDatas(this);
    }

    @Override
    public void onLoadFinish(List<ResPlaza> datas) {
        mDatas.setValue(datas);
    }

    @Override
    public void onLoadFailure(int errorCode, String message) {
        getErrorCode().setValue(errorCode);
    }

    public MutableLiveData<List<ResPlaza>> getDatas() {
        return mDatas;
    }
}

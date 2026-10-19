package com.ls.feature_find.ui.themelist;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_find.bean.ResThemeData;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.bean.ResList;

import java.util.List;

public class ThemeListViewModel extends BaseViewModel {

    private final ThemeListModel mModel;
    //主题播单数据
    private MutableLiveData<List<ResThemeData>> mDatas = new MutableLiveData<>();

    public ThemeListViewModel() {
        mModel = new ThemeListModel();
    }

    public void requestData() {
        showLoading(true);
        mModel.rerequestData(new IRequestCallback<List<ResThemeData>>() {
            @Override
            public void onLoadFinish(List<ResThemeData> datas) {
                showLoading(false);
                mDatas.setValue(datas);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }

    public MutableLiveData<List<ResThemeData>> getDatas() {
        return mDatas;
    }
}

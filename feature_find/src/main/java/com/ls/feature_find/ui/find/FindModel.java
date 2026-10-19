package com.ls.feature_find.ui.find;

import com.ls.feature_find.api.FindApiService;
import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResFind;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class FindModel {


    public FindModel() {

    }

    /**
     * 加载发现页数据
     */
    public void loadFindData(IRequestCallback<ResFind> callback) {

        FindApiService apiService = FindApiServiceProvider.getApiService();
        Call<ResBase<ResFind>> call = apiService.getFindData();

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResFind>>() {
            @Override
            public void onSuccess(ResBase<ResFind> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }
}

package com.ls.feature_find.ui.categorydetail;

import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.api.FindApiServiceProvider;
import com.ls.feature_find.bean.ResCategoryDetail;
import com.ls.libbase.base.IRequestCallback;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class CategoryDetailModel {

    /**
     * 请求分类详情数据
     *
     * @param id
     */
    public void requestDatas(int id, IRequestCallback<ResFindCategory> callback) {

        Call<ResBase<ResCategoryDetail>> call = FindApiServiceProvider.getApiService().getCategoryDetail(id);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResCategoryDetail>>() {
            @Override
            public void onSuccess(ResBase<ResCategoryDetail> result) {
                //获取数据
                ResFindCategory info = result.getData().getInfo();
                callback.onLoadFinish(info);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}

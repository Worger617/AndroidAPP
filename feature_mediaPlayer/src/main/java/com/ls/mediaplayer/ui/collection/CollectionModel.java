package com.ls.mediaplayer.ui.collection;

import com.ls.data_video.bean.ResCollection;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class CollectionModel {

    /**
     * 获取当前已登录用户的收藏列表
     *
     * @param callback
     */
    public void requestCollectionDatas(IRequestCallback<ResCollection> callback) {
        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResCollection>> call = MediaApiServiceProvider.getApiService()
                .getCollectionList(token, "archives");
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResCollection>>() {
            @Override
            public void onSuccess(ResBase<ResCollection> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}

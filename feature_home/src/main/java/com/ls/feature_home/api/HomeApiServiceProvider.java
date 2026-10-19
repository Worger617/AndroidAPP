package com.ls.feature_home.api;

import com.ls.network.RetrofitProvider;

import retrofit2.Retrofit;

/**
 * home模块中的HomeApiService统一在这里获取，以便统一管理
 */
public class HomeApiServiceProvider {

    private static HomeApiService mApiService;

    //单例
    public static HomeApiService getApiService() {
        if (mApiService == null) {
            Retrofit retrofit = RetrofitProvider.provide();
            mApiService = retrofit.create(HomeApiService.class);
        }
        return mApiService;
    }
}

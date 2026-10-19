package com.ls.feature_user.ui.settings;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.manager.UserManager;
import com.ls.libbase.utils.CacheUtils;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import retrofit2.Call;

public class SettingsModel {

    /**
     * 获取缓存大小
     *
     * @return
     */
    public String getCacheSize() {
        String totalCacheSize = CacheUtils.getTotalCacheSize();
        return totalCacheSize;
    }


    /**
     * 清除缓存
     */
    public boolean clearCache() {
        boolean b = CacheUtils.clearAppCache();//清除缓存
        boolean b1 = CacheUtils.clearExternalCache();//如果需要对外部存储的数据做删除，可以自行处理外部的存储目录

        return b && b1;//如果返回true表示删除成功
    }

    public boolean isLogin() {
        return UserManager.getInstance().isLogin();//是否登录
    }

    /**
     * 如果未登录 返回null
     *
     * @return 返回手机号
     */
    public String getMobile() {
        if (isLogin()) {
            String mobile = UserManager.getInstance().getUserInfo().getUser().getUsername();
            //把username中间的4位替换成****

            StringBuilder builder = new StringBuilder();
            builder.append(mobile.substring(0, 3));
            builder.append("****");
            builder.append(mobile.substring(7));
            String string = builder.toString();
            return string;//返回手机号，userName就是手机号
        }
        return null;
    }

    /**
     * 退出登录
     */
    public void logout(IRequestCallback<ResBase<ResBase>> callback) {

        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().logout(token);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResBase>>() {
            @Override
            public void onSuccess(ResBase<ResBase> result) {
                if (result.getCode() == 1) {
                    //清除本地已登录用户数据
                    UserManager.getInstance().logout();
                    callback.onLoadFinish(result);
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}

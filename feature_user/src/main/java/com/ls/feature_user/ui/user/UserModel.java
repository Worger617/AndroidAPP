package com.ls.feature_user.ui.user;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class UserModel {

    public boolean isLogin() {
        return UserManager.getInstance().isLogin();//是否登录
    }

    /**
     * 获取用户信息
     *
     * @param callback
     */
    public void loadUserInfo(ILoadUserInfoCallback callback) {
        if (isLogin()) {
            //获取到用户信息
            ResUser userInfo = UserManager.getInstance().getUserInfo();
            if (userInfo != null) {
                callback.onLoadSuccess(userInfo);
            } else {
                callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "未登录");
            }
        } else {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "未登录");
        }
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

package com.ls.feature_user.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ReqMobileLogin;
import com.ls.feature_user.bean.ReqSendSmsCode;
import com.ls.feature_user.bean.ResLogin;
import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class LoginModel {

    /**
     * 发送验证码
     *
     * @param mobile   手机号
     * @param callback 回调
     */
    public void sendSmsCode(String mobile, IRequestCallback<ResBase<ResBase>> callback) {

        ReqSendSmsCode smsCode = new ReqSendSmsCode(mobile, "mobilelogin");
        Call<ResBase<ResBase>> call = UserApiServiceProvider.getApiService().sendSmsCode(smsCode);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResBase>>() {
            @Override
            public void onSuccess(ResBase<ResBase> result) {
                callback.onLoadFinish(result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }

    /**
     * 手机号登录
     *
     * @param mobile   手机号
     * @param code     验证码
     * @param callback 回调
     */
    public void mobileLogin(String mobile, String code, IRequestCallback<ResBase<ResLogin>> callback) {

        ReqMobileLogin login = new ReqMobileLogin(mobile, code);
        Call<ResBase<ResLogin>> call = UserApiServiceProvider.getApiService().mobileLogin(login);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResLogin>>() {
            @Override
            public void onSuccess(ResBase<ResLogin> result) {
                callback.onLoadFinish(result);

                //token存储放在model中，因为model专门处理数据
                String token = result.getData().getToken();
                UserManager userManager = UserManager.getInstance();
                userManager.saveToken(token);


                //登录成功后，表示默认同意用户协议
                SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences("agreementStatus", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("agreement_privacy", true).apply();
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }

    /**
     * 获取用户信息
     *
     * @param userId   userid
     * @param callback
     */
    public void getUserInfo(String userId, IRequestCallback<ResBase<ResUser>> callback) {

        Call<ResBase<ResUser>> call = UserApiServiceProvider.getApiService().getUserInfo(userId, "archives");

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResUser>>() {
            @Override
            public void onSuccess(ResBase<ResUser> result) {

                ResUser resUser = result.getData();
                if (resUser != null) {
                    //存储用户信息
                    UserManager.getInstance().saveUserInfo(result.getData());
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, "用户信息获取失败！");
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }
}

package com.ls.feature_user.ui.settings;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.network.bean.ResBase;

public class SettingsViewModel extends BaseViewModel {

    private SettingsModel mModel;

    //手机号
    private MutableLiveData<String> mMobile = new MutableLiveData<>();
    //缓存大小
    private MutableLiveData<String> mCacheSize = new MutableLiveData<>();

    //是否显示退出登录的按钮
    private MutableLiveData<Integer> mExitLoginBtnVisibility = new MutableLiveData<>();

    private MutableLiveData<SettingsAction> mAction = new MutableLiveData<>();


    public SettingsViewModel() {
        this.mModel = new SettingsModel();

        refreshLoginStatus();
        refreshCashSize();//显示缓存
    }

    /**
     * 第一次进入页面、以及退出登录，需要调用这个方法，刷新当前的页面显示
     */
    private void refreshLoginStatus() {
        boolean login = mModel.isLogin();
        mExitLoginBtnVisibility.setValue(login ? View.VISIBLE : View.INVISIBLE);
        mMobile.setValue(mModel.getMobile());
    }

    /**
     * 退出登录点击事件
     */
    public void onLogoutClick() {
        mAction.setValue(SettingsAction.SHOW_LOGOUT_DIALOG);
    }

    /**
     * 账号与绑定点击事件
     */
    public void onAccountBindClick() {
        if (mModel.isLogin()) {
            mAction.setValue(SettingsAction.NAVIGATION_TO_ACCOUNT);
        } else {
            mAction.setValue(SettingsAction.NAVIGATE_TO_LOGIN);
        }
    }

    /**
     * 设置密码点击事件
     */
    public void onPasswordSettingClick() {
        if (mModel.isLogin()) {
            mAction.setValue(SettingsAction.NAVIGATION_TO_PASSWORD);
        } else {
            mAction.setValue(SettingsAction.NAVIGATE_TO_LOGIN);
        }
    }

    /**
     * 推送设置点击事件
     */
    public void onPushSettingClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PUSH_SETTING);
    }

    /**
     * 播放设置点击事件
     */
    public void onPlaySettingClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PLAY_SETTING);
    }

    /**
     * 清除缓存点击事件
     */
    public void onClearCacheClick() {
        mAction.setValue(SettingsAction.SHOW_CLEAR_CACHE_DIALOG);
    }

    /**
     * 用户协议点击事件
     */
    public void onUserAgreementClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_USER_AGREEMENT);
    }

    /**
     * 隐私概要
     */
    public void onSimplePrivacyPolicyClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PRIVACY_POLICY);
    }

    /**
     * 隐私政策、点击事件
     */
    public void onPrivacyPolicyClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PRIVACY_POLICY);
    }

    /**
     * 权限设置
     */
    public void onPermissionSettingsClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_PRIVACY_POLICY);
    }


    public void onUserInfoMenusClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_USER_INFO_MENU);
    }


    /**
     * 关于我们点击事件
     */
    public void onAboutUsClick() {
        mAction.setValue(SettingsAction.NAVIGATE_TO_ABOUT_US);
    }


    /**
     * 刷新缓存大小
     */
    public void refreshCashSize() {
        String cacheSize = mModel.getCacheSize();
        mCacheSize.setValue(cacheSize);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        showLoading(true);
        boolean isSuccess = mModel.clearCache();
        if (isSuccess) {
            refreshCashSize();
            showLoading(false);
            showToast("缓存清除成功！");
        } else {
            showToast("缓存清除失败，请手动前往设置页处理！");
            showLoading(false);
        }

    }

    /**
     * 退出登录
     */
    public void exitLogin() {
        //发起退出登录的请求，然后处理结果
    }

    public MutableLiveData<SettingsAction> getAction() {
        return mAction;
    }

    public MutableLiveData<Integer> getShowExitLoginBtn() {
        return mExitLoginBtnVisibility;
    }

    public MutableLiveData<String> getCacheSize() {
        return mCacheSize;
    }

    public MutableLiveData<String> getMobile() {
        return mMobile;
    }

    /**
     * 退出登录
     * 1、清除已登录的用户信息
     * 2、告诉服务端退出登录
     */
    public void logout() {
        showLoading(true);
        mModel.logout(new IRequestCallback<ResBase<ResBase>>() {
            @Override
            public void onLoadFinish(ResBase<ResBase> datas) {
                //发送一个退出登录的状态
                MessageEvent.LoginStatusEvent.post(false);
                refreshLoginStatus();
                showToast(datas.getMsg());
                showLoading(false);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showToast(message);
                showLoading(false);
            }
        });

    }


    /**
     * 枚举
     */
    public enum SettingsAction {
        FINISH,                      // 关闭页面
        SHOW_LOGOUT_DIALOG,         // 显示退出登录的弹窗
        NAVIGATION_TO_ACCOUNT,   // 跳转到账号与绑定
        NAVIGATION_TO_PASSWORD,// 跳转到设置密码页
        NAVIGATE_TO_PUSH_SETTING,   // 跳转到推送设置
        NAVIGATE_TO_PLAY_SETTING,   // 跳转到播放设置
        SHOW_CLEAR_CACHE_DIALOG,    // 显示清除缓存对话框
        NAVIGATE_TO_USER_AGREEMENT, // 跳转到用户协议
        NAVIGATE_TO_SIMPLE_PRIVACY_POLICY, // 跳转到概要隐私政策
        NAVIGATE_TO_PRIVACY_POLICY, // 跳转到隐私政策
        NAVIGATE_TO_PERMISSION_SETTING, // 跳转到权限设置
        NAVIGATE_TO_USER_INFO_MENU, // 跳转到用户信息清单
        NAVIGATE_TO_ABOUT_US,       // 跳转到关于我们
        NAVIGATE_TO_LOGIN   // 跳转到登录页
    }

}

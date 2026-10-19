package com.ls.feature_user.ui.user;

import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.network.bean.ResBase;

public class UserViewModel extends BaseViewModel {

    private final UserModel mModel;

    private MutableLiveData<String> mAvatar = new MutableLiveData<>();
    private MutableLiveData<String> mNickName = new MutableLiveData<>();
    private MutableLiveData<String> mBio = new MutableLiveData<>();
    private MutableLiveData<String> mFans = new MutableLiveData<>();
    private MutableLiveData<String> mFollow = new MutableLiveData<>();
    private MutableLiveData<String> mMedal = new MutableLiveData<>();
    //是否显示退出登录按钮，默认不显示
    private MutableLiveData<Integer> mShowLogoutBtn = new MutableLiveData<>(View.INVISIBLE);

    private MutableLiveData<UserCenterAction> mAction = new MutableLiveData<>();

    public UserViewModel() {
        mModel = new UserModel();
        //进入到user页面后 根据登录状态更新ui
        boolean login = mModel.isLogin();
        loadUserInfo(login);
    }


    public UserModel getModel() {
        return mModel;
    }

    public MutableLiveData<String> getAvatar() {
        return mAvatar;
    }

    public MutableLiveData<String> getNickName() {
        return mNickName;
    }

    public MutableLiveData<String> getBio() {
        return mBio;
    }

    public MutableLiveData<String> getFans() {
        return mFans;
    }

    public MutableLiveData<String> getFollow() {
        return mFollow;
    }

    public MutableLiveData<String> getMedal() {
        return mMedal;
    }

    /**
     * 加载用户信息
     *
     * @param login
     */
    public void loadUserInfo(boolean login) {
        if (login) {
            showLoading(true);
            mModel.loadUserInfo(new ILoadUserInfoCallback() {
                @Override
                public void onLoadSuccess(ResUser user) {
                    showLoading(false);
                    updateUserInfo(user);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    notLoginUpdateUserInfo();
                }
            });
            //已登录时 刷新退出登录按钮的状态为可见
            mShowLogoutBtn.setValue(View.VISIBLE);
        } else {
            notLoginUpdateUserInfo();
        }
    }

    private void notLoginUpdateUserInfo() {
        //如果没登录 或者获取不到登录的信息，那么传个空的user用来更新ui
        ResUser user = new ResUser();
        user.setUser(new UserInfo());
        updateUserInfo(user);

        //没登录的时候 刷新退出登录按钮的状态
        mShowLogoutBtn.setValue(View.INVISIBLE);
    }


    /**
     * 更新用户数据的显示
     *
     * @param user
     */
    private void updateUserInfo(ResUser user) {
        String avatar = user.getUser().getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            mAvatar.setValue(avatar);
        } else {
            mAvatar.setValue("");
        }

        String nickname = user.getUser().getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            mNickName.setValue(nickname);
        } else {
            mNickName.setValue("请先登录");
        }

        String bio = user.getUser().getBio();
        if (bio != null && !bio.isEmpty()) {
            mBio.setValue(bio);
        } else {
            mBio.setValue("请编辑资料完善个人信息吧！");
        }


        int fans = user.getFans();
        mFans.setValue(fans + " 粉丝");

        int follow = user.getFollow();
        mFollow.setValue(follow + " 关注");
        int medal = user.getMedal();
        mMedal.setValue(medal + " 粉丝");
    }


    /**
     * 编辑资料
     */
    public void onEditUserInfoClick() {
        boolean login = mModel.isLogin();
        //如果未登录 就去登录页
        mAction.setValue(login ? UserCenterAction.NAVIGATION_TO_EDIT_INFO : UserCenterAction.NAVIGATE_TO_LOGIN);
    }


    /**
     * 收藏页
     */
    public void onCollectionClick() {
        //如果未登录 就去登录页
        mAction.setValue(UserCenterAction.NAVIGATION_TO_COLLECTION);

//        boolean login = mModel.isLogin();
//        //如果未登录 就去登录页
//        mAction.setValue(login ? UserCenterAction.NAVIGATION_TO_COLLECTION : UserCenterAction.NAVIGATE_TO_LOGIN);
    }

    /**
     * 播放记录页
     */
    public void onRecordClick() {
        mAction.setValue(UserCenterAction.NAVIGATION_TO_RECORD);
    }

    /**
     * 退出登录
     */
    public void onLogoutClick() {
        mAction.setValue(UserCenterAction.SHOW_LOGOUT_DIALOG);
    }

    /**
     * 设置页
     */
    public void onSettingsClick() {
        mAction.setValue(UserCenterAction.NAVIGATE_TO_SETTINGS);
    }

    /**
     * 设置页
     */
    public void onUserInfoMenuClick() {
        mAction.setValue(UserCenterAction.NAVIGATE_TO_USER_INFO_MENU);
    }

    public MutableLiveData<UserCenterAction> getAction() {
        return mAction;
    }

    public MutableLiveData<Integer> getShowLogoutBtn() {
        return mShowLogoutBtn;
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
    public enum UserCenterAction {
        SHOW_LOGOUT_DIALOG,         // 显示退出登录的弹窗
        NAVIGATION_TO_EDIT_INFO,   // 跳转到账用户信息编辑
        NAVIGATION_TO_COLLECTION,// 跳转到收藏列表页
        NAVIGATION_TO_RECORD,// 跳转到播放记录页
        NAVIGATE_TO_LOGIN,   // 跳转到登录页
        NAVIGATE_TO_SETTINGS,   // 跳转到设置页
        NAVIGATE_TO_USER_INFO_MENU   // 跳转到用户信息收公示
    }
}

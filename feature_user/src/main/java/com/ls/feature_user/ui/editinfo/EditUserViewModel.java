package com.ls.feature_user.ui.editinfo;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.ls.feature_user.bean.ResUpload;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.network.bean.ResBase;

public class EditUserViewModel extends BaseViewModel {

    private final EditUserModel mModel;

    private MutableLiveData<String> mNickName = new MutableLiveData<>();//当前昵称
    private MutableLiveData<String> mBio = new MutableLiveData<>();//当前简介
    private MutableLiveData<String> mAvatarUrl = new MutableLiveData<>();//当前头像url地址
    private MutableLiveData<EditUserAction> mAction = new MutableLiveData<>();


    public EditUserViewModel() {
        mModel = new EditUserModel();
        refresh();
    }


    /**
     * 刷新显示
     */
    private void refresh() {
        if (mModel.isLogin()) {
            UserInfo userInfo = mModel.getUserInfo();
            mNickName.setValue(userInfo.getNickname());
            mBio.setValue(userInfo.getBio());
            mAvatarUrl.setValue(userInfo.getAvatar());
        } else {
            mNickName.setValue(null);
            mBio.setValue(null);
            mAvatarUrl.setValue(null);
        }
    }


    /**
     * 保存最新的用户信息
     */
    public void onSaveUserInfo() {
        //判断用户信息是否变化
        if (isChange()) {
            showLoading(true);
            String avatarUrl = mAvatarUrl.getValue();
            String nickName = mNickName.getValue();
            String bio = mBio.getValue();
            mModel.updateUserInfo(avatarUrl, nickName, bio, new IRequestCallback<ResBase>() {
                @Override
                public void onLoadFinish(ResBase datas) {
                    showToast(datas.getMsg());
                    showLoading(false);
                    MessageEvent.LoginStatusEvent.post(true);
                    mAction.setValue(EditUserAction.FINISH);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
        }
    }

    /**
     * 更换头像
     */
    public void onAvatarSelectClick() {
        mAction.setValue(EditUserAction.SHOW_AVATAR_SELECT_DIALOG);
    }

    public MutableLiveData<String> getNickName() {
        return mNickName;
    }

    public void setNickName(MutableLiveData<String> mNickName) {
        this.mNickName = mNickName;
    }

    public MutableLiveData<String> getBio() {
        return mBio;
    }

    public void setBio(MutableLiveData<String> bio) {
        this.mBio = bio;
    }

    public MutableLiveData<String> getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(MutableLiveData<String> avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public MutableLiveData<EditUserAction> getAction() {
        return mAction;
    }


    /**
     * @return 资料是否有变化 如果有的，在按返回键的时候，会提示是否保存
     */
    public boolean isChange() {
        boolean change = false;

        UserInfo userInfo = mModel.getUserInfo();

        String avatarUrl = mAvatarUrl.getValue();
        //当头像不为空 并且和旧的资料不一致时，表示有更改
        if (avatarUrl != null && !avatarUrl.equals(userInfo.getAvatar())) {
            change = true;
        }
        String nickName = mNickName.getValue();
        if (nickName != null && !nickName.equals(userInfo.getNickname())) {
            change = true;
        }
        String bio = mBio.getValue();
        if (bio != null && !bio.equals(userInfo.getBio())) {
            change = true;
        }
        return change;
    }

    public void uploadAvatar(Uri uri) {
        showLoading(true);
        mModel.updateFile(uri, new IRequestCallback<ResUpload>() {
            @Override
            public void onLoadFinish(ResUpload datas) {
                showToast("上传成功");
                showLoading(false);
                //更新当前头像信息（注意，这一步并没有保存，只是单纯的上传了图片，获取到新的图片地址而已）
                mAvatarUrl.setValue(datas.getFullurl());
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }


    /**
     * 枚举
     */
    public enum EditUserAction {
        FINISH,                      // 关闭页面
        SHOW_AVATAR_SELECT_DIALOG
    }
}

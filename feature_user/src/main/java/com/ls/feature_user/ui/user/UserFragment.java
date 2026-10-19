package com.ls.feature_user.ui.user;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.LayoutFragmentUserBinding;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = ARouterPath.User.FRAGMENT_USER)
public class UserFragment extends BaseFragment<LayoutFragmentUserBinding, UserViewModel> {
    private static final String TAG = "UserFragment";

    @Override
    protected UserViewModel getViewModel() {
        return new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_user;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        Log.i(TAG, "initView");
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivSettings, mDataBinding.ivQualifications);

        mViewModel.getAction().observe(this, userCenterAction -> {
            switch (userCenterAction) {
                case NAVIGATE_TO_LOGIN:
                    //跳转到登录页
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN).navigation();
                    break;
                case NAVIGATION_TO_EDIT_INFO:
                    //跳转到用户信息编辑
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_EDITUSERINFO).navigation();
                    break;
                case NAVIGATION_TO_RECORD:
                    //跳转到播放记录
                    ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_PLAYRECORD).navigation();
                    break;
                case SHOW_LOGOUT_DIALOG:
                    //显示退出登录弹窗
                    showLogoutDialog();
                    break;
                case NAVIGATE_TO_SETTINGS:
                    //显示退出登录弹窗
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_SETTINGS).navigation();
                    break;
                case NAVIGATE_TO_USER_INFO_MENU:
                    //显示退出登录弹窗
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.VALUE_USER_INFO)
                            .navigation();
                    break;
                case NAVIGATION_TO_COLLECTION:
                    //收藏页
                    ARouter.getInstance().build(ARouterPath.Video.ACTIVITY_COLLECTION)
                            .navigation();
                    break;
            }
        });

    }

    @Override
    protected void initData() {

    }

    /**
     * 显示是否退出登录的弹窗
     */
    private void showLogoutDialog() {
        YesOrNoDialog.showDialog(getActivity(), "提示", "是否退出当前账号？",
                new YesOrNoDialog.Callback() {
                    @Override
                    public void onConfirm() {
                        mViewModel.logout();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 通过eventBus订阅 登录状态变化的消息
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)//表示接收粘性事件
    public void onMessageEvent(MessageEvent.LoginStatusEvent event) {
        boolean login = event.isLogin();
        Log.i(TAG, "onMessageEvent: isLogin = " + login);
        mViewModel.loadUserInfo(login);
    }

    @Override
    public void onStart() {
        super.onStart();
        //确保页面活跃的时候再接收事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //页面不活跃的时候取消事件接收
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//注销
        }
    }
}

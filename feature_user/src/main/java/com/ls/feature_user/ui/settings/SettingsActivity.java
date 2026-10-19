package com.ls.feature_user.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivitySettingsBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_SETTINGS)
public class SettingsActivity extends BaseActivity<ActivitySettingsBinding, SettingsViewModel> {
    private static final String TAG = "SettingsActivity";

    @Override
    protected SettingsViewModel getViewModel() {
        return new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());

        mViewModel.getAction().observe(this, settingsAction -> {
            switch (settingsAction) {
                case NAVIGATION_TO_ACCOUNT://账号与绑定
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_ACCOUNT).navigation();
                    break;
                case NAVIGATION_TO_PASSWORD://设置密码
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_RESETPWD).navigation();
                    break;
                case NAVIGATE_TO_PUSH_SETTING://推送设置
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_PUSHSETTINGS).navigation();
                    break;
                case NAVIGATE_TO_PLAY_SETTING://播放设置
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_PLAYTTINGS).navigation();
                    break;
                case SHOW_CLEAR_CACHE_DIALOG://清除缓存
                    //显示清除缓存的弹窗
                    showClearCacheDialog();
                    break;
                case NAVIGATE_TO_USER_AGREEMENT://用户协议
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.VALUE_AGREEMENT)
                            .navigation();
                    break;
                case NAVIGATE_TO_SIMPLE_PRIVACY_POLICY://隐私概要
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.VALUE_SIMPLE_PRIVATE)
                            .navigation();
                    break;
                case NAVIGATE_TO_PRIVACY_POLICY://隐私政策
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.VALUE_PRIVATE)
                            .navigation();
                    break;
                case NAVIGATE_TO_PERMISSION_SETTING://隐私权限设置页面
                    break;
                case NAVIGATE_TO_USER_INFO_MENU://用户信息收集清单
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT)
                            .withInt(UserConfig.AgreementType.KEY_AGREEMENT_TYPE, UserConfig.AgreementType.VALUE_USER_INFO)
                            .navigation();
                    break;
                case FINISH://关闭当前页面
                    finish();
                    break;
                case NAVIGATE_TO_ABOUT_US://关于我们
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_ABOUTME).navigation();
                    break;
                case SHOW_LOGOUT_DIALOG://退出登录
                    showLogoutDialog();
                    break;
                case NAVIGATE_TO_LOGIN://跳转到登录页
                    ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN).navigation();
                    break;
            }
        });

    }

    /**
     * 显示是否退出登录的弹窗
     */
    private void showLogoutDialog() {
        YesOrNoDialog.showDialog(this, "提示", "是否退出当前账号？",
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
     * 显示清除缓存的弹窗
     */
    private void showClearCacheDialog() {
        YesOrNoDialog.showDialog(this, "清除缓存", "是否清除当前APP相关缓存",
                new YesOrNoDialog.Callback() {
                    @Override
                    public void onConfirm() {
                        //清除缓存
                        mViewModel.clearCache();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }

    @Override
    protected void initData() {

    }
}
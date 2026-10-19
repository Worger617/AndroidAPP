package com.ls.feature_user.ui.aboutme;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.ActivityAboutMeBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

/**
 * 关于我们
 */
@Route(path = ARouterPath.User.ACTIVITY_ABOUTME)
public class AboutMeActivity extends BaseActivity<ActivityAboutMeBinding, AboutMeViewModel> {


    @Override
    protected AboutMeViewModel getViewModel() {
        return new ViewModelProvider(this).get(AboutMeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_me;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());
    }

    @Override
    protected void initData() {

    }
}
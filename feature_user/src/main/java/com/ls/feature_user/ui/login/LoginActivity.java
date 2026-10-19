package com.ls.feature_user.ui.login;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.databinding.ActivityLoginBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_LOGIN)
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    protected void initView() {
        //在这里做一些布局、ui的初始化
//        mDataBinding.main.setVisibility(View.INVISIBLE);

        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.ivSettings);

        mViewModel.getUserMobile().observe(this, mobile -> {
            mViewModel.updateEnableLoginBtnStatus();
        });
        mViewModel.getCode().observe(this, code -> {
            mViewModel.updateEnableLoginBtnStatus();
        });

        initAgreementText();


        mDataBinding.ivBack.setOnClickListener(v -> {
            finish();
        });
        mDataBinding.ivQualifications.setOnClickListener(v -> {
            ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).navigation();
        });

        mViewModel.getLoginSuccess().observe(this, isLoginSuceess -> {
            if (isLoginSuceess) {
                finish();//登录成功后 关闭当前页面
            }
        });
    }

    private void initAgreementText() {
        String string = "请阅读并同意《用户协议》和《隐私政策》";
        //借助SpannableString包装处理字符串内容
        SpannableString spannableString = new SpannableString(string);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).navigation();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.BLACK);
//                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);

            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ARouter.getInstance().build(ARouterPath.User.ACTIVITY_AGREEMENT).navigation();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.BLACK);
//                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);

            }
        };

        spannableString.setSpan(clickableSpan1, 6, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 14, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mDataBinding.cbAgreen.setText(spannableString);
        mDataBinding.cbAgreen.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void initData() {


    }

    @Override
    protected LoginViewModel getViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }


}
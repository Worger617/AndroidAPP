package com.ls.feature_user.ui.agreement;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivityAgreementBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

@Route(path = ARouterPath.User.ACTIVITY_AGREEMENT)
public class AgreementActivity extends BaseActivity<ActivityAgreementBinding, BaseViewModel> {

    private final String BASE_URL = "https://titok.fzqq.fun/";
    private final String PRIVATE_URL = BASE_URL + "agreement.html";//隐私政策、隐私概要
    private final String AGREEMENT_URL = BASE_URL + "UserAgreement.html";//用户协议
    private final String USER_INFO_URL = BASE_URL + "userinfomenus.html";//隐私政策、隐私概要

    @Autowired(name = UserConfig.AgreementType.KEY_AGREEMENT_TYPE)
    public int mType;

    @Override
    protected BaseViewModel getViewModel() {
        return new ViewModelProvider(this).get(BaseViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_agreement;
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
        mViewModel.showLoading(true);

        mDataBinding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //网页加载结束  会触发这个方法
                mViewModel.showLoading(false);
            }
        });

        String url = AGREEMENT_URL;
        switch (mType) {
            case UserConfig.AgreementType.VALUE_AGREEMENT:
                url = AGREEMENT_URL;
                mDataBinding.tvTitle.setText("用户协议");
                break;
            case UserConfig.AgreementType.VALUE_SIMPLE_PRIVATE:
            case UserConfig.AgreementType.VALUE_PRIVATE:
                url = PRIVATE_URL;
                mDataBinding.tvTitle.setText("隐私政策");
                break;
            case UserConfig.AgreementType.VALUE_USER_INFO:
                url = USER_INFO_URL;
                mDataBinding.tvTitle.setText("个人信息收集清单");
                break;
        }

        mDataBinding.webView.loadUrl(url);

    }
}
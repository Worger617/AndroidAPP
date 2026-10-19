package com.ls.video.ui.main;

import com.ls.libbase.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    private final MainModel mModel;

    public MainViewModel() {

        mModel = new MainModel();
    }

    /**
     * 保存用户同意协议的状态
     */
    public void savePricacyAgreementStatus() {
        mModel.savePricacyAgreementStatus();
    }

    public boolean getPrivacyAreementStatus() {
        return mModel.getPrivacyAreementStatus();
    }

}

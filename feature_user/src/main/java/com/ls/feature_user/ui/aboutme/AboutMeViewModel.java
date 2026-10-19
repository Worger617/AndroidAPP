package com.ls.feature_user.ui.aboutme;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;

public class AboutMeViewModel extends BaseViewModel {

    AboutMeModel mModel;
    private MutableLiveData<String> mVersionLabel = new MutableLiveData<>();

    public AboutMeViewModel() {
        this.mModel = new AboutMeModel();

        int versionCode = mModel.getVersionCode();
        String versionName = mModel.getVersionName();

        String label = "版本信息：v" + versionName + "-" + versionCode;
        mVersionLabel.setValue(label);
    }

    public MutableLiveData<String> getVersionLabel() {
        return mVersionLabel;
    }
}

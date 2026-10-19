package com.ls.video.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.ls.video.MyApplication;

public class MainModel {

    /**
     * 保存用户同意协议的状态
     */
    public void savePricacyAgreementStatus() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("agreementStatus", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("agreement_privacy", true).apply();
    }


    /**
     * 是否同意隐私协议
     *
     * @return
     */
    public boolean getPrivacyAreementStatus() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("agreementStatus", Context.MODE_PRIVATE);
        boolean privacy = preferences.getBoolean("agreement_privacy", false);
        return privacy;
    }
}

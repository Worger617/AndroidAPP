package com.ls.feature_user.ui.aboutme;

import com.ls.libbase.utils.VersionUtils;

public class AboutMeModel {

    /**
     * 获取应用的版本名称
     *
     * @param context 上下文对象
     * @return 版本名称，如果获取失败则返回空字符串
     */
    public String getVersionName() {
        String versionName = VersionUtils.getVersionName();
        return versionName;
    }

    /**
     * 获取应用的版本代码
     *
     * @return 版本代码，如果获取失败则返回 -1
     */
    public int getVersionCode() {
        int versionCode = VersionUtils.getVersionCode();
        return versionCode;
    }
}

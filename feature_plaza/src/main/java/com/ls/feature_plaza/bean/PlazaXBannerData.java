package com.ls.feature_plaza.bean;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class PlazaXBannerData implements BaseBannerInfo {

    private String imgUrl;
    private String title;
    private String description;


    public PlazaXBannerData(String imgUrl, String title, String description) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
    }

    @Override
    public String getXBannerUrl() {
        return imgUrl;
    }

    @Override
    public String getXBannerTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

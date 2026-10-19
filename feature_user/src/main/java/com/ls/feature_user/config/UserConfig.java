package com.ls.feature_user.config;

public class UserConfig {

    public static class AgreementType {
        public static final String KEY_AGREEMENT_TYPE = "KEY_AGREEMENT_TYPE";//跳转到协议页面的key

        public static final int VALUE_AGREEMENT = 0;//用户协议
        public static final int VALUE_PRIVATE = 1;//隐私政策
        public static final int VALUE_SIMPLE_PRIVATE = 2;//隐私政策概要
        public static final int VALUE_USER_INFO = 3;//个人信息收集清单
    }

    public static class Camera_CAPTURE{
        //拍摄照片的结果码
        public static final int CAPTURE_REQUEST = 1001;
        //拍摄照片的结果key
        public static final String KEY_CAPTURE_REQUEST = "KEY_CAPTURE_REQUEST";
    }
}

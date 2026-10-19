package com.ls.librumeng;

import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.BuildConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class UmengHelper {

    private static final String TAG = "UmengHelper";

    public static void init(Context context) {
        Log.i(TAG, "init: 正在初始化友盟...");
        //log日志开关
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.preInit(context, "684fbe9679267e0210868919", "lsxbugvideo");

        /**
         * 正式的初始化umemg
         * 第二个参数：appkey
         * 第三个参数：应用发布渠道（应用名）
         * 第四个参数：设备类型  一般用手机
         * 第五个参数：友盟推送相关的参数
         */
        UMConfigure.init(context, "684fbe9679267e0210868919",
                "lsxbugvideo", UMConfigure.DEVICE_TYPE_PHONE, "");


        // 微信设置
        PlatformConfig.setWeixin("wx9cd1208e8ddd6deb","4b17d92c9324fccc4106a790a4045fdd");
        PlatformConfig.setWXFileProvider("com.ls.video.fileprovider");

    }
}

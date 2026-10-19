//package com.ls.libbase.base;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.alibaba.android.arouter.BuildConfig;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.ls.librarywechat.WxApplication;
//
//import me.jessyan.autosize.AutoSizeConfig;
//
///**
// * 当前工程中的Application基类
// */
//public class BaseApplicationOld extends WxApplication {
//
//    private static Application instance;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        instance = this;
//
//        //在调试模式下
//        if (BuildConfig.DEBUG) {
//            ARouter.openLog();
//            ARouter.openDebug();
//        }
//
//        //初始化ARouter
//        ARouter.init(this);
//
//        //AndroidAutoSize的参数初始化
//        AutoSizeConfig.getInstance().setCustomFragment(true);
//    }
//
//    /**
//     * 使用Application生成了一个全局可用的context
//     * 注意不要滥用，否则会产生下面的问题
//     * 1、把Application当成是某个Activity上下文，与ui更新关联在一起，会引发错误
//     * 2、更容易获取到context，会增加项目耦合性
//     *
//     * @return
//     */
//    public static Context getContext() {
//        return instance.getApplicationContext();
//    }
//}

package com.ls.libbase.config;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.manager.UserManager;

import java.util.Arrays;
import java.util.List;

/**
 * ARouter拦截器实现登录拦截
 */
@Interceptor(priority = 1, name = "LoginInterceptor")
public class LoginInterceptor implements IInterceptor {
    private static final String TAG = "LoginInterceptor";

    // 需要登录验证的页面路由路径列表
    private static final List<String> REQUIRED_LOGIN_PATHS = Arrays.asList(
            ARouterPath.Video.ACTIVITY_COLLECTION,
            ARouterPath.User.ACTIVITY_EDITUSERINFO
            // 可以添加更多需要登录验证的页面路由路径
    );

    // 假设这个方法用于判断用户是否登录
    private boolean isUserLoggedIn() {
        // 读取登录状态
        return UserManager.getInstance().isLogin();
    }

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        String path = postcard.getPath();
        boolean notLogin = !isUserLoggedIn();//未登录
        boolean isContains = REQUIRED_LOGIN_PATHS.contains(path);//需要进行登录拦截
        if (isContains && notLogin) {
            // 如果当前跳转的页面需要登录验证且用户未登录
            ARouter.getInstance().build(ARouterPath.User.ACTIVITY_LOGIN)
                    .navigation();
            // 终止路由
            callback.onInterrupt(null);
        } else {
            // 用户已登录或者当前页面不需要登录验证，继续路由
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        // 拦截器初始化，在 Application 中会调用此方法
        Log.d(TAG, "LoginInterceptor has been initialized.");
    }
}
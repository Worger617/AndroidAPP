package com.ls.libbase.eventbus;

import org.greenrobot.eventbus.EventBus;

public class MessageEvent {

    /**
     * 登录状态变更 登录成功、退出
     */
    public static class LoginStatusEvent {
        private boolean isLogin;//是否登录

        public LoginStatusEvent(boolean isLogin) {
            this.isLogin = isLogin;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public static void post(boolean isLogin) {
            //使用粘性事件发送消息，确保订阅者在活跃的时候再处理消息
            EventBus.getDefault().postSticky(new LoginStatusEvent(isLogin));
        }

    }
}

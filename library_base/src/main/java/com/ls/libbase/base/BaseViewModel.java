package com.ls.libbase.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    //错误码
    public MutableLiveData<Integer> mErrorCode = new MutableLiveData<>();

    //如果toastText发生了变化，表示需要进行toast弹窗显示
    private MutableLiveData<String> mToastText = new MutableLiveData<>();

    //是否显示加载样式
    private MutableLiveData<Boolean> mShowLoading = new MutableLiveData<>();

    //是否需要关闭当前页面
    private MutableLiveData<Boolean> mFinish = new MutableLiveData<>();


    private MutableLiveData<Boolean> mStartLoginAction = new MutableLiveData<>();


    public void startLogin() {
        mStartLoginAction.setValue(true);
    }

    public MutableLiveData<Boolean> getStartLoginAction() {
        return mStartLoginAction;
    }

    public MutableLiveData<Boolean> getFinish() {
        return mFinish;
    }

    /**
     * 关闭页面
     */
    public void onFinishPage() {
        mFinish.setValue(true);
    }

    /**
     * 显示吐司弹窗
     *
     * @param text
     */
    public void showToast(String text) {
        if (text == null || text.equals("")) {
            return;
        }
        mToastText.setValue(text);
        mToastText.setValue(null);
    }

    public MutableLiveData<String> getToastText() {
        return mToastText;
    }

    public MutableLiveData<Integer> getErrorCode() {
        return mErrorCode;
    }

    /**
     * 是否显示弹窗
     *
     * @param b
     */
    public void showLoading(boolean b) {
        mShowLoading.setValue(b);
    }

    public MutableLiveData<Boolean> getShowLoading() {
        return mShowLoading;
    }


}

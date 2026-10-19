package com.ls.libbase.base.list;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import java.util.List;

/**
 * 所有列表页面中ViewModel的基类
 *
 * @param <T> 当前页面的列表数据类型
 * @param <V> 当前页面的Model
 */
public class BaseListViewModel<T, V extends BaseListModel> extends BaseViewModel implements IListListenner<T> {


    public V mModel;

    /**
     * 由子类通过super(new BaseListModel)调用当前父类的构造方法
     * 目的是为了让子类指定model的具体类型，顺便设置结果回调
     *
     * @param model
     */
    public BaseListViewModel(V model) {
        this.mModel = model;
        mModel.setListenner(this);
    }

    //列表需要使用的数据
    public MutableLiveData<ResList<T>> mDatas = new MutableLiveData<>();


    //是否还能继续加载更多 默认是true
    public MutableLiveData<Boolean> mIsLoadMore = new MutableLiveData<>(true);


    /**
     * 请求列表数据
     *
     * @param isFirst 是否是第一次加载
     */
    public void requestDatas(boolean isFirst) {
        if (isFirst) {
            mIsLoadMore.setValue(true);
        }
        mModel.requestDatas(isFirst);
    }

    /**
     * @return 允许外部对mDatas观察 主要是在BaseListFragment中使用
     */
    public MutableLiveData<ResList<T>> getDatas() {
        return mDatas;
    }

    /**
     * @return 允许外部对mIsLoadMore观察  主要是在BaseListFragment中使用
     */
    public MutableLiveData<Boolean> getIsLoadMore() {
        return mIsLoadMore;
    }

    /**
     * 数据加载成功的回调
     *
     * @param isFirst 是否是第一次加载
     * @param datas   数据
     */
    @Override
    public void onLoadFinish(boolean isFirst, ResList<T> datas) {
        if (isFirst) {
            //第一次加载或者是刷新的情况
            mDatas.setValue(datas);
        } else {
            //分页加载的情况
            ResList<T> value = mDatas.getValue();
            List<T> list = value.getList();
            list.addAll(datas.getList());
            mDatas.setValue(value);
        }

        //当前列表的总数
        int count = datas.getCount();
        //如果列表加载完毕则不允许继续加载更多数据
        if (mDatas.getValue().getList().size() >= count) {
            mIsLoadMore.setValue(false);
        }
    }

    /**
     * 数据加载失败
     *
     * @param statusCode 错误码
     */
    @Override
    public void onLoadFailure(int statusCode) {
        //告诉BaseFragment 状态错误，显示对应的页面
        mErrorCode.setValue(statusCode);

        //如果是因为没有更多的数据，则顺便处理mIsLoadMore的状态
        if (statusCode == ErrorStatusConfig.ERROR_STATUS_EMPTY) {
            mIsLoadMore.setValue(false);
        }
    }
}

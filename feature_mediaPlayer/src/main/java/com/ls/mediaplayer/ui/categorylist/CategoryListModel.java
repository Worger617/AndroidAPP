package com.ls.mediaplayer.ui.categorylist;

import android.util.Log;

import com.ls.data_video.bean.ReqComment;
import com.ls.data_video.bean.ReqVideoOperation;
import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.BaseListModel;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.api.MediaApiService;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class CategoryListModel extends BaseListModel {

    private static final String TAG = "CategoryListModel";

    public int mType;//区分是热门发布，还是最新发布
    public int mChannelId;//分类id

    @Override
    public void requestDatas(boolean isFirst) {
        if (isFirst) {
            mPage = 1;
        } else {
            mPage++;
        }
        String token = UserManager.getInstance().getToken();

        MediaApiService apiService = MediaApiServiceProvider.getApiService();
        Call<ResBase<ResList<ResCategoryVideoDetail>>> call = apiService.getCategoryLists(token, mType, mChannelId, mPage, mLimit);

        ApiCall.enqueueLists(call, new ApiCall.ApiListsCallback() {
            @Override
            public void onSuccess(ResList result) {
                mListenner.onLoadFinish(isFirst, result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                mListenner.onLoadFailure(errorCode);
            }
        });
    }

    public void setArgments(int type, int id) {
        mType = type;
        mChannelId = id;
    }

    public boolean isLogin() {
        return UserManager.getInstance().isLogin();//是否登录
    }

    /**
     * 发送评论
     *
     * @param id
     * @param content
     * @param callback
     */
    public void sendComment(int id, String content, IRequestCallback<ResSendComment> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }
        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        ReqComment operation = new ReqComment(id, content);
        Call<ResBase<ResSendComment>> call = MediaApiServiceProvider.getApiService().sendComment(token, operation);
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResSendComment>>() {
            @Override
            public void onSuccess(ResBase<ResSendComment> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }

    /**
     * 点赞
     *
     * @param id 需要点赞的id
     */
    public void requestLike(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().requestLike(token, new ReqVideoOperation(id, "like"));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1001) {
                    Log.i(TAG, "onResponse: 数据请求成功");
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }


    /**
     * 取消点赞
     *
     * @param id 需要取消点赞的id
     */
    public void requestCancelLike(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().requestCancelLike(token, new ReqVideoOperation(id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1) {
                    Log.i(TAG, "onResponse: 数据请求成功");
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    /**
     * 收藏
     *
     * @param id 需要点赞的id
     */
    public void requestCollection(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().requestCollection(token, new ReqVideoOperation("archives", id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1) {
                    Log.i(TAG, "onResponse: 数据请求成功");
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param id 需要取消点赞的id
     */
    public void requestCancelCollection(int id, IRequestCallback<ResLike> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }

        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();

        Call<ResLike> call = MediaApiServiceProvider.getApiService().cancelCollection(token, new ReqVideoOperation(id));
        ApiCall.enqueueCommon(call, new ApiCall.ApiCallback<ResLike>() {
            @Override
            public void onSuccess(ResLike result) {

                if (result != null && result.getCode() == 1) {
                    Log.i(TAG, "onResponse: 数据请求成功");
                    callback.onLoadFinish(result);
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_SERVER_ERROR, result.getMsg());
                }
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }


}

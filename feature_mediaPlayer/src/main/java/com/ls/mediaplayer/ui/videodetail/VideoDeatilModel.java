package com.ls.mediaplayer.ui.videodetail;

import android.util.Log;

import com.ls.data_video.bean.ReqComment;
import com.ls.data_video.bean.ReqDeleteComment;
import com.ls.data_video.bean.ReqVideoOperation;
import com.ls.data_video.bean.ResComment;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.IListListenner;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.api.MediaApiService;
import com.ls.mediaplayer.api.MediaApiServiceProvider;
import com.ls.mediaplayer.db.VideoHistory;
import com.ls.mediaplayer.db.VideoHistoryRepository;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;

import retrofit2.Call;

public class VideoDeatilModel {

    private static final String TAG = "VideoDeatilModel";

    /**
     * 评论列表数据加载时用的page
     */
    private int mCommentPage = 1;

    /**
     * 是否是第一次加载
     *
     * @param id
     * @param isFirst
     * @param listenner
     */

    public void requestComments(int id, boolean isFirst, IListListenner<ResComment> listenner) {
        if (isFirst) {
            mCommentPage = 1;
        } else {
            mCommentPage++;
        }
        MediaApiService apiService = MediaApiServiceProvider.getApiService();
        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResList<ResComment>>> call = apiService.requestComments(token, id, mCommentPage);
        ApiCall.enqueueLists(call, new ApiCall.ApiListsCallback<ResComment>() {
            @Override
            public void onSuccess(ResList<ResComment> result) {
                listenner.onLoadFinish(isFirst, result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                listenner.onLoadFailure(errorCode);
            }
        });
    }

    /**
     * 获取视频详情
     *
     * @param videoId
     * @param callback
     */
    public void requestDetail(int videoId, IRequestCallback<ResVideoDetail> callback) {
        MediaApiService apiService = MediaApiServiceProvider.getApiService();

        String token = UserManager.getInstance().getToken();
        Call<ResBase<ResVideoDetail>> call = apiService.getVideoDetail(token, videoId);

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResVideoDetail>>() {
            @Override
            public void onSuccess(ResBase<ResVideoDetail> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
    }

    /**
     * 判断当前作者id与已登录的用户 是否是同一个人
     *
     * @param authorId
     */
    public boolean isAuthor(int authorId) {
        boolean isAuthor = false;
        if (!isLogin()) {
            return isAuthor;
        }
        //当前已登录的id
        String id = UserManager.getInstance().getUserInfo().getUser().getId();
        //判断id是否相同
        isAuthor = authorId == Integer.valueOf(id);
        return isAuthor;
    }


    /**
     * 删除评论
     *
     * @param id 需要删除的评论id
     */
    public void deleteComment(int id, IRequestCallback<ResBase> callback) {

        if (!isLogin()) {
            callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NOT_LOGIN, "用户未登录");
            return;
        }
        UserManager userManager = UserManager.getInstance();
        String token = userManager.getToken();


        Call<ResBase<ReqComment>> call = MediaApiServiceProvider.getApiService()
                .deleteComment(token, new ReqDeleteComment(id));

        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ReqComment>>() {
            @Override
            public void onSuccess(ResBase<ReqComment> result) {
                callback.onLoadFinish(result);
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });
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

    public boolean isLogin() {
        boolean login = UserManager.getInstance().isLogin();
        return login;
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

    public void insertHistory(ResVideoDetail.ArchivesInfoBean archivesInfo) {

        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        //如果未登录 userid = 0
        String userId = "0";
        if (UserManager.getInstance().isLogin()) {
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }
        //生成浏览记录
        String name = archivesInfo.getChannel().getName();
        VideoHistory history = repository.generateVideoHistory(userId, archivesInfo.getId(),
                archivesInfo.getTitle(), name, archivesInfo.getDuration(), archivesInfo.getImage());

        //插入数据
        repository.insert(history);
    }
}

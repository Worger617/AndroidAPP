package com.ls.mediaplayer.ui.videodetail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ls.data_video.bean.ResComment;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.IListListenner;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;
import com.ls.network.config.ErrorStatusConfig;
import com.umeng.socialize.ShareAction;

import java.util.List;

public class VideoDeatilViewModel extends BaseViewModel {

    private static final String TAG = "VideoDeatilViewModel";

    private final VideoDeatilModel mModel;

    //视频详情数据：单一数据源
    private MutableLiveData<ResVideoDetail.ArchivesInfoBean> mVideoInfo = new MutableLiveData<>();

    //评论数据
    private MutableLiveData<List<ResComment>> mComments = new MutableLiveData<>();

    private MutableLiveData<String> mChannel = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLikes = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> mIsCollection = new MutableLiveData<>(false);
    //评论列表是否能够加载更多
    private MutableLiveData<Boolean> mIsLoadMore = new MutableLiveData<>(true);
    private MutableLiveData<Action> mAction = new MutableLiveData<>();

    public VideoDeatilViewModel() {

        mModel = new VideoDeatilModel();
    }

    /**
     * 请求视频详情
     *
     * @param videoId
     */
    public void reqeustDetail(int videoId) {
        showLoading(true);
        mModel.requestDetail(videoId, new IRequestCallback<ResVideoDetail>() {
            @Override
            public void onLoadFinish(ResVideoDetail datas) {
                showLoading(false);

                ResVideoDetail.ArchivesInfoBean archivesInfo = datas.getArchivesInfo();
                List<ResComment> comments = datas.getCommentList();
                //更新数据
                mVideoInfo.setValue(archivesInfo);
                mComments.setValue(comments);
                mIsLikes.setValue(archivesInfo.getIslike() == 1);//是否有点赞 1表示已点赞
                mIsCollection.setValue(archivesInfo.getIscollection() == 1);//1表示已收藏
                mChannel.setValue("#" + archivesInfo.getChannel().getName());

                //插入浏览记录到数据库
                mModel.insertHistory(datas.getArchivesInfo());
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }

    /**
     * 请求评论列表
     *
     * @param isFrist 是否是第一次加载（刷新） 传true
     */
    public void requestComments(boolean isFrist) {

        int id = mVideoInfo.getValue().getId();
        mModel.requestComments(id, isFrist, new IListListenner<ResComment>() {
            @Override
            public void onLoadFinish(boolean isFirst, ResList<ResComment> videos) {

                //更新本地数据
                List<ResComment> list = videos.getList();
                //是否允许加载更多，由每次返回的数据是否大于等于10条来决定
                //如果小于10条，表示列表数据加载完毕
                mIsLoadMore.setValue(list != null && list.size() >= 10);

                if (isFirst) {
                    mComments.setValue(list);
                } else {
                    List<ResComment> comments = mComments.getValue();
                    comments.addAll(list);
                    mComments.setValue(comments);
                }

            }

            @Override
            public void onLoadFailure(int statusCode) {
                if (statusCode == ErrorStatusConfig.ERROR_STATUS_EMPTY) {
                    showToast("没有更多数据了!");
                    mIsLoadMore.setValue(false);
                }
                Log.i(TAG, "onLoadFailure: statusCode" + statusCode);
            }
        });
    }

    /**
     * 删除评论
     */
    public void deleteComment(ResComment comment) {

        if (!mModel.isLogin()) {
            //未登录  不允许点赞
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }

        int userId = comment.getUser_id();//评论的作者id
        //如果不是自己的评论，无法发起删除
        if (!mModel.isAuthor(userId)) {
            showToast("不能删除别人的评论！");
            return;
        }
        showLoading(true);

        int id = comment.getId();//评论id

        mModel.deleteComment(id, new IRequestCallback<ResBase>() {
            @Override
            public void onLoadFinish(ResBase datas) {
                showToast(datas.getMsg());
                showLoading(false);

                List<ResComment> comments = mComments.getValue();
                comments.remove(comment);
                mComments.setValue(comments);
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showToast(message);
                showLoading(false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsLoadMore() {
        return mIsLoadMore;
    }

    public MutableLiveData<ResVideoDetail.ArchivesInfoBean> getVideoInfo() {
        return mVideoInfo;
    }


    public MutableLiveData<String> getChannel() {
        return mChannel;
    }

    public MutableLiveData<Boolean> getIsLikes() {
        return mIsLikes;
    }

    public MutableLiveData<Boolean> getIsCollection() {
        return mIsCollection;
    }

    public MutableLiveData<List<ResComment>> getComments() {
        return mComments;
    }

    /**
     * 点赞
     */
    public void onLikeClick() {
        if (!mModel.isLogin()) {
            //未登录  不允许点赞
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }
        //是否点赞
        Boolean isLike = mIsLikes.getValue();
        ResVideoDetail.ArchivesInfoBean info = mVideoInfo.getValue();
        int id = info.getId();
        showLoading(true);
        if (isLike) {
            mModel.requestCancelLike(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

                    //更新取消点赞后的本地数据
                    info.setIslike(0);
                    info.setLikes(info.getLikes() - 1);
                    mVideoInfo.setValue(info);
                    mIsLikes.setValue(!isLike);

                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
        } else {
            mModel.requestLike(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

                    //更新点赞后的本地数据
                    info.setIslike(1);
                    info.setLikes(info.getLikes() + 1);
                    mVideoInfo.setValue(info);
                    mIsLikes.setValue(!isLike);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
        }
    }


    /**
     * 收藏
     */
    public void onCollectionClick() {
        if (!mModel.isLogin()) {
            //未登录  不允许点收藏
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }
        //是否点赞
        Boolean isCollection = mIsCollection.getValue();
        ResVideoDetail.ArchivesInfoBean info = mVideoInfo.getValue();
        int id = info.getId();
        showLoading(true);
        if (isCollection) {
            mModel.requestCancelCollection(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

                    //更新取消收藏后的本地数据
                    info.setIscollection(0);
                    info.setCollection(info.getCollection() - 1);
                    mVideoInfo.setValue(info);
                    mIsCollection.setValue(!isCollection);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
        } else {
            mModel.requestCollection(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

                    //更新收藏后的本地数据
                    info.setIscollection(1);
                    info.setCollection(info.getCollection() + 1);
                    mVideoInfo.setValue(info);
                    mIsCollection.setValue(!isCollection);
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
        }
    }

    /**
     * 发起评论
     *
     * @param message 评论的内容
     */
    public void sendComment(String message) {
        if (!mModel.isLogin()) {
            //未登录  不允许点收藏
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }

        ResVideoDetail.ArchivesInfoBean info = mVideoInfo.getValue();
        int id = info.getId();
        showLoading(true);

        mModel.sendComment(id, message, new IRequestCallback<ResSendComment>() {
            @Override
            public void onLoadFinish(ResSendComment datas) {
                showLoading(false);
                showToast("评论成功");

                //更新评论数据
                List<ResComment> comments = mComments.getValue();
                if (comments != null) {
                    comments.add(0, datas.getComment());
                    mComments.setValue(comments);
                    info.setComments(info.getComments() + 1);
                    mVideoInfo.setValue(info);
                }
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }

    //使用 library-umeng 模块的分享调用案例
    public void share2Wechat() {
        mAction.setValue(Action.share);

    }


    //使用 library-wechat 模块的分享调用案例
//    public void share2Wechat() {

//        ResVideoDetail.ArchivesInfoBean infoBean = mVideoInfo.getValue();
//        String title = infoBean.getTitle();
//        String description = infoBean.getDescription();
//        String url = infoBean.getFullurl();//服务端返回的视频网页地址
//        Bitmap bitmap = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), com.ls.librarywechat.R.mipmap.icon_logo108);

//        WxShareManager.getInstance().shareUrl2Wx(bitmap, title, description,url,true);

//    }


    public MutableLiveData<Action> getAction() {
        return mAction;
    }

    public enum Action {
        share
    }
}

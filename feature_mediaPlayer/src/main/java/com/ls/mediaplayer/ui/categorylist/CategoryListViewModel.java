package com.ls.mediaplayer.ui.categorylist;


import com.ls.data_video.bean.ResCategoryVideoDetail;
import com.ls.data_video.bean.ResLike;
import com.ls.data_video.bean.ResSendComment;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.base.list.BaseListViewModel;
import com.ls.network.bean.ResList;

public class CategoryListViewModel extends BaseListViewModel<ResCategoryVideoDetail, CategoryListModel> {

    /**
     * 由子类通过super(new BaseListModel)调用当前父类的构造方法
     * 目的是为了让子类指定model的具体类型，顺便设置结果回调
     */
    public CategoryListViewModel() {
        super(new CategoryListModel());
    }

    public void setArgments(int type, int id) {
        mModel.setArgments(type, id);
    }

    /**
     * 点赞
     *
     * @param position 当前被点赞的视频在列表中的位置
     */
    public void onLikeClick(int position) {
        if (!mModel.isLogin()) {
            //未登录  不允许点赞
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }
        //是否点赞
        ResList<ResCategoryVideoDetail> value = mDatas.getValue();
        ResCategoryVideoDetail videoDetail = value.getList().get(position);
        Boolean isLike = videoDetail.getIslike();
        int id = videoDetail.getId();
        showLoading(true);
        if (isLike) {
            mModel.requestCancelLike(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

                    //更新列表数据
                    videoDetail.setIslike(0);
                    videoDetail.setLikes(videoDetail.getLikes() - 1);
                    mDatas.setValue(value);
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

                    //更新列表数据
                    videoDetail.setIslike(1);
                    videoDetail.setLikes(videoDetail.getLikes() + 1);
                    mDatas.setValue(value);
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
    public void onCollectionClick(ResCategoryVideoDetail detail) {
        if (!mModel.isLogin()) {
            //未登录  不允许点收藏
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }
        //是否点赞
        Boolean isCollection = detail.getIscollection();

        int id = detail.getId();
        showLoading(true);
        if (isCollection) {
            mModel.requestCancelCollection(id, new IRequestCallback<ResLike>() {
                @Override
                public void onLoadFinish(ResLike datas) {
                    showLoading(false);
                    showToast(datas.getMsg());

//                    //更新取消收藏后的本地数据
//                    info.setIscollection(0);
//                    mVideoInfo.setValue(info);
//                    mIsCollection.setValue(!isCollection);
//
//                    videoDetail.setIslike(1);
//                    videoDetail.setLikes(videoDetail.getLikes() + 1);
//                    mDatas.setValue(value);
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

//                    //更新收藏后的本地数据
//                    info.setIscollection(1);
//                    mVideoInfo.setValue(info);
//                    mIsCollection.setValue(!isCollection);
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
    public void sendComment(String message, ResCategoryVideoDetail detail) {
        if (!mModel.isLogin()) {
            //未登录  不允许点收藏
            showToast("未登录");
            startLogin();//打开登录页
            return;
        }


        int id = detail.getId();
        showLoading(true);

        mModel.sendComment(id, message, new IRequestCallback<ResSendComment>() {
            @Override
            public void onLoadFinish(ResSendComment datas) {
                showLoading(false);
                showToast("评论成功");

                //更新评论数据
//                List<ResComment> comments = mComments.getValue();
//                if (comments != null) {
//                    comments.add(0, datas.getComment());
//                    mComments.setValue(comments);
//                }
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showLoading(false);
                showToast(message);
            }
        });
    }
}

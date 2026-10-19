package com.ls.mediaplayer.ui.introduce;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.mediaplayer.databinding.FragmentIntroduceBinding;
import com.ls.mediaplayer.ui.comment.report.CommentReportPopupWindow;
import com.ls.mediaplayer.ui.videodetail.VideoDeatilViewModel;
import com.ls.mediaplayer.ui.videolist.VideoListFragment;

@Route(path = ARouterPath.Video.FRAGMENT_INTRODUCE)
public class IntroduceFragment extends BaseFragment<FragmentIntroduceBinding, VideoDeatilViewModel> {

    private static final String TAG = "IntroduceFragment";
    private CommentReportPopupWindow mPopupWindow;

    @Override
    protected VideoDeatilViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(VideoDeatilViewModel.class);
//        return new ViewModelProvider(this).get(IntroduceViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_introduce;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {

        VideoListFragment fragment = (VideoListFragment) ARouter.getInstance().build(ARouterPath.Video.FRAGMENT_VIDEO_LIST)
                .withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .withBoolean(ARouterPath.Video.KEY_VIDEO_LIST_STYLE, true)
                .navigation();

        mDataBinding.ivComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showBottomPopupwindow();
            }
        });

        getChildFragmentManager().beginTransaction().add(mDataBinding.fcv.getId(), fragment).commit();
    }

    private void showBottomPopupwindow() {
        if (mPopupWindow == null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mPopupWindow = new CommentReportPopupWindow(activity);
            mPopupWindow.setOnPopupInteractionListener(new CommentReportPopupWindow.OnPopupInteractionListener() {
//                @Override
//                public void onLikeClicked() {
//                    mViewModel.onLikeClick();
//                }
//
//                @Override
//                public void onCollectionClicked() {
//                    mViewModel.onCollectionClick();
//                }

                @Override
                public void onSendMessage(String message) {
                    mViewModel.sendComment(message);
                }
            });
        }
        mPopupWindow.showPopup(mDataBinding.getRoot());
    }

    @Override
    protected void initData() {

    }

    public void updateFragmentHeight() {

        new Handler().postDelayed(() -> {
            Log.i(TAG, "updateFragmentHeight: " + mDataBinding.getRoot().getHeight());
            //重新计算当前根布局的所有父布局（所有相关布局）的大小和位置
            mDataBinding.getRoot().requestLayout();
        }, 500);
    }



}
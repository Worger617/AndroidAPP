package com.ls.mediaplayer.ui.comment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.adapter.ConmentAdapter;
import com.ls.data_video.bean.ResComment;
import com.ls.mediaplayer.databinding.FragmentVideoCommentBinding;
import com.ls.mediaplayer.ui.comment.delcomment.DeleteCommentDialog;
import com.ls.mediaplayer.ui.videodetail.VideoDeatilViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

@Route(path = ARouterPath.Video.FRAGMENT_COMMENT)
public class VideoCommentFragment extends BaseFragment<FragmentVideoCommentBinding, VideoDeatilViewModel> {
    private static final String TAG = "VideoCommentFragment";

    private ConmentAdapter mAdapter;

    @Override
    protected VideoDeatilViewModel getViewModel() {
        //ViewModel 的生命周期、类型与 Activity 相同  共享viewmodel的数据
        VideoDeatilViewModel videModel = new ViewModelProvider(requireActivity()).get(VideoDeatilViewModel.class);
        return videModel;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_video_comment;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {

        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ConmentAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.etChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String text = mDataBinding.etChat.getText().toString().trim();
                    if (text != null && text.length() > 0) {
                        mViewModel.sendComment(text);
                        mDataBinding.etChat.getText().clear(); // 清空输入框
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        mAdapter.setOnItemClickListenner(new ConmentAdapter.onItemClickListenner() {
            @Override
            public void onItemLongClick(ResComment comment) {

                DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog();
//                deleteCommentDialog.setArguments(new Bundle(""));
                deleteCommentDialog.setComment(comment);
                deleteCommentDialog.show(getChildFragmentManager(), "DeleteCommentDialog");
            }
        });


        mDataBinding.smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新结束后  释放刷新状态
                if (mDataBinding.smartRefreshLayout.isRefreshing()) {
                    mDataBinding.smartRefreshLayout.finishRefresh();
                }
                mDataBinding.smartRefreshLayout.setEnableLoadMore(true);

                mViewModel.requestComments(true);
            }
        });

        mDataBinding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mDataBinding.smartRefreshLayout.isLoading()) {
                    mDataBinding.smartRefreshLayout.finishLoadMore();
                }
                mViewModel.requestComments(false);
            }
        });

        mViewModel.getErrorCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (mDataBinding.smartRefreshLayout.isRefreshing()) {
                    mDataBinding.smartRefreshLayout.finishRefresh();
                }
            }
        });
    }


    @Override
    protected void initData() {
        mViewModel.getIsLoadMore().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadMore) {
                mDataBinding.smartRefreshLayout.setEnableLoadMore(isLoadMore);
            }
        });
        mViewModel.getComments().observe(requireActivity(), new Observer<List<ResComment>>() {
            @Override
            public void onChanged(List<ResComment> resComments) {
                //监听到列表数据发生变化
                mAdapter.setList(resComments);
            }
        });
        //请求评论列表数据
        mViewModel.requestComments(true);
    }

    public void getFragmentHeight() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = mDataBinding.getRoot().getHeight();
                mDataBinding.getRoot().requestLayout();
                Log.i(TAG, "getFragmentHeight height: " + height);
            }
        }, 500);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 监听软键盘显示/隐藏
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 键盘显示
                    // 获取导航栏高度
                    int navigationBarHeight = 0;
                    int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
                    }
                    // 调整位置时减去状态栏和导航栏的高度
                    mDataBinding.clComment.setTranslationY(-(keypadHeight - navigationBarHeight));
                } else { // 键盘隐藏
                    mDataBinding.clComment.setTranslationY(0);
                }
            }
        });
    }

    public void updateFragmentHeight() {
        new Handler().postDelayed(() -> {
            Log.i(TAG, "updateFragmentHeight: " + mDataBinding.getRoot().getHeight());
            //重新计算当前根布局的所有父布局（所有相关布局）的大小和位置
            mDataBinding.getRoot().requestLayout();
        }, 500);


        new Handler().postDelayed(() -> {
            Log.i(TAG, "updateFragmentHeight: " + mDataBinding.getRoot().getHeight());
        }, 1500);


    }

    public void updateComments(List<ResComment> resComments) {
//        resComments
    }
}
package com.ls.mediaplayer.ui.comment.delcomment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ls.mediaplayer.R;
import com.ls.data_video.bean.ResComment;
import com.ls.mediaplayer.databinding.LayoutDeleteCommentBinding;
import com.ls.mediaplayer.ui.videodetail.VideoDeatilViewModel;

/**
 * 删除评论的弹窗
 */
public class DeleteCommentDialog extends DialogFragment {

    private LayoutDeleteCommentBinding binding;
    private ResComment mComment;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        VideoDeatilViewModel viewModel = new ViewModelProvider(requireActivity()).get(VideoDeatilViewModel.class);

        // 使用 DataBinding 方式加载布局
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.layout_delete_comment,
                null, false);

        binding.setLifecycleOwner(getActivity());

        //点击布局删除评论 并关闭自身
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteComment(mComment);
                dismiss();
            }
        });
        //关联自定义布局
        View root = binding.getRoot();
        builder.setView(root);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 获取对话框的 Window 对象
        Window window = getDialog().getWindow();
        if (window != null) {
            // 设置窗口位置居中
            window.setGravity(Gravity.CENTER);
            // 设置窗口宽高（可选）
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // 或具体数值
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    public void setComment(ResComment comment) {
        mComment = comment;
    }
}
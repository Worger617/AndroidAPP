package com.ls.feature_user.ui.editinfo;

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

import com.ls.feature_user.R;
import com.ls.feature_user.databinding.DialogLayoutPictureSelectBinding;

public class PictureSelectDialog extends DialogFragment {

    public static PictureSelectDialog newInstance() {


        PictureSelectDialog fragment = new PictureSelectDialog();
        return fragment;
    }

    private DialogLayoutPictureSelectBinding binding;

    private OnItemClickListenner onItemClickListenner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // 使用 DataBinding 方式加载布局
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_layout_picture_select,
                null, false);

        binding.setLifecycleOwner(getActivity());
        //相册
        binding.tvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListenner != null) {
                    onItemClickListenner.onAlbumClick();
                }
                dismiss();
            }
        });

        //相机
        binding.tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListenner != null) {
                    onItemClickListenner.onCameraClick();
                }
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


    public void setOnItemClickListenner(OnItemClickListenner onItemClickListenner) {
        this.onItemClickListenner = onItemClickListenner;
    }

    public interface OnItemClickListenner {

        /**
         * 点击了相机，表示需要拍摄一张照片
         */
        void onCameraClick();


        /**
         * 点击了相册，表示需要从相册获取一张照片
         */
        void onAlbumClick();
    }
}

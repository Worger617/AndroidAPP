package com.ls.libbase.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.ls.libbase.databinding.LayoutDialogYesOrNoBinding;

public class YesOrNoDialog extends DialogFragment {


    private final Callback mCallback;

    public YesOrNoDialog(Callback callback) {
        mCallback = callback;
    }

    public static void showDialog(FragmentActivity activity, String title, String content, Callback callback) {
        YesOrNoDialog yesOrNoDialog = YesOrNoDialog.newInstance(title, content, callback);
        yesOrNoDialog.show(activity.getSupportFragmentManager(), "yesorno");

    }

    private static YesOrNoDialog newInstance(String title, String content, Callback callback) {

        Bundle args = new Bundle();
        args.putString("KEY_TITLE", title);
        args.putString("KEY_CONTENT", content);

        YesOrNoDialog fragment = new YesOrNoDialog(callback);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String title = bundle.getString("KEY_TITLE");
        String content = bundle.getString("KEY_CONTENT");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //查找布局
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LayoutDialogYesOrNoBinding binding = LayoutDialogYesOrNoBinding.inflate(inflater);
        binding.tvTitle.setText(title);
        binding.tvContent.setText(content);

        binding.tvConfirm.setOnClickListener(v -> {
            mCallback.onConfirm();
            dismiss();//关闭弹窗
        });
        binding.tvCancel.setOnClickListener(v -> {
            mCallback.onCancel();
            dismiss();//关闭弹窗
        });

        //布局关联dialog
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        //设置弹窗的宽度、位置
        alertDialog.setOnShowListener(dialog -> {
            //在dialog显示之后
            WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);//设置宽度为屏幕的90%
            //更新设置
            alertDialog.getWindow().setAttributes(layoutParams);
        });

        return alertDialog;
    }


    public interface Callback {
        void onConfirm();//点击确定

        void onCancel();//点击取消
    }
}

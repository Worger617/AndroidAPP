package com.ls.feature_user.ui.editinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.OpenableColumns;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.BR;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivityEditUserInfoBinding;
import com.ls.libbase.adapter.CommonBindingAdapter;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.ui.dialog.YesOrNoDialog;
import com.ls.libbase.utils.StatusBarUtils;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 编辑用户信息
 */
@Route(path = ARouterPath.User.ACTIVITY_EDITUSERINFO)
public class EditUserInfoActivity extends BaseActivity<ActivityEditUserInfoBinding, EditUserViewModel> {

    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected EditUserViewModel getViewModel() {
        return new ViewModelProvider(this).get(EditUserViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        //沉浸式效果声明
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.tvTitle, mDataBinding.tvSave);

        mViewModel.getAction().observe(this, editUserAction -> {
            switch (editUserAction) {
                case SHOW_AVATAR_SELECT_DIALOG:
                    //显示弹窗
                    showAvatarSelectDialog();
//                    openGallery();//直接从相册获取
                    break;
                case FINISH:
                    super.finish();//调用父类finish，直接关闭
                    break;
            }
        });

        // 初始化 ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            CommonBindingAdapter.loadCircleImage(mDataBinding.ivAvatar, uri);
//                        loadImageFromUri(uri);
//                            String filePath = getFileNameFromUri(uri);
                            mViewModel.uploadAvatar(uri);
                        }
                    }
                }

        );
    }


    @Override
    protected void initData() {

    }


    @Override
    public void finish() {
        //资料发生变化，退出需要特殊处理
        if (mViewModel.isChange()) {
            YesOrNoDialog.showDialog(this, "提示", "是否保存更新？", new YesOrNoDialog.Callback() {
                @Override
                public void onConfirm() {
                    //提交保存，提交成功后会再执行super.finish
                    mViewModel.onSaveUserInfo();
                }

                @Override
                public void onCancel() {
                    //直接关闭
                    EditUserInfoActivity.super.finish();
                }
            });
        } else {
            super.finish();//一定要调用父类的finish，否则就是调用当前类中的finish
        }

    }

    /**
     * 显示选择头像的弹窗（相册、或者是相机拍摄）
     */
    public void showAvatarSelectDialog() {
        PictureSelectDialog pictureSelectDialog = PictureSelectDialog.newInstance();

        pictureSelectDialog.setOnItemClickListenner(new PictureSelectDialog.OnItemClickListenner() {
            @Override
            public void onCameraClick() {

                // 请求相机权限，如果有权限直接启动相机
                if (allPermissionsGranted()) {
                    startCamera();
                } else {
                    ActivityCompat.requestPermissions(EditUserInfoActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                }

            }

            @Override
            public void onAlbumClick() {
                openGallery();
            }
        });

        pictureSelectDialog.show(getSupportFragmentManager(), "PictureSelectDialog");
    }




    /**
     * 打开相册
     */
    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    // 检查是否已经获得所有权限
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // 权限请求结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                mViewModel.showToast("权限获取失败，请在系统设置手动授权");
                finish();
            }
        }
    }

    private void startCamera() {
        //需要实现接受回传值：
        //1、navigation(this,100);需要指定请求码  2、在onActivityResult中获取结果
        ARouter.getInstance().build(ARouterPath.User.ACTIVITY_CAMERA).navigation(this,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果结果码与拍摄照片相同
        if (UserConfig.Camera_CAPTURE.CAPTURE_REQUEST == resultCode){
            //获取照片拍摄结果
            Uri uri = data.getParcelableExtra(UserConfig.Camera_CAPTURE.KEY_CAPTURE_REQUEST);
            mViewModel.uploadAvatar(uri);
        }
    }

    //
//    private void loadImageFromUri(Uri uri) {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//            CommonBindingAdapter.loadCircleImage(mDataBinding.ivAvatar, uri);
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    // 工具方法：从 Uri 获取文件名（兼容 content:// 和 file://）
//    private String getFileNameFromUri(Uri uri) {
//        String fileName = null;
//        if (uri.getScheme().equals("content")) {
//            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
//                if (cursor != null && cursor.moveToFirst()) {
//                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    if (nameIndex != -1) {
//                        fileName = cursor.getString(nameIndex);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (uri.getScheme().equals("file")) {
//            fileName = new File(uri.getPath()).getName();
//        }
//        return fileName != null ? fileName : "unnamed_file";
//    }
}
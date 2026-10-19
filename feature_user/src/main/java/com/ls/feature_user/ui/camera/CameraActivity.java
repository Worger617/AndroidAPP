package com.ls.feature_user.ui.camera;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.common.util.concurrent.ListenableFuture;
import com.ls.feature_user.R;
import com.ls.feature_user.config.UserConfig;
import com.ls.feature_user.databinding.ActivityCameraBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.concurrent.ExecutionException;

/**
 * 相机拍照
 *
 * 注意：进入到这个页面之前，需要先处理相机权限的获取！
 */
@Route(path = ARouterPath.User.ACTIVITY_CAMERA)
public class CameraActivity extends BaseActivity<ActivityCameraBinding,CameraViewModel> {

    private static final String TAG = "CameraActivity";

    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private boolean isFrontCamera = false;  // 默认后置摄像头
    @Override
    protected CameraViewModel getViewModel() {
        return new ViewModelProvider(this).get(CameraViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_camera;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());


        startCamera();

        // 点击拍照按钮时执行拍照
        mDataBinding.captureButton.setOnClickListener(view -> takePicture());
        // 启动相机
        startCamera();

        // 切换摄像头按钮点击事件
       mDataBinding.captureSwitch.setOnClickListener(v -> {
            isFrontCamera = !isFrontCamera;
            switchCamera();
        });
    }


    private void startCamera() {
        // 获取 CameraProvider
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                // 默认使用后置摄像头
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                bindCameraUseCases();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        // 创建预览
        Preview preview = new Preview.Builder().build();

        // 将 Preview 连接到 PreviewView
        preview.setSurfaceProvider(mDataBinding.previewView.getSurfaceProvider());

        // 拍照设置
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // 解绑之前的所有用例
        cameraProvider.unbindAll();

        // 绑定预览和拍照功能到相机
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    private void switchCamera() {
        // 切换前后置摄像头
        if (isFrontCamera) {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        }

        // 重新绑定摄像头
        bindCameraUseCases();
    }

    // 拍照方法
    private void takePicture() {
        if (imageCapture != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "my_image_" + System.currentTimeMillis() + ".jpg");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                            Uri savedUri = outputFileResults.getSavedUri();

                            Intent intent = new Intent();
                            //因为Uri实现了Parcelable，所以也可以使用intent传递
                            intent.putExtra(UserConfig.Camera_CAPTURE.KEY_CAPTURE_REQUEST,savedUri);
                            setResult(UserConfig.Camera_CAPTURE.CAPTURE_REQUEST, intent);
                            finish();
                            Log.d("CameraXApp", "Image saved: " + savedUri);
                        }

                        @Override
                        public void onError(ImageCaptureException exception) {
                            Log.e("CameraXApp", "Error saving image: " + exception.getMessage());
                        }
                    });
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
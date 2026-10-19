package com.ls.feature_user.ui.editinfo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.Nullable;

import com.ls.feature_user.api.UserApiServiceProvider;
import com.ls.feature_user.bean.ReqUpdateUserProfile;
import com.ls.feature_user.bean.ResUpload;
import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.bean.UserInfo;
import com.ls.libbase.manager.UserManager;
import com.ls.network.ApiCall;
import com.ls.network.bean.ResBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;

public class EditUserModel {


    public boolean isLogin() {
        return UserManager.getInstance().isLogin();
    }

    /**
     * @return 返回用户信息
     */
    public UserInfo getUserInfo() {

        UserInfo userInfo = null;
        if (isLogin()) {
            userInfo = UserManager.getInstance().getUserInfo().getUser();
        }
        return userInfo;
    }

    /**
     * 上传文件
     *
     * @param uri
     * @param callback
     */
    public void updateFile(Uri uri, IRequestCallback<ResUpload> callback) {

        MultipartBody.Part multipartBody = createMultipartBody(uri);

        String token = UserManager.getInstance().getToken();
        // 调用 FileUploadService 接口的方法，传入文件部分
        Call<ResBase<ResUpload>> call = UserApiServiceProvider.getApiService().uploadFile(token, multipartBody);
        //异步执行文件上传请求
        ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<ResUpload>>() {
            @Override
            public void onSuccess(ResBase<ResUpload> result) {
                callback.onLoadFinish(result.getData());
            }

            @Override
            public void onError(int errorCode, String meesage) {
                callback.onLoadFailure(errorCode, meesage);
            }
        });

    }

    public void updateUserInfo(String avatarUrl, String nickName, String bio, IRequestCallback<ResBase> callback) {
        if (isLogin()) {
            ReqUpdateUserProfile userProfile = new ReqUpdateUserProfile();
            userProfile.setAvatar(avatarUrl);
            userProfile.setNickname(nickName);
            userProfile.setBio(bio);

            String token = UserManager.getInstance().getToken();

            Call<ResBase<Void>> call = UserApiServiceProvider.getApiService().updateUserProfile(token, userProfile);
            ApiCall.enqueue(call, new ApiCall.ApiCallback<ResBase<Void>>() {
                @Override
                public void onSuccess(ResBase<Void> result) {
                    //把新数据更新到UserManager
                    UserManager.getInstance().updateUserInfo(avatarUrl, nickName, bio);
                    callback.onLoadFinish(result);
                }

                @Override
                public void onError(int errorCode, String meesage) {
                    callback.onLoadFailure(errorCode, meesage);
                }
            });

        }
    }

    /**
     * 根据Uri生成一个上传的对象
     * 创建了一个自定义的 RequestBody 对象，用于将 Uri 指向的文件内容以流式方式上传到服务器
     * @param uri
     */
    public MultipartBody.Part createMultipartBody(Uri uri) {
        // 1. 获取 MIME 类型
        ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();
        String mimeType = contentResolver.getType(uri);

        // 2. 创建流式 RequestBody
        RequestBody requestBody = new RequestBody() {

            // 定义内容类型（MIME）
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse(mimeType);
            }
            // 将文件内容写入网络请求流
            @Override
            public void writeTo(BufferedSink sink) throws IOException {

//                将文件内容分块写入网络请求流，避免一次性加载大文件到内存
                try (InputStream inputStream = contentResolver.openInputStream(uri)) {
                    byte[] buffer = new byte[4096];// 每次读取 4KB 缓冲区
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        sink.write(buffer, 0, read);// 写入到请求流
                    }
                }
            }
        };

        // 3. 构建 MultipartBody.Part（包含文件名）
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "file", // 表单字段名
                getFileNameFromUri(uri), // 从 Uri 获取文件名
                requestBody
        );
        return filePart;
    }

    // 工具方法：从 Uri 获取文件名（兼容 content:// 和 file://）
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName != null ? fileName : "unnamed_file";
    }
}

package com.ls.librarywechat;


import android.graphics.Bitmap;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;

public class WxShareManager {

    private static WxShareManager mInstance;

    private WxShareManager() {
        //no instance
    }

    public static WxShareManager getInstance() {

        if (mInstance == null) {
            synchronized (WxShareManager.class) {
                if (mInstance == null) {
                    mInstance = new WxShareManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 分享网页链接到微信好有
     *
     * @param bitmap      图片
     * @param title       标题
     * @param description 描述
     * @param url         点开卡片后的网页地址
     * @param isFriend    true分享给好友  false分享到朋友圈
     */
    public void shareUrl2Wx(Bitmap bitmap, String title, String description, String url, boolean isFriend) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;// 特定情况下可能不展示
        msg.thumbData = compressImage(bitmap);//thumbData建议压缩候再传给微信

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        //指定分享给好友还是朋友圈
        req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneFavorite;

        //调用api接口，发送数据到微信
        IWXAPI api = WxApplication.getApi();
        api.sendReq(req);
    }


    /**
     * 把图片压缩到100kb以下，如果压缩到质量为0的程度后还是大于100kb，也会直接返回
     *
     * @param bitmap
     * @return
     */
    public byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;

        // 循环压缩直到满足大小要求
        do {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        } while (baos.toByteArray().length > 100 * 1024 && quality > 0);

        return baos.toByteArray();
    }

    /**
     * 分享文字到微信
     *
     * @param text
     */
    public void shareText2WxFriends(String text) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        ////transaction字段用与唯一标示一个请求
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;//指定分享给某个好友
        //调用api接口，发送数据到微信
        IWXAPI api = WxApplication.getApi();
        api.sendReq(req);
    }
}

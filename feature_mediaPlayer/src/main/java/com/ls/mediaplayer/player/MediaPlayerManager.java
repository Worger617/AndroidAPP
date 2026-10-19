package com.ls.mediaplayer.player;

import android.content.Context;
import android.net.Uri;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

/**
 * media3的封装
 */
public class MediaPlayerManager {

    //volatile保证线程安全
    private static volatile MediaPlayerManager instance;
    private ExoPlayer mPlayer;

    private MediaPlayerManager(Context context) {
        //这里的上下文绑定的是 Application
        mPlayer = new ExoPlayer.Builder(context.getApplicationContext()).build();
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MediaPlayerManager.class) {
                if (instance == null) {
                    instance = new MediaPlayerManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 绑定播放视图
     *
     * @param playerView
     */
    public void bindPlayerView(PlayerView playerView) {
        if (mPlayer != null && playerView != null) {
            playerView.setPlayer(mPlayer);
        }
    }

    public ExoPlayer getPlayer() {
        return mPlayer;
    }

    /**
     * 播放指定 URL
     */
    public void play(String url) {
        if (mPlayer != null) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            mPlayer.setMediaItem(mediaItem);
            mPlayer.prepare();
            mPlayer.play();

            mPlayer.seekToNextMediaItem();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    /**
     * true：如果所有其他条件（如缓冲完成、音视频轨道就绪等）满足，播放器就会开始播放。
     * false：播放器会进入 暂停状态，但不会释放资源，恢复后可以继续播放。
     *
     * @param playWhenReady
     */
    public void setPlayWhenReady(boolean playWhenReady) {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            instance = null;
        }
    }
}

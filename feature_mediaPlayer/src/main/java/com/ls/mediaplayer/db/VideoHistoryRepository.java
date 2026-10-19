package com.ls.mediaplayer.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.room.Room;

import com.ls.libbase.base.IRequestCallback;
import com.ls.network.config.ErrorStatusConfig;

import java.util.Date;
import java.util.List;


/**
 * 专门操作视频浏览记录数据库的类
 */
public class VideoHistoryRepository {

    private static final String TAG = "VideoHistoryRepository";
    private final AppDatabase mAppDatabase;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public VideoHistoryRepository(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "Video_hostory_database")
                .build();
    }

    /**
     * 生成一条浏览记录
     *
     * @param userId
     * @param videoId
     * @param title
     * @param tag
     * @param duration
     * @return
     */
    public VideoHistory generateVideoHistory(String userId, int videoId, String title, String tag, String duration, String cover) {

        //获取当前的时间戳
        long viewTime = new Date().getTime();
        VideoHistory videoHistory = new VideoHistory();
        videoHistory.setUserId(userId);
        videoHistory.setVideoId(videoId);
        videoHistory.setTitle(title);
        videoHistory.setTag(tag);
        videoHistory.setDuration(duration);
        videoHistory.setViewTime(viewTime);
        videoHistory.setCover(cover);

        return videoHistory;
    }

    /**
     * 通过userid、videoid删除单条记录
     *
     * @param userId
     */
    public void delete(String userId, int videoId, IRequestCallback<String> callback) {
        new Thread(() -> {
            int i = mAppDatabase.videoHistoryDao().deleteVideoHistory(userId, videoId);
            mainHandler.post(() -> {
                //可以在这里执行主线程操作，比如更新ui
                if (i > 0) {
                    callback.onLoadFinish("成功删除" + i + "条数据");
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NORMAL, "删除失败");
                }
            });
        }).start();
    }


    /**
     * 通过userid、videoid列表删除多条记录
     *
     * @param userId
     */
    public void deleteByIds(String userId, List<Integer> videoIds, IRequestCallback<String> callback) {
        new Thread(() -> {
            int i = mAppDatabase.videoHistoryDao().deleteVideoHistoryByIds(userId, videoIds);
            mainHandler.post(() -> {
                //可以在这里执行主线程操作，比如更新ui
                if (i > 0) {
                    callback.onLoadFinish("删除成功");
                } else {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_NORMAL, "删除失败");
                }
            });
        }).start();
    }


    /**
     * 通过userid查询浏览记录
     *
     * @param userId
     */
    public void query(String userId, IRequestCallback<List<VideoHistory>> callback) {
        new Thread(() -> {
            List<VideoHistory> videoHistorys = mAppDatabase.videoHistoryDao().getVideoHistorys(userId);
            mainHandler.post(() -> {
                //可以在这里执行主线程操作，比如更新ui
                if (videoHistorys == null || videoHistorys.size() == 0) {
                    callback.onLoadFailure(ErrorStatusConfig.ERROR_STATUS_EMPTY, "没有历史浏览记录");
                } else {
                    callback.onLoadFinish(videoHistorys);
                }
            });
        }).start();
    }

    /**
     * 插入浏览记录
     */
    public void insert(VideoHistory history) {
        //子线程完成耗时操作
        new Thread(() -> {

            VideoHistory existing = mAppDatabase.videoHistoryDao().getVideoHistoryByUserAndVideo(
                    history.getUserId(), history.getVideoId());

            if (existing != null) {
                //更新浏览时间
                mAppDatabase.videoHistoryDao().updateViewTime(
                        history.getUserId(), history.getVideoId(), history.getViewTime());
                Log.i(TAG, "insert: videoId =" + history.getVideoId() + "已更新");
            } else {
                //表示之前没有这条记录， 插入新数据
                mAppDatabase.videoHistoryDao().insertVideoHistory(history);
                Log.i(TAG, "insert: videoId =" + history.getVideoId() + "已插入");
            }
            mainHandler.post(() -> {
                //可以在这里执行主线程操作，比如更新ui
                //但是插入操作不需要更新ui
            });

        }).start();

    }
}

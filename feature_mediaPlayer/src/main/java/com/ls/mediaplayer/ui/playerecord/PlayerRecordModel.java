package com.ls.mediaplayer.ui.playerecord;

import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.base.IRequestCallback;
import com.ls.libbase.manager.UserManager;
import com.ls.mediaplayer.db.VideoHistory;
import com.ls.mediaplayer.db.VideoHistoryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PlayerRecordModel {
    public void requestHistory(IRequestCallback<List<VideoHistory>> callback) {

        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        //通过usedid来查询浏览记录
        String userId = "0";
        if (UserManager.getInstance().isLogin()) {
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }
        //查询
        repository.query(userId, callback);
    }

    public void deleteByIds(HashMap<VideoHistory, Boolean> selectDelDatas, IRequestCallback<String> callback) {
        VideoHistoryRepository repository = new VideoHistoryRepository(BaseApplication.getContext());
        //通过usedid来查询浏览记录
        String userId = "0";
        if (UserManager.getInstance().isLogin()) {
            userId = UserManager.getInstance().getUserInfo().getUser().getId();
        }

        List<Integer> ids = new ArrayList<>();

        Set<VideoHistory> histories = selectDelDatas.keySet();
        for (VideoHistory history : histories) {
            ids.add(history.getVideoId());
        }

        repository.deleteByIds(userId, ids, callback);
    }
}

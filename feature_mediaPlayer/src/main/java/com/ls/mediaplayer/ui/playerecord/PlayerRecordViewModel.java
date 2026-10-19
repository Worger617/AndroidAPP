package com.ls.mediaplayer.ui.playerecord;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ls.libbase.base.BaseViewModel;
import com.ls.libbase.base.IRequestCallback;
import com.ls.mediaplayer.db.VideoHistory;

import java.util.HashMap;
import java.util.List;

public class PlayerRecordViewModel extends BaseViewModel {

    private static final String TAG = "PlayerRecordViewModel";

    private final PlayerRecordModel mModel;
    private MutableLiveData<List<VideoHistory>> mDatas = new MutableLiveData<>();

    private HashMap<VideoHistory, Boolean> mSelectDelDatas;

    //当前是否处于勾选删除的状态
    private MutableLiveData<Boolean> mSelectStatus = new MutableLiveData<>(false);

    public PlayerRecordViewModel() {

        mModel = new PlayerRecordModel();
    }

    public void requestHistory() {
        showLoading(true);
        mModel.requestHistory(new IRequestCallback<List<VideoHistory>>() {
            @Override
            public void onLoadFinish(List<VideoHistory> datas) {
                //指定数据
                mDatas.setValue(datas);
                showLoading(false);
                Log.i(TAG, "onLoadFinish: datas size = " + datas.size());
            }

            @Override
            public void onLoadFailure(int errorCode, String message) {
                showToast(message);
                showLoading(false);
                Log.i(TAG, "onLoadFinish: errorCode = 3  没有数据");
            }
        });
    }

    /**
     * 多选操作
     */
    public void onSelectClick() {
        //如果不是勾选状态
        if (!mSelectStatus.getValue()) {
            mSelectStatus.setValue(true);
            //开始编辑前，初始化记录勾选数据的hashmap
            mSelectDelDatas = new HashMap<>();
        } else {
            mSelectStatus.setValue(false);
            showLoading(true);
            mModel.deleteByIds(mSelectDelDatas, new IRequestCallback<String>() {
                @Override
                public void onLoadFinish(String datas) {
                    showToast(datas);
                    requestHistory();
                }

                @Override
                public void onLoadFailure(int errorCode, String message) {
                    showLoading(false);
                    showToast(message);
                }
            });
            mSelectDelDatas = null;
        }
    }

    /**
     * 勾选或取消勾选了某条数据
     *
     * @param history
     * @param isSelect
     */
    public void updateDelSelectDatas(VideoHistory history, boolean isSelect) {
        //取消选中
        if (mSelectDelDatas.containsKey(history) && !isSelect) {
            mSelectDelDatas.remove(history);
            Log.i(TAG, "updateDelSelectDatas: 取消选中 id =" + history.getVideoId());
        } else {
            //加入列表
            mSelectDelDatas.put(history, isSelect);
            Log.i(TAG, "updateDelSelectDatas: 加入选中 id =" + history.getVideoId());
        }
    }

    public MutableLiveData<Boolean> getSelectStatus() {
        return mSelectStatus;
    }

    public MutableLiveData<List<VideoHistory>> getDatas() {
        return mDatas;
    }


}

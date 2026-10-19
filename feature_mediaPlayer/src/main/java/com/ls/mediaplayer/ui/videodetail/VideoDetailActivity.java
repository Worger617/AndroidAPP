package com.ls.mediaplayer.ui.videodetail;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResVideoDetail;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.eventbus.MessageEvent;
import com.ls.libbase.utils.StatusBarUtils;
import com.ls.mediaplayer.BR;
import com.ls.mediaplayer.R;
import com.ls.mediaplayer.databinding.ActivityVideoDetailBinding;
import com.ls.mediaplayer.player.MediaPlayerManager;
import com.ls.mediaplayer.ui.comment.VideoCommentFragment;
import com.ls.mediaplayer.ui.introduce.IntroduceFragment;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

@Route(path = ARouterPath.Video.ACTIVITY_VIDEODETAIL)
public class VideoDetailActivity extends BaseActivity<ActivityVideoDetailBinding, VideoDeatilViewModel> {

    private static final String TAG = "VideoDetailActivity";

    @Autowired(name = ARouterPath.Video.KEY_VIDEO_ID)
    public int mVideoId;
    private IntroduceFragment mIntroduceFragment;
    private VideoCommentFragment mCommentFragment;
    private MediaPlayerManager mPlayerManager;

    /**
     * 适用于以下启动模式：singleTop SingleTask SingleInstance
     * 如果启动一个已经存在的Activity实例，那么这个方法会被触发
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //接收到新的视频id
        mVideoId = intent.getIntExtra(ARouterPath.Video.KEY_VIDEO_ID, 0);
        mViewModel.reqeustDetail(mVideoId);
        //滚动到最顶上
        mDataBinding.nsl.scrollTo(0, 0);
    }

    @Override
    protected VideoDeatilViewModel getViewModel() {
        return new ViewModelProvider(this).get(VideoDeatilViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());

        initViewPager();
        initTab();
        initPlayer();

        mViewModel.getAction().observe(this, action -> {

            if (action == VideoDeatilViewModel.Action.share) {
                ResVideoDetail.ArchivesInfoBean data = mViewModel.getVideoInfo().getValue();

                UMWeb web = new UMWeb(data.getFullurl());
                web.setTitle(data.getTitle());//标题
                web.setThumb(new UMImage(this, data.getImage()));//缩略图
                web.setDescription(data.getDescription());//描述


                new ShareAction(this)
                        .withMedia(web)
                        .setDisplayList( SHARE_MEDIA.WEIXIN)//目前只认证了微信平台，所以只添加微信
//                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA shareMedia) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA shareMedia) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA shareMedia, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA shareMedia) {

                            }
                        }).open();
            }
        });
    }

    private void initPlayer() {
        mPlayerManager = MediaPlayerManager.getInstance(this);
        mPlayerManager.bindPlayerView(mDataBinding.playView);
    }

    private void initTab() {
        mDataBinding.tabLayout.setViewPager(mDataBinding.viewPager);

        ArrayList titles = new ArrayList();
        titles.add("简介");
        titles.add("评论");
        mDataBinding.tabLayout.setAdapter(new TabFlowAdapter(titles));

    }

    private void initViewPager() {

        mIntroduceFragment = (IntroduceFragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_INTRODUCE)
                .navigation();
        mCommentFragment = (VideoCommentFragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_COMMENT)
                .navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mIntroduceFragment);
        fragments.add(mCommentFragment);

        mDataBinding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments == null ? 0 : fragments.size();
            }
        });


        mDataBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //在切换fragment的时候动态计算对应fragment的高度
                if (position == 0) {
                    mIntroduceFragment.updateFragmentHeight();
                } else if (position == 1) {
                    mCommentFragment.updateFragmentHeight();
                }
            }
        });
        //指定viewpager预加载的页面数量
//        mDataBinding.viewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void initData() {
        Log.i(TAG, "initData: mVideoId = " + mVideoId);
        mViewModel.reqeustDetail(mVideoId);
        //获取到播放信息
        mViewModel.getVideoInfo().observe(this, archivesInfoBean -> {
            //视频播放地址
            String url = archivesInfoBean.getVideo_file();
            mPlayerManager.play(url);
        });
    }


    /**
     * 通过eventBus订阅 登录状态变化的消息
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)//表示接收粘性事件
    public void onMessageEvent(MessageEvent.LoginStatusEvent event) {
        boolean login = event.isLogin();
        Log.i(TAG, "onMessageEvent: isLogin = " + login);
        //登录状态如果发生变化  重新请求数据   目的是为了刷新点赞、收藏的状态
        mViewModel.reqeustDetail(mVideoId);
    }

    @Override
    public void onStart() {
        super.onStart();
        //确保页面活跃的时候再接收事件
        EventBus.getDefault().register(this);
        //Activity进入前台  恢复播放
        mPlayerManager.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        //页面不活跃的时候取消事件接收
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//注销
        }

        //Activity不可见
        mPlayerManager.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.release();
    }
}
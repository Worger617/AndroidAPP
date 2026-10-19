package com.ls.feature_find.ui.categorydetail;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.data_video.bean.ResFindCategory;
import com.ls.feature_find.BR;
import com.ls.feature_find.R;
import com.ls.feature_find.databinding.ActivityCategoryDetailBinding;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.ArrayList;

@Route(path = ARouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
public class CategoryDetailActivity extends BaseActivity<ActivityCategoryDetailBinding, CategoryDetailViewModel> {

    private static final String TAG = "CategoryDetailActivity";

    @Autowired(name = ARouterPath.Find.KEY_CATEGORY_DATA)
    ResFindCategory mDetail;

    @Override
    protected CategoryDetailViewModel getViewModel() {
        return new ViewModelProvider(this).get(CategoryDetailViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_category_detail;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2Views(mDataBinding.getRoot(), mDataBinding.ivBack, mDataBinding.ivShare);
        initViewPager();
    }

    @Override
    protected void initData() {
        Log.i(TAG, "initData: mDetail=" + mDetail.getId() + "  name= " + mDetail.getName());
        //虽然mDetail已经有数据了，但是分类详情接口包含了浏览人数、参与人数等新字段
        mViewModel.requestDatas(mDetail.getId());
    }

    private void initViewPager() {
        //热门推荐
        Fragment recommendFragment = (Fragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_CATEGORY_LIST)
                .withInt(ARouterPath.Video.KEY_CATEGORY_TYPE, ARouterPath.Video.CATEGORY_VIDEO_RECOMMEND)
                .withInt(ARouterPath.Video.KEY_CATEGORY_ID, mDetail.getId())
                .navigation();
        //最新发布
        Fragment publishFragment = (Fragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_CATEGORY_LIST)
                .withInt(ARouterPath.Video.KEY_CATEGORY_TYPE, ARouterPath.Video.CATEGORY_VIDEO_NEWPUBLISH)
                .withInt(ARouterPath.Video.KEY_CATEGORY_ID, mDetail.getId())
                .navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(recommendFragment);
        fragments.add(publishFragment);

        mDataBinding.viewPager2.setAdapter(new FragmentStateAdapter(this) {
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
        ;
        mDataBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int rbRecommendId = mDataBinding.rbRecommend.getId();
                int rbNewId = mDataBinding.rbNew.getId();
                if (checkedId == rbRecommendId) {
                    mDataBinding.viewPager2.setCurrentItem(0);
                    mDataBinding.barrier1.setVisibility(View.VISIBLE);
                    mDataBinding.barrier2.setVisibility(View.INVISIBLE);
                } else if (checkedId == rbNewId) {
                    mDataBinding.viewPager2.setCurrentItem(1);
                    mDataBinding.barrier1.setVisibility(View.INVISIBLE);
                    mDataBinding.barrier2.setVisibility(View.VISIBLE);
                }
            }
        });
        //不允许viewpager左右滑动
        mDataBinding.viewPager2.setUserInputEnabled(false);
    }

}
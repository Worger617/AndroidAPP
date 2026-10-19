package com.ls.feature_home;



import android.util.Log;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_home.databinding.LayoutFragmentHomeBinding;

import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.config.ARouterPath;
import com.ls.libbase.utils.StatusBarUtils;

import java.util.ArrayList;

@Route(path = ARouterPath.Home.FRAGMENT_HOME)
public class HomeFragment extends BaseFragment<LayoutFragmentHomeBinding, HomeViewModel> {

    private static final String TAG = "HomeFragment";

    private Fragment mRecommendFragment;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_fragment_home;
    }

    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    @Override
    protected void initView() {
        StatusBarUtils.addStatusBarHeight2RootView(mDataBinding.getRoot());

        Log.i(TAG, "initView");
        mRecommendFragment = (Fragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_VIDEO_LIST)
                .withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .navigation();
        Fragment dailyFragment = (Fragment) ARouter.getInstance()
                .build(ARouterPath.Video.FRAGMENT_VIDEO_LIST)
                .withInt(ARouterPath.Video.KEY_VIDEO_LIST_TYPE, ARouterPath.Video.VIDEO_LIST_FRAGMENT_DAILY)
                .navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mRecommendFragment);
        fragments.add(dailyFragment);

        mDataBinding.viewPager2.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments != null ? fragments.size() : 0;
            }
        });

        mDataBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            /**
             * ViewPager2滑动完之后
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mDataBinding.rbRecommend.setChecked(true);
                        break;
                    case 1:
                        mDataBinding.rbDaily.setChecked(true);
                        break;
                }
            }
        });

        mDataBinding.rgIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mDataBinding.rbRecommend.getId()) {
                    mDataBinding.viewPager2.setCurrentItem(0);
                } else if (checkedId == mDataBinding.rbDaily.getId()) {
                    mDataBinding.viewPager2.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    protected void initData() {

////一个测试方法，主要用来测试错误状态码
//        mDataBinding.btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String string = mDataBinding.etTest.getText().toString();
//                mRecommendFragment.setErrorCode(Integer.valueOf(string));
//            }
//        });
    }
}

package com.ls.video.ui.main;


import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ls.feature_user.ui.agreement.InitAgreementDialog;
import com.ls.libbase.base.BaseActivity;
import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.config.ARouterPath;
import com.ls.librumeng.UmengHelper;
import com.ls.video.BR;
import com.ls.video.R;
import com.ls.video.databinding.ActivityMainBinding;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;

@Route(path = ARouterPath.Main.ACTIVITY_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private static final String TAG = "MainActivity";

    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getBindingVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initView() {

        Fragment homeFragment = (Fragment) ARouter.getInstance().build("/home/homeFragment").navigation();
        Fragment plazaFragment = (Fragment) ARouter.getInstance().build("/plaza/plazaFragment").navigation();
        Fragment findFragment = (Fragment) ARouter.getInstance().build("/find/findFragment").navigation();
        Fragment userFragment = (Fragment) ARouter.getInstance().build("/user/userFragment").navigation();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(plazaFragment);
        fragments.add(findFragment);
        fragments.add(userFragment);

//        MainFragmentStateAdapter stateAdapter = new MainFragmentStateAdapter(this);
//        stateAdapter.setFragments(fragments);
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

        mDataBinding.viewPager.setUserInputEnabled(false);//不允许用户滑动切换


        mDataBinding.rbBottomNavigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_home) {
                    showFragment(0);
                } else if (checkedId == R.id.rb_plaza) {
                    showFragment(1);
                } else if (checkedId == R.id.rb_find) {
                    showFragment(2);
                } else if (checkedId == R.id.rb_mine) {
                    showFragment(3);
                }
            }
        });

        //如果未同意，就要弹窗提示
        if (!mViewModel.getPrivacyAreementStatus()) {
            InitAgreementDialog dialog = new InitAgreementDialog();
            dialog.setCallback(new InitAgreementDialog.AgreementCallback() {
                @Override
                public void onAgreement() {
                    mViewModel.savePricacyAgreementStatus();

                    UmengHelper.init(BaseApplication.getContext());
                }

                @Override
                public void onExit() {
                    finish();//关闭app
                }
            });
            dialog.show(getSupportFragmentManager(), "initagreement");
        }
    }

    private void showFragment(int position) {
        mDataBinding.viewPager.setCurrentItem(position);
    }

    @Override
    protected void initData() {
    }


}
package com.ls.libbase.base.list;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.libbase.R;
import com.ls.libbase.base.BaseFragment;
import com.ls.libbase.databinding.LayoutSrtRecyclerviewBinding;
import com.ls.network.bean.ResList;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * 这是一个专门针对于有recyclerView+smarRefreshLayout的Fragment基类
 *
 * @param <T> 这个泛型指的是当前页面的具体数据类型
 */
public abstract class BaseListFragment<T> extends BaseFragment<LayoutSrtRecyclerviewBinding, BaseListViewModel> {
    /**
     * 适配器
     */
    private RecyclerView.Adapter mAdapter;

    /**
     * @return 所有列表页使用同一个布局文件
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.layout_srt_recyclerview;
    }

    /**
     * @return 目前不需要为xml中设置变量，所以根据BaseFragment中的规则，这个方法返回0即可，或者不重写都可以
     */
    @Override
    protected int getBindingVariableId() {
        return 0;
    }

    /**
     * 将列表页面都需要做的 recyclerView smartRefreshLayout初始化放到这里统一实现
     * 但是注意，BaseListFragment子类中的initView方法会覆盖父类的同名方法
     * 如果子类方法中没有显式调用 super.initView()，父类的initView实现逻辑将不会被触发！
     */
    @Override
    protected void initView() {

        //初始化recyclerView
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        mDataBinding.recyclerView.setLayoutManager(layoutManager);
        //初始化适配器
        mAdapter = getAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);
        //为smartRefreshLayout添加  加载更多的回调
        mDataBinding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //下拉触发这个方法
//                mViewModel.requestData(mPageType, false);
//                onLoadMoreDatas(false);

                mViewModel.requestDatas(false);
            }
        });
        //为smartRefreshLayout添加  刷新的回调
        mDataBinding.smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
//                mViewModel.requestData(mPageType, true);
                mViewModel.requestDatas(true);
            }
        });
    }


    /**
     * 将列表页面都需要做的 数据观测、加载 放到这里统一实现
     * 但是注意，BaseListFragment子类中的initData方法会覆盖父类的同名方法
     * 如果子类方法中没有显式调用 super.initData()，父类的initData实现逻辑将不会被触发！
     */
    @Override
    protected void initData() {

        mViewModel.requestDatas(true);

        //观测到数据发生变化

        mViewModel.getDatas().observe(getViewLifecycleOwner(), new Observer<ResList<T>>() {
            @Override
            public void onChanged(ResList<T> datas) {
                //停止刷新、加载的状态
                SmartRefreshLayout smartRefreshLayout = mDataBinding.smartRefreshLayout;
                if (smartRefreshLayout.isRefreshing()) {
                    smartRefreshLayout.finishRefresh();
                }
                if (smartRefreshLayout.isLoading()) {
                    smartRefreshLayout.finishLoadMore();
                }
                //将数据设置到适配器进行显示
                List<T> lists = datas.getList();
                onDatasRequestSuccess(lists);
            }
        });


        //是否允许加载更多
        mViewModel.getIsLoadMore().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadMore) {
                //是否允许继续加载
                mDataBinding.smartRefreshLayout.setEnableLoadMore(isLoadMore);
                if (!isLoadMore) {
                    Toast.makeText(getContext(), "没有更多数据了！", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * 由子类提供adapter
     *
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 由子类提供LayoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 数据获取成功后通知子类设置到适配器中
     * <p>
     * 另外还有种做法：可以再定义一个BaseAdapter类，然后当成列表页的adapter基类，在里面实现一个setDatas方法
     * 那么就不再需要通知子类设置数据，而是可以直接在当前的BaseListFragment中完成。
     * //    private BaseAdapter mAdapter;  mAdapter.setDatas(List<T> list);
     *
     * @param list 获取到的数据
     */
    protected abstract void onDatasRequestSuccess(List<T> list);
}

package com.ls.feature_plaza.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ls.feature_plaza.R;
import com.ls.feature_plaza.bean.PlazaXBannerData;
import com.ls.feature_plaza.bean.ResPlaza;
import com.ls.feature_plaza.databinding.ItemBannerBinding;
import com.ls.feature_plaza.databinding.ItemImageBinding;
import com.ls.libbase.utils.GlideUtils;
import com.stx.xhb.androidx.XBanner;

import java.util.ArrayList;
import java.util.List;

public class PlazaApater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_BANNER = 1;//banner
    private static final int ITEM_TYPE_IMAGE = 2;//常规item类型
    private List<ResPlaza.PlazaDetail> mLists;
    private ArrayList<PlazaXBannerData> mBannerDatas;//banner数据

    private PlazaItemListener mListener;
    private ResPlaza mBannerData;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (ITEM_TYPE_BANNER == viewType) {
            ItemBannerBinding bannerBinding = ItemBannerBinding.inflate(layoutInflater, parent, false);
            BannerViewHolder viewHolder = new BannerViewHolder(bannerBinding);
            return viewHolder;
        } else {
            ItemImageBinding imageBinding = ItemImageBinding.inflate(layoutInflater, parent, false);
            ImageViewHolder viewHolder = new ImageViewHolder(imageBinding);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_TYPE_BANNER) {
            //顶部的第一行 banner
            BannerViewHolder viewHolder = (BannerViewHolder) holder;
            ItemBannerBinding binding = viewHolder.bannerBinding;
            //设置占位图
            binding.xbanner.setBannerPlaceholderImg(R.mipmap.ic_launcher, ImageView.ScaleType.CENTER_CROP);
            //一屏多页
            binding.xbanner.setIsClipChildrenMode(true);
            //设置banner数据以及自定义banner每页的布局
            binding.xbanner.setBannerData(R.layout.item_banner_child, mBannerDatas);
            binding.xbanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageView imgeView = view.findViewById(R.id.image_wiew);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvLabel = view.findViewById(R.id.tv_label);

                    PlazaXBannerData data = mBannerDatas.get(position);
                    GlideUtils.loadImage(data.getXBannerUrl(), imgeView);
                    tvTitle.setText(data.getXBannerTitle());
                    tvLabel.setText(data.getDescription());

                }
            });

            binding.xbanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    binding.tvIndicator.setText(String.valueOf(position + 1));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            //ITEM_TYPE_IMAGE
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            ResPlaza.PlazaDetail detail = mLists.get(position - 1);
            viewHolder.binding.setData(detail);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_BANNER : ITEM_TYPE_IMAGE;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mBannerDatas != null && mBannerDatas.size() > 0) {
            count += 1;
        }

        if (mLists != null && mLists.size() > 0) {
            count += mLists.size();
        }
        return count;
    }

    public void setDatas(List<ResPlaza> data) {
        if (data != null && data.size() >= 2) {

            mBannerData = data.get(0);
            mBannerDatas = converXBannerDatas(mBannerData);
            ResPlaza imageData = data.get(1);
            mLists = imageData.getLists();


            //刷新
            notifyDataSetChanged();
        }
    }

    /**
     * 因为xbanner需要接受特定的数据类型，所以要把服务端返回的数据转成xbanner可以接受的数据类型
     *
     * @param data
     * @return
     */
    private ArrayList<PlazaXBannerData> converXBannerDatas(ResPlaza data) {

        List<ResPlaza.PlazaDetail> lists = data.getLists();

        if (lists != null && lists.size() > 0) {
            ArrayList<PlazaXBannerData> xBannerDatas = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                ResPlaza.PlazaDetail detail = lists.get(i);
                PlazaXBannerData bannerData = new PlazaXBannerData(detail.getImage(),
                        detail.getName(), detail.getDescription());
                xBannerDatas.add(bannerData);
            }

            return xBannerDatas;
        }

        return null;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ItemImageBinding binding;

        public ImageViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getLayoutPosition();
                    ResPlaza.PlazaDetail plazaDetail = mLists.get(position - 1);
                    mListener.onImageClick(plazaDetail);
                }
            });
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private final ItemBannerBinding bannerBinding;

        public BannerViewHolder(ItemBannerBinding binding) {
            super(binding.getRoot());
            bannerBinding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        //bug修复版：获取banner当前的位置
                        int bannerCurrentItem = binding.xbanner.getBannerCurrentItem();
                        itemClick(bannerCurrentItem);

                        //视频里使用的是getLayoutPosition，会导致不管banner滑动到第几页
                        //获取到的始终是0，使用id=0表示跳转到运动分类详情页
//                        int position = getLayoutPosition();
//                        itemClick(position);
                    }
                }
            });
            binding.xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, Object model, View view, int position) {
                    if (mListener != null) {
                        itemClick(position);
                    }
                }

            });
        }

        private void itemClick(int position) {
            List<ResPlaza.PlazaDetail> lists = mBannerData.getLists();
            ResPlaza.PlazaDetail detail = lists.get(position);
            mListener.onBannerClick(detail);
        }
    }

    public interface PlazaItemListener {
        /**
         * 头部广告点击
         *
         * @param detail
         */
        void onBannerClick(ResPlaza.PlazaDetail detail);

        /**
         * 底部图片点击
         *
         * @param detail
         */
        void onImageClick(ResPlaza.PlazaDetail detail);
    }


    public void setListener(PlazaItemListener listenner) {
        this.mListener = listenner;
    }
}

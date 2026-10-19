package com.ls.feature_find.api;

import com.ls.feature_find.bean.ResCategoryDetail;
import com.ls.feature_find.bean.ResFind;
import com.ls.feature_find.bean.ResThemeData;
import com.ls.feature_find.bean.ResTopic;
import com.ls.network.bean.ResBase;
import com.ls.network.bean.ResList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 这里存放find模块的api
 */
public interface FindApiService {

    /**
     * 发现首页数据
     *
     * @return 服务端返回的数据类型
     */
    @GET("addons/cms/api.eye/find")
    Call<ResBase<ResFind>> getFindData();


    /**
     * 获取分类详情数据
     *
     * @return 服务端返回的数据类型
     */
    @GET("addons/cms/api.eye/category_detail")
    Call<ResBase<ResCategoryDetail>> getCategoryDetail(@Query("channel_id") int id);


    /**
     * 获取主题播单
     *
     * 不需要传任何参数
     *
     * @return 服务端返回的数据类型
     */
    @GET("addons/cms/api.eye/anchor")
    Call<ResBase<List<ResThemeData>>> getAnchor();
    /**
     * 获取主题播单
     *
     * 不需要传任何参数
     *
     * @return 服务端返回的数据类型
     */
    @GET("addons/cms/api.eye/topic")
    Call<ResBase<List<ResTopic>>> getTopic();


}

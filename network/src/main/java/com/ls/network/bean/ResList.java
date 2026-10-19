package com.ls.network.bean;

import java.util.List;
/**
 * 当前项目 网络请求中返回的列表数据类型基类
 *
 * @param <T> T指的是具体返回的数据类型
 */
public class ResList<T> {
    private int count;//当前列表的总个数
    private List<T> list;//本次分页请求获取到的个数

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

package com.ls.data_video.bean;

import java.util.List;

/**
 * 收藏列表
 */
public class ResCollection {


    /**
     * collectionList : {"total":2,"per_page":10,"current_page":1,"last_page":1,"data":[{"id":32,"type":"archives","aid":132,"user_id":11,"title":"日本分屏温情广告：60 秒的亲子温暖","image":"http://ali-img.kaiyanapp.com/cover/20230412/8e4bdd503ec5655de179cc8e5c7d7e22.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","url":"https://titok.fzqq.fun/cms/guanggao/132.html","createtime":1745133329,"updatetime":1745133329,"tag":"广告","create_date":"2025-04-20 15:15:29"},{"id":31,"type":"archives","aid":131,"user_id":11,"title":"泰国金融广告，大胆预测 10 年后的人类","image":"http://ali-img.kaiyanapp.com/d49b4b599ca45d0c1f47b2cb5b4f5d1b.jpeg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","url":"https://titok.fzqq.fun/cms/guanggao/131.html","createtime":1745133325,"updatetime":1745133325,"tag":"广告","create_date":"2025-04-20 15:15:25"}]}
     * model : null
     */

    private CollectionListBean collectionList;

    public CollectionListBean getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(CollectionListBean collectionList) {
        this.collectionList = collectionList;
    }

    public static class CollectionListBean {
        /**
         * total : 2
         * per_page : 10
         * current_page : 1
         * last_page : 1
         * data : [{"id":32,"type":"archives","aid":132,"user_id":11,"title":"日本分屏温情广告：60 秒的亲子温暖","image":"http://ali-img.kaiyanapp.com/cover/20230412/8e4bdd503ec5655de179cc8e5c7d7e22.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","url":"https://titok.fzqq.fun/cms/guanggao/132.html","createtime":1745133329,"updatetime":1745133329,"tag":"广告","create_date":"2025-04-20 15:15:29"},{"id":31,"type":"archives","aid":131,"user_id":11,"title":"泰国金融广告，大胆预测 10 年后的人类","image":"http://ali-img.kaiyanapp.com/d49b4b599ca45d0c1f47b2cb5b4f5d1b.jpeg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp","url":"https://titok.fzqq.fun/cms/guanggao/131.html","createtime":1745133325,"updatetime":1745133325,"tag":"广告","create_date":"2025-04-20 15:15:25"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<ResVideoDetail.ArchivesInfoBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public List<ResVideoDetail.ArchivesInfoBean> getData() {
            return data;
        }

        public void setData(List<ResVideoDetail.ArchivesInfoBean> data) {
            this.data = data;
        }

//        public static class DataBean {
//            /**
//             * id : 32
//             * type : archives
//             * aid : 132
//             * user_id : 11
//             * title : 日本分屏温情广告：60 秒的亲子温暖
//             * image : http://ali-img.kaiyanapp.com/cover/20230412/8e4bdd503ec5655de179cc8e5c7d7e22.jpg?imageMogr2/auto-orient/thumbnail/640x/interlace/1/quality/80/format/webp
//             * url : https://titok.fzqq.fun/cms/guanggao/132.html
//             * createtime : 1745133329
//             * updatetime : 1745133329
//             * tag : 广告
//             * create_date : 2025-04-20 15:15:29
//             */
//
//            private int id;
//            private String type;
//            private int aid;
//            private int user_id;
//            private String title;
//            private String image;
//            private String url;
//            private int createtime;
//            private int updatetime;
//            private String tag;
//            private String create_date;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getType() {
//                return type;
//            }
//
//            public void setType(String type) {
//                this.type = type;
//            }
//
//            public int getAid() {
//                return aid;
//            }
//
//            public void setAid(int aid) {
//                this.aid = aid;
//            }
//
//            public int getUser_id() {
//                return user_id;
//            }
//
//            public void setUser_id(int user_id) {
//                this.user_id = user_id;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getImage() {
//                return image;
//            }
//
//            public void setImage(String image) {
//                this.image = image;
//            }
//
//            public String getUrl() {
//                return url;
//            }
//
//            public void setUrl(String url) {
//                this.url = url;
//            }
//
//            public int getCreatetime() {
//                return createtime;
//            }
//
//            public void setCreatetime(int createtime) {
//                this.createtime = createtime;
//            }
//
//            public int getUpdatetime() {
//                return updatetime;
//            }
//
//            public void setUpdatetime(int updatetime) {
//                this.updatetime = updatetime;
//            }
//
//            public String getTag() {
//                return tag;
//            }
//
//            public void setTag(String tag) {
//                this.tag = tag;
//            }
//
//            public String getCreate_date() {
//                return create_date;
//            }
//
//            public void setCreate_date(String create_date) {
//                this.create_date = create_date;
//            }
//        }
    }
}

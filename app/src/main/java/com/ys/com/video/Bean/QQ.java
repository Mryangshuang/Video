package com.ys.com.video.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/1 0001.
 */

public class QQ {

    private List<ContentBean> content;

    public static QQ objectFromData(String str) {

        return new com.google.gson.Gson().fromJson(str, QQ.class);
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * filterid : 0
         * subid : 0
         * predownload : 1
         * resurl :
         * md5 :
         * iconurl : https://sqimg.qq.com/QQiPhoneAV/AVFunChat/AVFilter/Android/NOFILTER.png
         * iconmd5 : 60d9f448cf770e481687f6ed61f0355f
         * name : EMPTY
         */

        private String filterid;
        private String subid;
        private String predownload;
        private String resurl;
        private String md5;
        private String iconurl;
        private String iconmd5;
        private String name;

        public static ContentBean objectFromData(String str) {

            return new com.google.gson.Gson().fromJson(str, ContentBean.class);
        }

        public String getFilterid() {
            return filterid;
        }

        public void setFilterid(String filterid) {
            this.filterid = filterid;
        }

        public String getSubid() {
            return subid;
        }

        public void setSubid(String subid) {
            this.subid = subid;
        }

        public String getPredownload() {
            return predownload;
        }

        public void setPredownload(String predownload) {
            this.predownload = predownload;
        }

        public String getResurl() {
            return resurl;
        }

        public void setResurl(String resurl) {
            this.resurl = resurl;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public String getIconmd5() {
            return iconmd5;
        }

        public void setIconmd5(String iconmd5) {
            this.iconmd5 = iconmd5;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

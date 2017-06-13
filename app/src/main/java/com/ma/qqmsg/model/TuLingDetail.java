package com.ma.qqmsg.model;

/**
 * Created by mags on 2017/6/12.
 */

public class TuLingDetail {
    private String name,info,url,
    article,source,icon,detailurl;

    public String getName() {
        return name;
    }

    public TuLingDetail setName(String name) {
        this.name = name;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public TuLingDetail setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TuLingDetail setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getArticle() {
        return article;
    }

    public TuLingDetail setArticle(String article) {
        this.article = article;
        return this;
    }

    public String getSource() {
        return source;
    }

    public TuLingDetail setSource(String source) {
        this.source = source;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public TuLingDetail setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public TuLingDetail setDetailurl(String detailurl) {
        this.detailurl = detailurl;
        return this;
    }
}

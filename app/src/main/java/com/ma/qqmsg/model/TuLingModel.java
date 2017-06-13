package com.ma.qqmsg.model;

import java.util.List;

/**
 * Created by mags on 2017/6/12.
 */

public class TuLingModel {
    private int code;
    private String text;
    private String url;
    private List<TuLingDetail> list;

    public String getUrl() {
        return url;
    }

    public TuLingModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getCode() {
        return code;
    }

    public TuLingModel setCode(int code) {
        this.code = code;
        return this;
    }

    public String getText() {
        return text;
    }

    public TuLingModel setText(String text) {
        this.text = text;
        return this;
    }

    public List<TuLingDetail> getList() {
        return list;
    }

    public TuLingModel setList(List<TuLingDetail> list) {
        this.list = list;
        return this;
    }
}

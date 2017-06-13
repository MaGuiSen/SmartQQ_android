package com.ma.qqmsg.model;

/**
 * Created by mags on 2017/6/10.
 */

public class Replay {
    int id;
    String to_id;
    String content;
    String is_enable;
    String is_enable_ai;

    public int getId() {
        return id;
    }

    public Replay setId(int id) {
        this.id = id;
        return this;
    }

    public String getTo_id() {
        return to_id;
    }

    public Replay setTo_id(String to_id) {
        this.to_id = to_id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Replay setContent(String content) {
        this.content = content;
        return this;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public Replay setIs_enable(String is_enable) {
        this.is_enable = is_enable;
        return this;
    }

    public String getIs_enable_ai() {
        return is_enable_ai;
    }

    public Replay setIs_enable_ai(String is_enable_ai) {
        this.is_enable_ai = is_enable_ai;
        return this;
    }
}

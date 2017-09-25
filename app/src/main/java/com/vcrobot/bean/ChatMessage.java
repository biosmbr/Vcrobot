package com.vcrobot.bean;

import android.content.Context;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class ChatMessage {
    //文本
    private String content;
    private int flag;
    private String time;

    //语音
    private float seconds;
    private String folder;
    private String srText;

    //网络情况
    private int netErr;

    //消息类型
    private int type;

    //回复消息rid
    private String rid;

    public ChatMessage(){
    }

    public String getContext() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getSeconds() {
        return seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getSrText() {
        return srText;
    }

    public void setSrText(String srText) {
        this.srText = srText;
    }

    public int getNetErr() {
        return netErr;
    }

    public void setNetErr(int netErr) {
        this.netErr = netErr;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}

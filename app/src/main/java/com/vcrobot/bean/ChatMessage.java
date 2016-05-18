package com.vcrobot.bean;

import android.content.Context;

/**
 * Created by Dolphix.J Qing on 2016/5/8.
 */
public class ChatMessage {
    private String content;
    private int flag;
    private String time;

    private float seconds;
    private String folder;
    private String srText;

    private int type;

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
}

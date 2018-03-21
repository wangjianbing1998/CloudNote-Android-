package com.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class UpdateItem implements Serializable {

    private int history_id;
    private int notes_id;
    private String update_time;
    private int update_type;
    private String before_title;
    private String after_title;
    private String before_content;
    private String after_content;


    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int pHistory_id) {
        history_id = pHistory_id;
    }

    public int getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(int pNotes_id) {
        notes_id = pNotes_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String pUpdate_time) {
        update_time = pUpdate_time;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int pUpdate_type) {
        update_type = pUpdate_type;
    }

    public String getBefore_title() {
        return before_title;
    }

    public void setBefore_title(String pBefore_title) {
        before_title = pBefore_title;
    }

    public String getAfter_title() {
        return after_title;
    }

    public void setAfter_title(String pAfter_title) {
        after_title = pAfter_title;
    }

    public String getBefore_content() {
        return before_content;
    }

    public void setBefore_content(String pBefore_content) {
        before_content = pBefore_content;
    }

    public String getAfter_content() {
        return after_content;
    }

    public void setAfter_content(String pAfter_content) {
        after_content = pAfter_content;
    }


    @Override
    public String toString() {
        return "UpdateItem{" +
                "history_id=" + history_id +
                ", notes_id=" + notes_id +
                ", update_time='" + update_time + '\'' +
                ", update_type=" + update_type +
                ", before_title='" + before_title + '\'' +
                ", after_title='" + after_title + '\'' +
                ", before_content='" + before_content + '\'' +
                ", after_content='" + after_content + '\'' +
                '}';
    }


    public UpdateItem(int pHistory_id, int pNotes_id, String pUpdate_time, int pUpdate_type, String pBefore_title, String pAfter_title, String pBefore_content, String pAfter_content) {
        history_id = pHistory_id;
        notes_id = pNotes_id;
        update_time = pUpdate_time;
        update_type = pUpdate_type;
        before_title = pBefore_title;
        after_title = pAfter_title;
        before_content = pBefore_content;
        after_content = pAfter_content;
    }


    public UpdateItem() {
    }
}

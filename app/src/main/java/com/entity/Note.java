package com.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class Note implements  Serializable {

    private int id;
    private String title;
    private String content;
    private String creating_date;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        id = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public String getCreating_date() {
        return creating_date;
    }

    public void setCreating_date(String pCreating_date) {
        creating_date = pCreating_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int pUser_id) {
        user_id = pUser_id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creating_date='" + creating_date + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public Note(int pId, String pTitle, String pContent, String pCreating_date, int pUser_id) {
        id = pId;
        title = pTitle;
        content = pContent;
        creating_date = pCreating_date;
        user_id = pUser_id;
    }
    public Note(){

    }


}

package com.hm.viewdemo.bean;

/**
 * Created by dumingwei on 2020/4/1
 * <p>
 * Desc:
 */
public class MyBean {

    private String title;
    private String detail;

    public MyBean(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

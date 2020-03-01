package com.saahil.blogshare;

public class Posts {
    private String title, body;
    private String publish;
    private int id;

    public Posts(int id, String title, String publish, String body) {
        this.id=id;
        this.title = title;
        this.publish = publish;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished() {
        return publish;
    }

    public void setPublished(String publish) {
        this.publish = publish;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

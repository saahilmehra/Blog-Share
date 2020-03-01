package com.saahil.blogshare;

public class Posts {
    private String title, body;
    private String publish;

    public Posts(String title, String publish, String body) {
        this.title = title;
        this.publish = publish;
        this.body = body;
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

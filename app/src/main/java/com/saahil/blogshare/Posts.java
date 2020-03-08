package com.saahil.blogshare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Posts {
    private String title, body;
    private String publish;
    private int id;
    private String slug;
    private String status;

    public Posts(int id, String title, String publish, String body, String slug, String status) {
        this.id=id;
        this.title = title;
        this.publish = publish;
        this.body = body;
        this.slug=slug;
        this.status=status;
    }

    public Posts(String title, String body, String slug, String status) {
        this.title = title;
        this.body = body;
        this.slug = slug;
        this.status = status;
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
        String date;
        Date temp_date=new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            temp_date= formatter1.parse(publish.substring(0,19));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        date=new SimpleDateFormat("dd MMM, yyyy").format(temp_date);
        return date;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

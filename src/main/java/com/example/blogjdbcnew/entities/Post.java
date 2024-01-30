package com.example.blogjdbcnew.entities;

import org.springframework.stereotype.Component;

@Component
public class Post {
    private Integer id;
    private String title;
    private String content;
    private int userId;

//    private Category category;
//
//    private User user;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

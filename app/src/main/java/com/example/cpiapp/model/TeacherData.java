package com.example.cpiapp.model;

public class TeacherData {
  private   String name, email, post, key, image;

    public TeacherData() {
    }

    public TeacherData(String name, String email, String post, String key, String image) {
        this.name = name;
        this.email = email;
        this.post = post;
        this.key = key;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

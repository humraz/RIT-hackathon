package com.example.humraz.rithack;


public class forumdata {
    private String post;
    private String user;

    long stackId;
    public forumdata() {
      /*Blank default constructor essential for Firebase*/
    }
    public forumdata(String a)
    {

    }
    //Getters and setters
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {

        return post;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
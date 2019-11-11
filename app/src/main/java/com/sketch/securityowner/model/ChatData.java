package com.sketch.securityowner.model;

import java.io.Serializable;

public class ChatData implements Serializable {

    private String help_id;
    private String chat_id;
    private String type;
    private String content;
    private String image;
    private String date;
    private String time;
    private String status;
    private String sender_id;
    private String sender_name;
    private boolean is_me;
    private String image_from;

    public String getHelp_id() {
        return help_id;
    }

    public void setHelp_id(String help_id) {
        this.help_id = help_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_me() {
        return is_me;
    }

    public void setIs_me(boolean is_me) {
        this.is_me = is_me;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getImage_from() {
        return image_from;
    }

    public void setImage_from(String image_from) {
        this.image_from = image_from;
    }


    /*  {
            "help_id": "20",
            "chat_id": "33",
            "type": "user",
            "content": "hello ",
            "image": "",
            "date": "2019-10-16",
            "time": "14:29:24",
            "status": "Pending"
        },*/
}

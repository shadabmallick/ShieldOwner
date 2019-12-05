package com.shield.resident.model;

import java.io.Serializable;

public class ChatListData implements Serializable {

    private String sender_id;
    private String receiver_id;
    private String user_name;
    private String user_image;
    private String user_type;
    private String message;
    private String message_type;
    private String datetime;
    private String chat_type;


    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }



    /*{
        "sender_id": "87",
            "receiver_id": "21",
            "message": "got it",
            "message_type": "1",
            "datetime": "2019-05-21 06:26:37",
            "chat_type": "User",
            "dist": "108",
            "user_name": "Nahtral",
            "user_type": "Coach/Teachers",
            "user_image": "https://www.supernahtralsports.com/uploads/user/1551682037_image_image.png"
    },*/
}

package com.kut.industryandroid.Models;

public class Room {
    private String  roomname;
    private String lastMessage;

    public Room() {
    }

    public Room(String roomname, String lastMessage) {
        this.roomname = roomname;
        this.lastMessage = lastMessage;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}

package com.example.lostandfound.database;

public class LostFoundItem {

    public int id;
    public String postType;     // "Lost" or "Found"
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;
    public String imagePath;    // file path of the uploaded image
    public String timestamp;

    public LostFoundItem(int id, String postType, String name,
                         String phone, String description, String date,
                         String location, String imagePath,
                         String timestamp) {
        this.id          = id;
        this.postType    = postType;
        this.name        = name;
        this.phone       = phone;
        this.description = description;
        this.date        = date;
        this.location    = location;
        this.imagePath   = imagePath;
        this.timestamp   = timestamp;
    }
}
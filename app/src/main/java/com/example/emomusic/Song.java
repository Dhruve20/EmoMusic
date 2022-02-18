package com.example.emomusic;

public class Song {
    private String Name, Url;

    public Song() {
    }

    public Song(String Name, String Url) {
        this.Name = Name;
        this.Url = Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }
}

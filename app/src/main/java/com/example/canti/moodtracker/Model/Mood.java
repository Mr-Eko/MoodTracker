package com.example.canti.moodtracker.Model;


public class Mood {

    private int smileyResource;
    public int backgroundColor;
    public int position;
    public int sound;
    public String comment;


    public Mood(int smileyResource, int backgroundColor, int position, int sound) {
        this.smileyResource = smileyResource;
        this.backgroundColor = backgroundColor;
        this.position = position;
        this.sound = sound;
    }

    public Mood(String comment,int backgroundColor, int position) {

        this.comment = comment;
        this.backgroundColor = backgroundColor;
        this.position = position;
    }
    public int getSmileyResource() {
        return smileyResource;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getSound() {
        return sound;
    }
}

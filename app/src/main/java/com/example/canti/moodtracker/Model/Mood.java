package com.example.canti.moodtracker.Model;


public class Mood {

    private int smileyResource;
    private int backgroundColor;
    public int position;
    private int sound;
    public String comment;


    public Mood(int smileyResource, int backgroundColor, int position, int sound) {
        this.smileyResource = smileyResource;
        this.backgroundColor = backgroundColor;
        this.position = position;
        this.sound = sound;
    }

    protected Mood (String comment, int position) {

        this.comment = comment;
        this.position = position;
    }

    public Mood (int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getSmileyResource() {
        return smileyResource;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getPosition() {
        return position;
    }

    public int getSound() {
        return sound;
    }
}

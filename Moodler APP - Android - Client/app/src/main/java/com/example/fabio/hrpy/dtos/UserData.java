package com.example.fabio.hrpy.dtos;

import java.util.ArrayList;

public class UserData {

    private String name;

    private String music;

    private String artist;

    private boolean evaluation;

    private String hrv;

    private String uri;

    public UserData(String name, String music, String artist, boolean evaluation, ArrayList<String> hrv, String uri){
        this.name = name;
        this.music = music;
        this.artist = artist;
        this.evaluation = evaluation;
        this.hrv = hrv.toString();
        this.uri = uri;
    }


    public String getName() {
        return name;
    }

    public String getMusic() {
        return music;
    }

    public boolean getEvaluation(){
        return evaluation;
    }

    public String getHrv() {
        return hrv;
    }

    public String getArtist() {
        return artist;
    }

    public String getUri() {
        return uri;
    }
}

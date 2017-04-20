package com.example.wesmt.testp2p;

/**
 * Created by wesmt on 4/17/2017.
 */

public class Song {

    private long songID;
    private String songTitle;

    public Song(long id, String title)
    {
        songID = id;
        songTitle = title;
    }

    public long getSongID()
    {
        return songID;
    }

    public String getTitle()
    {
        return songTitle;
    }



}

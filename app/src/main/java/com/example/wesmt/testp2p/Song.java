package com.example.wesmt.testp2p;

/**
 * Created by wesmt on 4/17/2017.
 */

public class Song {

    private long songID;
    private String songTitle;
    private String songPath;

    public Song(long id, String title,String path)
    {
        songID = id;
        songTitle = title;
        songPath = path;
    }

    public String getSongPath()
    {
        return songPath;
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

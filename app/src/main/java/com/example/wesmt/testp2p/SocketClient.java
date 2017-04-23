package com.example.wesmt.testp2p;

/**
 * Created by wesmt on 4/23/2017.
 */
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;



public class SocketClient {

    private Socket client;
    public byte [] song;

    public SocketClient(String ipaddress) throws IOException
    {
        this.client = new Socket(ipaddress,8888);


    }


    public void listen() throws IOException {

        DataInputStream dIn = new DataInputStream(client.getInputStream());
        int length = dIn.readInt();
        if(length > 0)
        {
            song = new byte[length];
            dIn.readFully(song,0,song.length);
        }
        dIn.close();
        client.close();

    }



}

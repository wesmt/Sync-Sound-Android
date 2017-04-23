package com.example.wesmt.testp2p;

/**
 * Created by wesmt on 4/23/2017.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class SocketServer {

    ServerSocket server;
    private int socketNumber = 8888;
    
    private byte[] data;
    private String t;


    public SocketServer(byte[] songData, String type)
    {
        this.data = songData;
        this.t = type;
    }


    public void listen() throws IOException {
        Socket socket = this.server.accept();

    }
}


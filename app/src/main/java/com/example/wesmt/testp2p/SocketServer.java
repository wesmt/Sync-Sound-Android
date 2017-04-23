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


public class SocketServer {

    ServerSocket server;
    private int socketNumber = 8888;

    private byte[] data;
    private String t;


    public SocketServer(byte[] songData, String type) throws IOException
    {
        this.data = songData;
        this.t = type;

        this.server = new ServerSocket(socketNumber);



    }



    public void listen() throws IOException {
        Socket socket = this.server.accept();
        //DataInputStream input;
        DataOutputStream output;
        try{
            output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(data.length);
            output.write(data);
            output.close();

            //input = new DataI
        }
        catch(IOException e)
        {

        }
        socket.close();
        server.close();
    }
}


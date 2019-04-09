package com.ca4.Client;

import com.ca4.Core.MovieServiceDetails;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MovieClient
{
    public static void main(String[] args)
    {
        Socket client;
        OutputStream outputToSocket;
        InputStream inputFromSocket;
        PrintWriter streamWriter;
        BufferedReader streamReader;

        try{
            InetAddress serverIP = InetAddress.getByName(MovieServiceDetails.SERVER_IP);

            client = new Socket(serverIP, MovieServiceDetails.SERVER_PORT);
        }catch (UnknownHostException ue){
            ue.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}

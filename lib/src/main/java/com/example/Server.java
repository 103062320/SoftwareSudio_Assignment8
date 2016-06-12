package com.example;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import javax.swing.JTextArea;

/**
 * Created by Gary on 16/5/28.
 */
public class Server extends JFrame implements Runnable {
    private Thread thread;
    private ServerSocket servSock;
    private JTextArea textArea;

    public Server(){

        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 600);
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setBounds(0, 0, 400, 600);
        this.add(this.textArea);
        this.setVisible(true);


        try {
            // Detect server ip
            InetAddress IP = InetAddress.getLocalHost();
            this.textArea.append("IP of my system is := "+IP.getHostAddress() + "\n");
            this.textArea.append("Waitting to connect......" + "\n");
           // System.out.println("IP of my system is := "+IP.getHostAddress());
           // System.out.println("Waitting to connect......");

            // Create server socket
            servSock = new ServerSocket(2000);

            // Create socket thread
            thread = new Thread(this);
            thread.start();
        } catch (java.io.IOException e) {
            this.textArea.append("Socket啟動有問題 !" + "\n");
            this.textArea.append("IOException :" + e.toString() + "\n");
            //System.out.println("Socket啟動有問題 !");
            //System.out.println("IOException :" + e.toString());
        } finally{

        }
    }

    class ConnectionThread extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        public ConnectionThread(Socket socket) {
            this.socket = socket;
            try {
                this.reader = new BufferedReader(new
                        InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(new
                        OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        public void run() {
            while(true) {
                try {
                    String line = this.reader.readLine();
                    textArea.append("[Server Said]" + line + "\n");
                    //System.out.println("[Server Said]" + line);
                    // System.out.println(line); // line is message from server
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void run(){
        // Running for waitting multiple client
        while(true){
            //System.out.println("aa");
            try{
                // After client connected, create client socket connect with client
                Socket clntSock = servSock.accept();
                InputStream in = clntSock.getInputStream();

                this.textArea.append("Connected!!" + "\n");
               // System.out.println("Connected!!");

                ConnectionThread connThread = new
                        ConnectionThread(clntSock);
                connThread.start();
                // Transfer data
               /* byte[] b = new byte[1024];
                int length;

                length = in.read(b);
                String s = new String(b);
                System.out.println("[Server Said]" + s);*/

            }
            catch(Exception e){
                this.textArea.append("Error: "+e.getMessage() + "\n");
               // System.out.println("Error: "+e.getMessage());
            }
        }
    }
}

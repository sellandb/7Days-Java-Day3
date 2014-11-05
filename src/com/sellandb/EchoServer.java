package com.sellandb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bselland on 11/4/14.
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        class ConnectionHandler implements Runnable {
            InputStream in; OutputStream out;

            ConnectionHandler(Socket socket) throws IOException {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            }

            @Override
            public void run() {
                try {
                    int n;
                    byte[] buffer = new byte[4096];

                    //Setup HTTP Headers
                    String headers = "";
                    headers += "HTTP/1.1 200 OK\r\n";
                    headers += "Content-Type: text/html\r\n";

                    //Setup the HTML template
                    String bodyStart = "<html><body><h2>Mirror:</h2><p>";
                    String bodyEnd = "</p></body></html>";


                    if((n = in.read(buffer)) > 0) {
                        //Figure out the content length for the HTTP Content-Length Header
                        //Use n as the buffer length instead of buffer.length - buffer.length reports
                        //the size of the buffer, not the length of the content.
                        int length = bodyStart.getBytes().length + n + bodyEnd.getBytes().length;

                        //Add the content length header and the extra line before the body content
                        headers += "Content-Length: " + length + "\r\n";
                        headers += "\r\n";

                        out.write(headers.getBytes());      //Write out the header
                        out.write(bodyStart.getBytes());    //Write out the start of our body
                        out.write(buffer, 0, n);            //Write out the data
                        out.write(bodyEnd.getBytes());      //Write out the end of the body
                        out.flush();
                    }

                } catch (IOException e) {

                } finally {
                    try {
                        //Close our sockets
                        in.close();
                        out.close();
                    } catch (IOException e) {

                    }
                }

            }
        }

        //Setup socket
        ServerSocket server = new ServerSocket(4567);

        //Setup Threadpool to handle incoming requests
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        //loop and handle requests as they come in
        while (true) {
            Socket socket = server.accept();
            executor.execute(new ConnectionHandler(socket));
        }
    }
}

package com.lawyus.study.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DemoServer {

    static void main() {
        DemoServer server = new DemoServer(7000);
        server.start();
    }

    public DemoServer(int port) {
        this.port = port;
    }
    private int port;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            port = serverSocket.getLocalPort();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new RespHandler(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class RespHandler implements Runnable {
    private Socket socket;

    public RespHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader.lines().forEach(s -> {
                System.out.println("Received from client: " + s);

                PrintWriter writer;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                writer.write("server");
                writer.println();
                writer.flush();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

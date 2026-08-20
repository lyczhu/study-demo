package com.lawyus.study.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("服务器启动，等待客户端连接...");

        clientSocket = serverSocket.accept();
        System.out.println("客户端已连接: " + clientSocket.getInetAddress());

        // 初始化输入输出流
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        // 启动读取线程
        Thread readThread = new Thread(this::readMessages);
        readThread.start();

        // 主线程用于发送消息
        sendMessages();

        // 清理资源
        readThread.interrupt();
        stop();
    }

    private void readMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("客户端: " + message);

                // 如果客户端发送"bye"，则结束通信
                if ("bye".equalsIgnoreCase(message)) {
                    System.out.println("客户端请求结束通信");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("连接已断开");
        }
    }

    private void sendMessages() {
        try (Scanner scanner = new Scanner(System.in);) {
            String message;
            while (true) {
                System.out.print("服务器: ");
                message = scanner.nextLine();
                out.println(message);

                if ("z".equalsIgnoreCase(message)) {
                    System.out.println("结束通信");
                    break;
                }
            }
        }
    }

    public void stop() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        try {
            server.start(7500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

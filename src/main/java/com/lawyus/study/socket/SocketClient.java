package com.lawyus.study.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void start(String host, int port) throws IOException {
        socket = new Socket(host, port);
        System.out.println("已连接到服务器: " + host + ":" + port);

        // 初始化输入输出流
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

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
                System.out.println("服务器: " + message);

                // 如果服务器发送"bye"，则结束通信
                if ("bye".equalsIgnoreCase(message)) {
                    System.out.println("服务器请求结束通信");
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
                System.out.print("客户端: ");
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
        if (socket != null) socket.close();
    }

    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        try {
            client.start("localhost", 7500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

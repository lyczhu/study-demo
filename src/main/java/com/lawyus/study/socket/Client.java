package com.lawyus.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    static void main() {
        clientConnect(7000);
    }

    private static void clientConnect(int serverPort) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
        ) {
            writer.println("request from client");
            writer.flush();
            Thread.sleep(1000);
            writer.println("request again");
            writer.flush();

            reader.lines().forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

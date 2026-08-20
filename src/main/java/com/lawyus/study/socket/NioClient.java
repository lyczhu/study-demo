package com.lawyus.study.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NioClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private static final String HOST = "localhost";
    private static final int PORT = 7650;

    public void start() throws IOException {
        // 创建Selector
        selector = Selector.open();

        // 创建SocketChannel并连接服务器
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(HOST, PORT));

        // 注册连接事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        // 启动消息发送线程
        new Thread(this::sendMessage).start();

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isConnectable()) {
                    handleConnect(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }

        System.out.println("连接服务器成功!");
        // 注册读事件，准备接收服务器消息
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            channel.close();
            System.out.println("服务器断开连接");
            System.exit(0);
        }

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        String message = new String(data);
        System.out.println("收到服务器消息: " + message);
    }

    private void sendMessage() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("请输入消息: ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    socketChannel.close();
                    System.exit(0);
                }

                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(buffer);
                System.out.println("发送消息: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new NioClient().start();
    }
}

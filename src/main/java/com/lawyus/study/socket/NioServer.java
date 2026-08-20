package com.lawyus.study.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 7650;

    public void start() throws IOException {
        // 创建Selector
        selector = Selector.open();

        // 创建ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(PORT));

        // 注册Accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动，监听端口: " + PORT);

        while (true) {
            // 阻塞等待就绪的Channel
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        // 注册读事件，准备接收客户端消息
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("客户端连接: " + clientChannel.getRemoteAddress());

        // 发送欢迎消息
        String welcomeMsg = "欢迎连接到服务器!";
        ByteBuffer buffer = ByteBuffer.wrap(welcomeMsg.getBytes());
        clientChannel.write(buffer);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                // 客户端关闭连接
                channel.close();
                System.out.println("客户端断开连接");
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String message = new String(data);
            System.out.println("收到客户端消息: " + message);

            // 注册写事件，准备回复
            channel.register(selector, SelectionKey.OP_WRITE, message);

        } catch (IOException e) {
            channel.close();
            System.out.println("客户端异常断开");
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String receivedMsg = (String) key.attachment();

        // 构造回复消息
        String response = "服务器回复: " + receivedMsg.toUpperCase();
        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());

        channel.write(buffer);
        System.out.println("发送回复: " + response);

        // 重新注册读事件，继续接收消息
        channel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }
}

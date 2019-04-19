package com.yanhua.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xuyanhua
 * @description:
 * @date 2019/4/19 上午10:40
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //获取socket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        //获取通道管理器
        Selector selector = Selector.open();
        //监听
        serverSocketChannel.socket().bind(new InetSocketAddress(2323));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = server.accept();
                    clientChannel.write(ByteBuffer.wrap("hello world!".getBytes()));
                    //在与客户端连接成功后，为客户端通道注册SelectionKey.OP_READ事件。
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);
                    System.out.println("receive message from client, size:"
                            + buffer.position() + " msg: " + message);
                }
            }
        }
    }
}

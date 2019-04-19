package com.yanhua.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author xuyanhua
 * @description:
 * @date 2019/4/19 上午10:34
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        //打开socket通道
        SocketChannel channel = SocketChannel.open();
        //设置为非阻塞
        channel.configureBlocking(false);
        //获得通道管理器，或选择器
        Selector selector = Selector.open();
        //创建连接
        channel.connect(new InetSocketAddress("localhost", 2323));
        //将通道注册到通道管理器上
        channel.register(selector, SelectionKey.OP_CONNECT);

        //监听
        while (true) {
            selector.select();
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isConnectable()) {
                    SocketChannel channel1 = (SocketChannel) key.channel();
                    if (channel1.isConnectionPending()) {
                        //正在连接,则完成连接
                        channel1.finishConnect();
                    }
                    channel1.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    //有可读数据事件
                    SocketChannel channel1 = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    channel1.read(buffer);
                    byte[] data = buffer.array();
                    String s = new String(data);
                    System.out.println("receive message from server :size =" + buffer.position() + ", message=" + s);
                }

            }
        }

    }
}

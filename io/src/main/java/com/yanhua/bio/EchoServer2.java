package com.yanhua.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 基于SocketServer的BIO服务端代码
 * 每接收到一个请求会创建一个线程
 * 多线程版本，防止线程开启太多导致的oom
 *
 * @author xuyanhua
 * @description:
 * @date 2019/3/22 上午12:50
 */
public class EchoServer2 {
    private final static ExecutorService pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(10));

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(8080)) {
            while (true) {
                final Socket client = server.accept();
                try {
                    pool.execute(() -> {
                        try (InputStream inputStream = client.getInputStream()) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            String msg = br.readLine();
                            System.out.println("sever -->" + msg);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            if (client != null) {
                                try {
                                    client.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (RejectedExecutionException e) {
                    System.out.println("抛弃了一个任务:" + client.toString());
                    if (client != null) {
                        try {
                            client.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } finally {

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.yanhua.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于Socket的BIO客户端代码
 *
 * @author xuyanhua
 * @description:
 * @date 2019/3/22 上午12:55
 */
public class EchoClient {
    private final static AtomicLong id = new AtomicLong();

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            Socket client = null;
            try {
                client = new Socket("localhost", 8080);
                OutputStream out = client.getOutputStream();
                out.write(("hello-" + id.incrementAndGet()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

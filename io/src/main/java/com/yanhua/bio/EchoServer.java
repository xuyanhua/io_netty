package com.yanhua.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xuyanhua
 * @description:
 * @date 2019/3/22 上午12:50
 */
public class EchoServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8080)) {
            while (true) {
                final Socket client = server.accept();
                new Thread(() -> {
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
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

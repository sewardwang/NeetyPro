package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: wangzh
 * @Date: 2021/5/21 17:46
 * @Description:
 */
public class BIOServer {
    public static void main(String[] args) {
        // 线程池机制
        //思路
        //1、创建一个线程池
        //2、如果有客户端连接，就创建一个线程与之通讯（单独写一个方法）

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建ServiceSocket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("服务器启动了");

        while (true) {

            System.out.println("线程信息 ID =" + Thread.currentThread().getId() + "线程信息 名字 =" + Thread.currentThread().getName());
            System.out.println("等待客户端连接");
            //监听，等待客户端连接

            Socket socket = null;
            try {
                assert serverSocket != null;
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("连接到一个客户端");

            //就创建一个线程、与之通讯(单独写一个方法)
            final Socket finalSocket = socket;
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {//我们重写
                    //可以和客户端通讯
                    handler(finalSocket);

                }
            });
        }
    }

    //编写一个handler方法、和客户端通讯
    public static void handler(Socket socket) {

        try {
            System.out.println("线程信息 ID =" + Thread.currentThread().getId() + "线程信息 名字 =" + Thread.currentThread().getName());


            //用一个数组来接收数据
            byte[] bytes = new byte[1024];

            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环的读取客户发送的数据
            while (true) {
                System.out.println("线程信息 ID =" + Thread.currentThread().getId() + "线程信息 名字 =" + Thread.currentThread().getName());
                System.out.println("read.....");

                int read = inputStream.read();
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                    System.out.print(new String(bytes, 0, read));

                    //输出客户端发送的数据
                } else {
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


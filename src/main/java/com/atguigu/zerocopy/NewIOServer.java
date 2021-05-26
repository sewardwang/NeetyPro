package com.atguigu.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/25 16:37
 * @Description :
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {

        //开启一个7001 Socket端口地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);

        //ServerSocket通道关联到上面的地址
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //
        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(inetSocketAddress);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while ( -1 != readCount){
                try {
                    readCount = socketChannel.read(byteBuffer);

                }catch (Exception e){
                   break;
                }
                byteBuffer.rewind();//倒带 相当于position = 0 mark 作废 ，下次可以继续用byteBuffer处理
            }


        }


    }
}

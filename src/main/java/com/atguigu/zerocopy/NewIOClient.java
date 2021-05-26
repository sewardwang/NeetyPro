package com.atguigu.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/25 16:43
 * @Description :
 */
public class NewIOClient {
    public static void main(String[] args) throws Exception{

        SocketChannel socketChannel = SocketChannel.open();
        // socketChannel.configureBlocking(false);

       // InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",7001);

        socketChannel.connect(new InetSocketAddress("127.0.0.1",7001));

        String fileName="erlang_rabbitmq.rar";

        //得到一个文件的channel

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        //准备发送
        long startTime = System.currentTimeMillis();

        //在Linux下一个transferTo方法可以完成传输
        //在windows下 一次调用只能发送transferTo 只能发送8m,就需要分段传输文件
        //记录传输位置，下一次再在这个位置进行传输


        long longCount = (long) Math.ceil(fileChannel.size()/1024/1024/8);

        for (int i = 0; i < longCount; i++) {

            long transferCount = fileChannel.transferTo(i, fileChannel.size(), socketChannel);

            System.out.println("发送的总的字节数 = "+ transferCount + "耗时" +(System.currentTimeMillis()-startTime));
        }

//        long position = 0;
//        long size = fileChannel.size();
//        long total = 0;
//      //  long startTime = System.currentTimeMillis();
//        while (position < size) {
//
//            long transfer = fileChannel.transferTo(position, fileChannel.size(), socketChannel);
//            System.out.println("发送：" + transfer);
//            if (transfer <= 0) {
//                break;
//            }
//            total += transfer;
//            position += transfer;
//        }
        //关闭
        fileChannel.close();


    }
}

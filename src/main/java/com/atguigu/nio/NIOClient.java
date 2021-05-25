package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 19:08
 * @Description :
 */
public class NIOClient {
    public static void main(String[] args) throws  Exception{

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置非阻塞模式
        socketChannel.configureBlocking(false);

        //提供服务器端的IP与端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){

            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }

        //如果连接成功就发送数据
        String str = "Hello,尚硅谷       ";

        //Wraps a byte array into a buffer  包裹中的数据放到buffer 比较方便
        //以前的普通写法
        //将 str 放入到byteBuffer
        //byteBuffer.put(str.getBytes());
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        //发送数据，将buffer写入到channel

        socketChannel.write(buffer);

        //代码停在这读一个
        System.in.read();


    }
}

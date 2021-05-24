package com.atguigu.nio;

/**
* @Author: Wangzh
* @Date: 2021/5/24 17:44
* @Description:
*/

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;


 //* Scattering: 表示将数据写入到buffer时，可以采用buffer数组，依次写入[分散]
 //* Gathering: 从buffer读取数据时，也可以采用buffer数组，依次读

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {

        //使用ServerSocketChannel 获取与SocketChannel 网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到Socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组 类似于服务器端
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接(telnet) 只要成功相当于得到通道关联了服务端
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8; //假定从客户端接收8个字节
        //循环的读取
        while (true) {
            try {
                int byteRead = 0;


                while (byteRead < messageLength) {
                    long l = socketChannel.read(byteBuffers);
                    byteRead += l;//累计读取的字节数
                    System.out.println("byteRead=" + byteRead);
                    //使用流打印,看看当前这个buffer
                    Arrays.stream(byteBuffers).map(buffer -> "position =" +
                            buffer.position() + ", limit =" + buffer.limit()).forEach(System.out::println);

                }


                //将所有buffer进行flip
                Arrays.asList(byteBuffers).forEach(Buffer::flip);

                //将数据读取显示到客户端
                long byteWrite = 0;

                while (byteWrite < messageLength) {
                    long l = socketChannel.write(byteBuffers);//
                    byteWrite += l;
                }
                //将所有buffer进行clean操作
                Arrays.asList(byteBuffers).forEach(Buffer::clear);

                System.out.println("byreRead:=" + byteRead + " byteWire:=" + byteWrite + " messageLength:=" + messageLength);


            } catch (Exception e ){
                e.printStackTrace();
            }
        }


    }
}

package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 11:59
 * @Description :
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{

        //创建文件的输入流
        File file = new File("D:\\file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream获取通道 实际类型为FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());



        //将通道数据放入到缓冲区
        fileChannel.read(byteBuffer);

        //将缓冲区byteBuffer的字节转成字符串String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();





    }
}

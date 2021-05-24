package com.atguigu.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 11:40
 * @Description :
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{


        String string = "hello,振华";
        //创意个输出流->channel

        FileOutputStream fileOutputStream = new FileOutputStream("d:/file01.txt");

        //通过 fileOutputStream 获取 对应 的 FileChannel fileOutputStream原生类其实包含了fileChannel 实际类型为FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将 str 放入到byteBuffer
        byteBuffer.put(string.getBytes());

        //对byteBuffer进行flip反转
        byteBuffer.flip();

        //将byteBuffer数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}

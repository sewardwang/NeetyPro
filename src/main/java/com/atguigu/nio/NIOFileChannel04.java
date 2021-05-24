package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 14:19
 * @Description :
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("d:\\a.jpg");

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a2.jpg");


        FileChannel destCh = fileOutputStream.getChannel();

        FileChannel sourceCh = fileInputStream.getChannel();

        //使用transferForm完成拷贝 通道进行拷贝比用缓冲区进行拷贝要方便的多，底层也是类似上个实例
        destCh.transferFrom(sourceCh,0,sourceCh.size());

        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}

package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 12:17
 * @Description :
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        //定义文件
        File file = new File("d:\\1.txt");

        //输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        //定义输入流通道
        FileChannel fileChannel = fileInputStream.getChannel();


        //定义输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\2.txt");

        //定义输出流通道
        FileChannel fileChannel1 = fileOutputStream.getChannel();


        //定义缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //通过通道读取文件中的数据放入到缓冲区
        while (true) {
            //这里有一个重要的操作，一定不要忘记 清空buffer
            /*
            * public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }*/
            byteBuffer.clear();
            int read = fileChannel.read(byteBuffer);
            System.out.println("read = "+read);
            //read = -1表示读完
            if (read == -1) {
                break;
            }

            //缓冲中的数据进行反转
            byteBuffer.flip();
            //把缓冲区数据写入到输出流通道上
            fileChannel1.write(byteBuffer);

        }

        //关闭输入输出流
        fileInputStream.close();
        fileOutputStream.close();
    }
}

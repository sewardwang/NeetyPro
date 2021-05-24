package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 15:20
 * @Description :
 *
 */

/*
*说明
* 1、MappedByteBuffer可以让文件直接再内存(堆外内存)修改，操作系统不需要拷贝一次 性能非常高
* */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:/1.txt", "rw");

        //获取对应的文件通道
        FileChannel channel = randomAccessFile.getChannel();

        /*
        * 参数1：FileChannel.MapMode.READ_WRITE 使用的是读写模式 在
        * 参数2: 0 代表可以修改的起始位置
        * 参数3： 5 代表映射到内存中的大小 意思将1.txt最多可以映射5个字节，
        * 可以修改字节的范围就是0-5字节范围内 意思到5的位置 5并不能被修改 否则会被报错IndexOutOfBoundsException 从0开始的
        * 实际类型为DirectByteBuffer
        * */

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) '1');
        mappedByteBuffer.put(3,(byte)'9');
        mappedByteBuffer.put(5,(byte)'Y');

        randomAccessFile.close();
        System.out.println("修改成功");


    }
}


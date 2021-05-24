package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 15:01
 * @Description :
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {

        //创建一个BUFFER
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        //按照类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9L);
        byteBuffer.putChar('王');
        byteBuffer.putShort((short) 4);

        //取出反转
        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}

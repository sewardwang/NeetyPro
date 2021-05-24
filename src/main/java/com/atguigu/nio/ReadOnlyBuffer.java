package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 15:12
 * @Description :
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {

        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            buffer.put((byte)i);


        }
        //读取翻转
        buffer.flip();

        //得到一个只读的buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        while (readOnlyBuffer.hasRemaining()){

            System.out.println(readOnlyBuffer.get());

        }

        readOnlyBuffer.putInt(11);
    }
}

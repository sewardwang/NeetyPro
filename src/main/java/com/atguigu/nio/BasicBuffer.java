package com.atguigu.nio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channel;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 10:27
 * @Description :
 */
public class BasicBuffer {
    public static void main(String[] args) {


        //举例说明简单buffer的使用
        //首先创建一个buffer buffer有很多类型，下面创建一个了 intBuffer 可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer中存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);


        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }


        //如何从buffer读取数据如果buffer 还有剩余进行打印输出,get类似指针一个一个向后延续
        //向buffer转换，读写切换 反转的主要所用就是告诉缓冲区把 赋值给limit=position 再把position置为0 下一个指针数据为0取完了的意思

        /*
        * public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }*/
        intBuffer.flip();
        intBuffer.position(1);// 1 , 2
        intBuffer.limit(3);
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }


    }
}

package com.atguigu.nio;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 15:40
 * @Description :
 */

import java.nio.channels.ServerSocketChannel;

/**
 * Scattering: 表示将数据写入到buffer时，可以采用buffer数组，依次写入[分散]
 * Gathering: 从buffer读取数据时，也可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) {

        //使用ServerSocketChannel 与SocketChannel

        Class<ServerSocketChannel> serverSocketChannelClass = ServerSocketChannel.class;



    }
}

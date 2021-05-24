package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author : wangzh
 * @Date : 2021/5/24 18:17
 * @Description :
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{

        //创建ServerSocketChannel  ->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666,在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到selector 关联的事件为OPEN_ACCEPT 连接事件

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){

            //等待一秒，也可以用selector.selectNow,如果没事件发生返回
            if (selector.select(1000) == 0){//没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回的不>0 ,就获取到相关的SelectionKey集合
            //1、如果返回>0，表示已经获取到关注的事件
            //2、selector.selectedKeys()关注事件的集合
            //通过selector.selectedKeys()反向获取通道
            // 意思是有事件发生的key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey>  使用迭代器遍历

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){

                //获取到SelectionKey
                SelectionKey key = keyIterator.next();

                //根据当前key对应的通道发生的事件做相应的处理
                if (key.isConnectable()) {//如果发生的是OP_ACCEPT 代表有新的客户端来连接了
                    //给该客户端生产一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将当前socketChannel注册到selector,关联的事件为OP_READ，同时给socketChannel
                    //管理一个buffer

                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                if (key.isReadable()){//发生OP_READ事件 与上面OP_READ是两个不同的业务
                    //通过key 反向获取channel
                    key.cancel();
                    SocketChannel channel = (SocketChannel) key.channel();

                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();

                    //将当前channel读取到buffer中去
                    channel.read(buffer);
                    System.out.println("form 客户端" + new String(buffer.array()));

                    channel.close();



                }

                //手动从集合中(迭代器)移除当前的selectorKey 因为是多线程，防止重复操作
                keyIterator.remove();


            }



        }
    }
}

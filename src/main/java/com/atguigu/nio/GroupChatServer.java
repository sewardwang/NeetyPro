
package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author : wangzh
 * @Date : 2021/5/25 10:11
 * @Description :
 */
public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer() {

        try {

            //得到选择器
            selector = Selector.open();
            //初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到 Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    //监听
    public void listen() {

        System.out.println("监听线程: " + Thread.currentThread().getName());

        try {

            //循环处理
            while (true) {

                int count = selector.select();

                if (count > 0) {//有事件处理

                    //遍历得到的selectionKey集合
                    Iterator<SelectionKey> Iterator = selector.selectedKeys().iterator();

                    //遍历是否还有下一个
                    while (Iterator.hasNext()) {
                        //取出这个个selectedKey
                        SelectionKey key = Iterator.next();

                        //监听到是accept

                        if (key.isAcceptable()) {

                            SocketChannel sc = listenChannel.accept();

                            //一定要记得把通道设置为非阻塞模式
                            sc.configureBlocking(false);
                            //将该 sc 注册到 selector上
                            sc.register(selector, SelectionKey.OP_READ);

                            //提示上线
                            System.out.println(sc.getRemoteAddress() + " 上线了..");

                        }


                        //读的事件 通道发生Read事件，即通道是可读的状态，从通道读数据到buffer中去
                        if (key.isReadable()) {
                            //处理读（专门写方法..)
                            try {
                                readData(key);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        //当前key 删除 防止重复处理
                        Iterator.remove();


                    }

                } else
                    System.out.println("等待.....");


            }

        } catch (Exception e) {
            e.printStackTrace();


        }

    }


    //读取客户端消息
    private void readData(SelectionKey key) {

        //定义一个socketChannel 取到关联的channel
        SocketChannel channel = null;

        try {

            //得到channel
            channel = (SocketChannel) key.channel();

            //创建一个buffer

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);

            //根据count的值进行不同的处理
            if (count > 0) {

                //把缓冲区的数据转换处理
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("form 客户端: " + msg.trim());

                //还要向其他客户端转发消息(要排除自己) 专门写一个方法处理
                sendInfoToOtherClient(msg, channel);
            }


        } catch (IOException e) {

            try {
                System.out.println(channel.getRemoteAddress() + " 离线了..");

                //取消注册 关闭通道
                key.cancel();

                channel.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // e.printStackTrace();
        }

    }

    //转发消息给其他客户端,(其实就是给不同的通道进行转发）
    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {


        System.out.println("服务器转发消息中.....");
        //遍历所有注册到selector上的socketChannel并排除自己self

        for (SelectionKey key : selector.keys()) {

            //通过key 取出对应socketChannel 也可以用上面强转的形式，这是以接口的形式

            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {

                //转型转成SocketChannel
                SocketChannel dest = (SocketChannel) targetChannel;

                //将msg 存储到一个buffer

                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

                //将buffer的数据写入到通道中

                dest.write(buffer);
            }
        }


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();


        //监听
        groupChatServer.listen();


    }


}

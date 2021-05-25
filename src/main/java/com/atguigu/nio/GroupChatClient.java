package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;


/**
 * @Author : wangzh
 * @Date : 2021/5/25 10:58
 * @Description :
 */
public class GroupChatClient {


    private final Selector selector;
    private final SocketChannel socketChannel;
    private final String username;

    //构造器 完成初始化工作
    public GroupChatClient() throws IOException {


        //得到选择器
        selector = Selector.open();
        //得到通道
        //服务器的端口
        int PORT = 6667;
        //定义相关的属性
        //服务器的ip
        String HOST = "127.0.0.1";
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

//
//            //连接到服务器端
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);

        //设置为非阻塞模式
        socketChannel.configureBlocking(false);

        //将channel注册到selector选择器
        socketChannel.register(selector, SelectionKey.OP_READ);

        //得到username

        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " is ok...");


//            if (!socketChannel.connect(inetSocketAddress)){
//
//                while (!socketChannel.finishConnect())
//                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
//            }


    }

    //向服务器发送消息
    public void sendInfo(String info) {

        info = username + "说" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //读取从服务器端回复的消息
    public void readInfo() {
        try {

            int readChannel = selector.select();

            //读取到数据即有可用的通道，即有事件发送的通道
            if (readChannel > 0) {

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();

                    //是可读的就得到相关的通道
                    if (key.isReadable()) {

                        SocketChannel sc = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        sc.read(buffer);

                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());


                    }
                }
                //记得每次操作完需要删除单枪的selectionKey，防止重复操作
                iterator.remove();

            }//System.out.println("没有可用的通道....");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        //启动我们的客户端

        GroupChatClient chatClient = new GroupChatClient();

        //启动我们的线程,每隔3秒读取从服务器端可能发送的数据
        new Thread(() -> {

            while (true) {
                chatClient.readInfo();
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //客户端发送数据给服务端 创建一个扫描器来进行处理
        Scanner scanner = new Scanner(System.in);

        //循环只要他还有下一行我就进行读取 得到客户端输入发送出去
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }


    }
}

package com.atguigu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/26 10:00
 * @Description :
 */
public class NettyClient {

    public static void main(String[] args) throws Exception{

        //客户端需要一个事件循环组 使用EventLoopGroup接收
        EventLoopGroup group = new NioEventLoopGroup();



        try {
            //创建客户端启动助手 客户端使用的是Bootstrap 服务器端才是ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();

            //设置客户端相关参数  还是用链式编程
            bootstrap.group(group)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());//加入自己的处理器handler
                        }
                    });

            System.out.println("客户端 ok ...");

            //启动客户端去连接服务器端
            //关于ChannelFuture要分析，设计到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();




        } finally {
            group.shutdownGracefully();

        }


    }
}

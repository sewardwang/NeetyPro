package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author : wangzh
 * @Date : 2021/5/26 9:14
 * @Description :
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        //创建BossGroup 和 WorkGroup
        //说明
        //1、创建两个线程组 boosGroup 和workGroup
        //2、bossGroup只是处理连接骑牛，真正和客户端业务处理，会交给workGroup完成
        //3、两个都是无限循环
        //4、bossGroup含有子线程（NioEventLoop）个数默认是CPU的核数*2

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        EventLoopGroup workGroup = new NioEventLoopGroup(8);

        try {
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //会使用链式编程的方式来进行设置
            bootstrap.group(bossGroup, workGroup) //设置2个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel做为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象(匿名对象)
                        //向workGroup给pipeLine设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户socketChannel hashcode= " +ch.hashCode());
                            //可以使用一个集合管理SocketChannel，在需要推送消息时，可以将业务加入到各个channel对应NioEventLoop的TaskQueue或者ScheduleTaskQueue
                            ch.pipeline().addLast(new NettyServerHandler());

                        }
                    });//给我们workGroup的EventLoop对应的管理设置处理器

            System.out.println("....服务器 is ready....");

            //绑定一个端口并且同步处理，生成一个 channelFuture 对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器 监控我们关心的事件

            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("监听端口 6668 成功");
                    }else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }


    }
}

package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author : wangzh
 * @Date : 2021/5/26 12:15
 * @Description :
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty 提供的httpServerCodec codec=>[coder - decoder]

        //HttpServerCodec说明
        //1、HttpServerCodec说明是netty提供的http的 编码-解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2、增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());


    }
}


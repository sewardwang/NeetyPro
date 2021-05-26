package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


/**
 * @Author : wangzh
 * @Date : 2021/5/26 12:13
 * @Description :
 * 1、SimpleChannelInboundHandler是ChannelInboundHandlerAdapter 的子类
 * 2、HttpObject 客户端和服务端相互通讯的数据呗封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {

            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址是 " + ctx.channel().remoteAddress());

            System.out.println(" pipeLine hashCode " + ctx.pipeline().hashCode() + " TestHttpServerHandler " + this.hashCode());
            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取到uri
            URI uri = new URI(httpRequest.uri());

            //如果favicon.ico网站图标地址刚好等于请求地址，刚好对他进行过滤
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应，即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            //回复给浏览器http协议
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建好的 response返回
            ctx.writeAndFlush(response);

        }
    }
}
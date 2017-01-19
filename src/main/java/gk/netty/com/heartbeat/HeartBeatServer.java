package gk.netty.com.heartbeat;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty心跳机制服务器端实现
 * Project Name:NettyDemo 
 * @author gk
 * TODO
 * Date:2016年11月11日上午10:34:07 
 * Copyright (c) 2016, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class HeartBeatServer {
	
	private int port;//端口号
	
	public HeartBeatServer() {
		
	}
	
	public HeartBeatServer(int port) {
		this.port = port;
	}

	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
						ch.pipeline().addLast("decoder", new StringDecoder());
						ch.pipeline().addLast("encoder", new StringEncoder());
						ch.pipeline().addLast(new HeartBeatServerHandler());
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// 绑定端口，开始接收进来的连接
			ChannelFuture future = sb.bind().sync();
			
			System.out.println("Server start listen to port : " + port);
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			
		}finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new HeartBeatServer(8080).start();
	}

}

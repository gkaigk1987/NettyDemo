package gk.netty.com.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * netty心跳机制客户端实现
 * Project Name:NettyDemo 
 * @author gk
 * TODO
 * Date:2016年11月11日上午10:51:16 
 * Copyright (c) 2016, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class HeartBeatClient {

	private int port;//端口号
	
	private String host;//主机

	public HeartBeatClient() {
		super();
	}

	public HeartBeatClient(int port, String host) {
		this.port = port;
		this.host = host;
	}
	
	public void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bs = new Bootstrap();
			bs.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
			.remoteAddress(host, port).option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("ping",new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
					ch.pipeline().addLast("decoder", new StringDecoder());  
					ch.pipeline().addLast("encoder", new StringEncoder());  
					ch.pipeline().addLast(new HeartBeatClientHandler());  
				}
			});
			ChannelFuture f = bs.connect().sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {

		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		new HeartBeatClient(8080, "localhost").start();
	}
}

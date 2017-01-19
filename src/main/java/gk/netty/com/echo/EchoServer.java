package gk.netty.com.echo;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	
	private int port;
	
	public EchoServer() {
		
	}
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public void start() throws Exception{
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(group).channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new EchoServerHandler());
					}
				});
			ChannelFuture future = sb.bind().sync();
			System.out.println(EchoServer.class.getName()+" started and listen on " 
					+ future.channel().localAddress());
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new EchoServer(8080).start();
	}

}

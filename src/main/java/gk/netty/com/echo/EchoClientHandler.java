package gk.netty.com.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	/**
	 * 服务器的连接被建立后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty 你好!",CharsetUtil.UTF_8));
	}

	/**
	 * 捕获一个异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 数据从服务器接收到调用
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		System.out.println("Client Received : " + msg.toString(CharsetUtil.UTF_8));
	}

}

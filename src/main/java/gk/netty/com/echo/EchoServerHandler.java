package gk.netty.com.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 每个信息入站都会调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf byteBuf = (ByteBuf)msg;
		System.out.println("服务器接收：" + byteBuf.toString(CharsetUtil.UTF_8));
		ctx.write(byteBuf);
	}

	/**
	 * 通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
			.addListener(ChannelFutureListener.CLOSE);//冲刷所有待审消息到远程节点。关闭通道后，操作完成
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}

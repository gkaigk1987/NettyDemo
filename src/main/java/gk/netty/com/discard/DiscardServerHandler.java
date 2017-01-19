package gk.netty.com.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//		((ByteBuf)msg).release();//丢弃数据
		
		//打印接收信息
		ByteBuf in = (ByteBuf)msg;
		try{
			while(in.isReadable()) {
				System.out.println((char)in.readByte());
				System.out.flush();;
			}
		}finally {
			ReferenceCountUtil.release(msg);
		}
		
//		ctx.writeAndFlush(msg);//返回内容到客户端//Echo Server
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}

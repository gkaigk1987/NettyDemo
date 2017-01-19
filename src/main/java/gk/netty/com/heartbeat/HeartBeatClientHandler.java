package gk.netty.com.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

/**
 * netty心跳机制客户端Handler
 * Project Name:NettyDemo 
 * @author gk
 * TODO
 * Date:2016年11月11日上午10:38:33 
 * Copyright (c) 2016, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
	
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));
	
	private static final int TRY_TIMES = 3;//循环发动次数
	
	private int currentTime = 0;//当前次数

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("激活时间："+ new Date());
		 System.out.println("HeartBeatClientHandler channelActive");
//		 ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("停止时间是："+new Date());  
        System.out.println("HeartBeatClientHandler channelInactive");  
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String message = (String) msg;  
        System.out.println(message);  
//        if (message.equals("HEARTBEAT")) {  
//            ctx.write("Client has read message from server");  
//            ctx.flush();  
//        }  
        ReferenceCountUtil.release(msg);  
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		System.out.println("客户端循环触发时间："+new Date());
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state() == IdleState.WRITER_IDLE) {
				if(currentTime <= TRY_TIMES) {
					System.out.println("currentTime:"+currentTime); 
					currentTime++;
					ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
				}
			}
		}else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
	
}

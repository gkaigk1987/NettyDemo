package gk.netty.com.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * netty心跳机制服务器端handler
 * Project Name:NettyDemo 
 * @author gk
 * TODO
 * Date:2016年11月11日上午10:03:01 
 * Copyright (c) 2016, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
	
	private int loss_connect_times = 0;//连接丢失此处
	
	private final int loss_max_times = 5;//连接最大允许丢失次数

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Server read...");
		System.out.println(ctx.channel().remoteAddress() + "->Server:" + msg.toString());
		ctx.channel().writeAndFlush(msg.toString());
	}

	/**
	 * 服务器端在指定时间内未接收到信息时会调用此方法
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if(evt instanceof IdleStateEvent) {
			//是心跳检测事件
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state() == IdleState.READER_IDLE) {
				loss_connect_times++;
				System.out.println("5秒未接收到客户端的信息了！");
				if(loss_connect_times > loss_max_times) {
					//如果未接收信息的次数大于5次则关闭该连接通道
					System.out.println("关闭该不活跃的channel！");
					ctx.channel().close();
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

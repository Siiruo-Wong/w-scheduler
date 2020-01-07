package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.ExecutorParameter;
import com.siiruo.wscheduler.core.bean.IExecutor;
import com.siiruo.wscheduler.core.exception.WSchedulerRpcException;
import com.siiruo.wscheduler.core.util.JsonUtil;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by siiruo wong on 2020/1/7.
 */
public class RpcClientChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientChannelHandler.class);
    private ThreadPoolExecutor poolExecutor;
    private IExecutor clientExecutor;
    public RpcClientChannelHandler(ThreadPoolExecutor poolExecutor, IExecutor clientExecutor) {
        this.poolExecutor = poolExecutor;
        this.clientExecutor = clientExecutor;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        final byte[] requestBytes = ByteBufUtil.getBytes(msg.content());
        final String uri = msg.uri();
        final boolean keepAlive = HttpUtil.isKeepAlive(msg);
        poolExecutor.execute(()->process(ctx, uri, requestBytes, keepAlive));
    }

    private void process(ChannelHandlerContext ctx, String uri, byte[] requestBytes, boolean keepAlive){
        try {
            if (requestBytes.length == 0) {
                throw new WSchedulerRpcException("w-scheduler client receive an empty request.");
            }
            ExecutorParameter parameter = JsonUtil.toBean(requestBytes, ExecutorParameter.class);
            clientExecutor.execute(parameter);
            byte[] responseBytes = JsonUtil.toJSONBytes("success");
            writeResponse(ctx, keepAlive, responseBytes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            byte[] responseBytes =JsonUtil.toJSONBytes("failure");
            writeResponse(ctx, keepAlive, responseBytes);
        }
    }

    /**
     * write response
     */
    private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, byte[] responseBytes){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");       // HttpHeaderValues.TEXT_PLAIN.toString()
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("w-scheduler client caught an exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            ctx.channel().close();
            LOGGER.debug("w-scheduler client closing an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

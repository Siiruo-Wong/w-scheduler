package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.ClientExecutor;
import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.client.config.WSchedulerClient;
import com.siiruo.wscheduler.core.bean.AbstractExecutor;
import com.siiruo.wscheduler.core.bean.Worker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by siiruo wong on 2020/1/7.
 */
public class RpcClientWorker implements Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientWorker.class);
    private WSchedulerClient client;
    private AbstractExecutor executor;

    public RpcClientWorker(WSchedulerClient client, AbstractExecutor executor) {
        this.client = client;
        this.executor = executor;
    }

    @Override
    public void work() {
         ThreadPoolExecutor serverHandlerPool =  new ThreadPoolExecutor(100, 500, 60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r-> new Thread(r, "RpcClientWorker"),
                (r,e)->{
                    //throw new Exception("Thread pool is EXHAUSTED!");
                });
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new IdleStateHandler(0, 0, WSchedulerConstantType.BEAT_TIME_PERIOD * 3, TimeUnit.SECONDS))
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                    .addLast(new RpcClientChannelHandler(serverHandlerPool,executor));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(client.getClientPort()).sync();
            LOGGER.info("w-scheduler rpc server started successfully.");
            onStart();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            if (e instanceof InterruptedException) {
                LOGGER.info("w-scheduler rpc server stopped.");
            } else {
                LOGGER.error("w-scheduler rpc server stopped error.", e);
            }
        } finally {
            // stop
            try {
                serverHandlerPool.shutdown();	// shutdownNow
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            try {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onStart() {
        //注册
    }

    @Override
    public void onStop() {
        //1、移除删除注册
        //2、注销线程
    }
}

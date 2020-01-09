package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.client.config.WSchedulerClient;
import com.siiruo.wscheduler.core.bean.*;
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
public class ExecutionCoordinator implements Worker,Sensor<ExecutionCoordinator> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionCoordinator.class);
    private WSchedulerClient client;
    private AbstractExecutor executor;//ClientExecutor
    private ThreadDecoratedLauncher registerWorker;
    private volatile boolean working;

    public ExecutionCoordinator(AbstractExecutor executor) {
        this.executor = executor;
        this.client=WSchedulerContextHolder.getClient();
    }
    @Override
    public void work() {
        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(100, 500, 60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r-> new Thread(r, "ExecutionCoordinator"),
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
                                    .addLast(new RpcClientChannelHandler(threadPoolExecutor,executor));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(client.getClientPort()).sync();//sensitive to InterruptedException
            LOGGER.info("w-scheduler rpc server started successfully.");
            onStart(this);
            future.channel().closeFuture().sync();//sensitive to InterruptedException
        } catch (InterruptedException e) {
            if (e instanceof InterruptedException) {
                LOGGER.info("w-scheduler rpc server stopped.");
            } else {
                LOGGER.error("w-scheduler rpc server stopped error.", e);
            }
        } finally {
            try {
                threadPoolExecutor.shutdown();
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
    public void interrupt() {
        onStop(this);
    }

    public boolean status() {
        return this.working;
    }

    @Override
    public void onStart(ExecutionCoordinator target) {
        if (!status()) {
            this.working =true;
            this.registerWorker=new ThreadDecoratedLauncher(SchedulingLauncher.THREAD_GROUP,"registerWorker",new RegistrationCoordinator());
            this.registerWorker.start();
        }
    }

    @Override
    public void onStop(ExecutionCoordinator target) {
        if (status()) {
            this.working =false;
            this.registerWorker.stop();
        }
    }
}

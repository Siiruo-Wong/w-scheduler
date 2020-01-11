package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.bean.WSchedulerConstantType;
import com.siiruo.wscheduler.client.business.ExecutionHandler;
import com.siiruo.wscheduler.client.config.WSchedulerClientConfig;
import com.siiruo.wscheduler.core.bean.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by siiruo wong on 2020/1/7.
 */
public class ExecutionWorker implements Worker,Sensor<ExecutionWorker> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionWorker.class);
    private final List<Launcher> childLaunchers=new LinkedList<>();
    private WSchedulerClientConfig clientConfig;
    private LifecycleExecutor executor;//ClientExecutor
    private volatile boolean working;

    public ExecutionWorker(LifecycleExecutor executor) {
        this.executor = executor;
        this.clientConfig =WSchedulerContextHolder.getClientConfig();
    }
    @Override
    public void work() {
        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(50, 200, 60L,
                                                    TimeUnit.SECONDS,
                                                    new LinkedBlockingQueue<>(1000),
                                                    r-> new Thread(r, "ExecutionWorker"),
                                                    (r,e)->new ThreadPoolExecutor.CallerRunsPolicy());
        // ThreadPool threadPool=new QueuedThreadPool(200,50,60*000,-1,new LinkedBlockingQueue<>(1000),null,r-> new Thread(r, "ExecutionWorker"));
        // Server server = new Server(threadPool);
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(this.clientConfig.getClientPort());
        server.setConnectors(new Connector[]{connector});

        ContextHandler contextHandler = new ContextHandler(WSchedulerConstantType.W_SCHEDULER_CLIENT_CONTEXT_PATH);
        contextHandler.setHandler(new ExecutionHandler(this.executor,threadPoolExecutor));
        contextHandler.setAllowNullPathInfo(true);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {contextHandler });
        server.setHandler(contexts);
        try {
            try {
                server.start();
            } catch (Exception e) {
                if (e.getCause() instanceof BindException) {
                    int port=this.clientConfig.getClientPort();
                    boolean isBind=false;
                    for (;port<=65535;port++){
                        try {
                            connector.setPort(port);
                            server.start();
                            isBind=true;
                            break;
                        } catch (Exception e1) {}
                    }
                    if(!isBind){
                        port=this.clientConfig.getClientPort();
                        for (;port>0;port--){
                            try {
                                connector.setPort(port);
                                server.start();
                                isBind=true;
                                break;
                            } catch (Exception e1) {}
                        }
                    }
                    if (isBind) {
                        this.clientConfig.setClientPort(port);
                    }else{
                        LOGGER.error("w-scheduler server started error.", e);
                    }
                }
            }
            onStart(this);
            server.join();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                LOGGER.error("w-scheduler rpc server stopped error.ExecutionWorker", e);
            }
        }finally {
            try {
                server.stop();
                threadPoolExecutor.shutdown();
                onStop(this);
            } catch (Exception e) {
                LOGGER.error("w-scheduler rpc server stopped error.", e);
            }
        }
    }

    @Override
    public void interrupt() {
        LOGGER.info("w-scheduler ExecutionWorker interrupt.");
        onStop(this);
    }

    @Override
    public void onStart(ExecutionWorker target) {
        if (!status()) {
            this.working =true;
            addChildLauncher(new ThreadLauncher(WSchedulerSchedulingLauncher.THREAD_GROUP,"registerWorker",new RegistrationWorker()));
            for (Launcher childLauncher : this.childLaunchers) {
                childLauncher.start();
            }
        }
    }

    @Override
    public void onStop(ExecutionWorker target) {
        LOGGER.info("w-scheduler ExecutionWorker onStop.");
        if (status()) {
            this.working =false;
            for (Launcher childLauncher : this.childLaunchers) {
                childLauncher.stop();
            }
        }
    }

    public boolean status() {
        return this.working;
    }

    public void addChildLauncher(Launcher launcher){
        if (launcher==null) {
            return;
        }
        this.childLaunchers.add(launcher);
    }
}

package com.siiruo.wscheduler.client.business;

import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.*;
import com.siiruo.wscheduler.core.exception.WSchedulerRemoteException;
import com.siiruo.wscheduler.core.type.*;
import com.siiruo.wscheduler.core.util.JsonUtil;
import com.sun.management.OperatingSystemMXBean;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * Created by siiruo wong on 2020/1/11.
 */
public class ExecutionHandler extends AbstractHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionHandler.class);
    private LifecycleExecutor executor;
    private ExecutorService threadPoolExecutor;
    private List<Dispatcher<HttpServletRequest,ResponseType>> dispatchers=new LinkedList<>();

    public ExecutionHandler(LifecycleExecutor executor, ExecutorService threadPoolExecutor) {
        this(executor);
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public ExecutionHandler(LifecycleExecutor executor) {
        this.executor = executor;
    }
    {
        this.dispatchers.add(new HealthCheckDispatcher());
        this.dispatchers.add(new ShowExecutorsDispatcher());
        this.dispatchers.add(new ExecutionDispatcher());
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            ResponseType responseType=null;
            for (Dispatcher<HttpServletRequest,ResponseType> dispatcher : this.dispatchers) {
                if (dispatcher.matches(request)) {
                    responseType=dispatcher.dispatch(request);
                    break;
                }
            }
            if (responseType==null) {
                responseType=new DefaultResponseType(new ResultType(ResponseCodeType.UN_KNOWN));
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer=null;
            try {
                writer = response.getWriter();
                writer.write(JsonUtil.toJSONString(responseType));
                writer.flush();
            } catch (IOException e) {

            } finally {
                if (writer!=null) {
                    writer.close();
                }
            }
    }

    private class HealthCheckDispatcher implements Dispatcher<HttpServletRequest,ResponseType>{
        @Override
        public boolean matches(HttpServletRequest input) {
            return ApiConstantType.EXECUTOR_HEALTH_CHECK_PATH_IN_CLIENT.equalsIgnoreCase(input.getPathInfo());
        }
        @Override
        public ResponseType dispatch(HttpServletRequest input) {
            OperatingSystemMXBean operatingSystemMXBean =(OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            double cpuUsed=operatingSystemMXBean.getSystemCpuLoad()*100;
            MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            double memUsed = heapMemoryUsage.getUsed();
            double memMax = heapMemoryUsage.getMax();
            double memInit = heapMemoryUsage.getInit();
            HealthCheckResponseType response=new HealthCheckResponseType();
            response.setCpu(cpuUsed);
            response.setMemInit(memInit);
            response.setMemUsed(memUsed);
            response.setMemMax(memMax);
            return response;
        }
    }

    private class ShowExecutorsDispatcher implements Dispatcher<HttpServletRequest,ResponseType>{
        @Override
        public boolean matches(HttpServletRequest input) {
            return ApiConstantType.EXECUTOR_SHOW_EXECUTORS_PATH_IN_CLIENT.equalsIgnoreCase(input.getPathInfo());
        }
        @Override
        public ResponseType dispatch(HttpServletRequest input) {
            ResponseType responseType=new ResponseType() {
                List<ExecutorInfo> executors;
                {
                    this.result =new ResultType(ResponseCodeType.SUCCESS);
                    this.executors= WSchedulerContextHolder.getRegisterInfo().getExecutors();
                }

                public List<ExecutorInfo> getExecutors() {
                    return executors;
                }

                public void setExecutors(List<ExecutorInfo> executors) {
                    this.executors = executors;
                }
            };
            return responseType;
        }
    }

    private class ExecutionDispatcher implements Dispatcher<HttpServletRequest,ResponseType>{
        @Override
        public boolean matches(HttpServletRequest input) {
            return ApiConstantType.EXECUTOR_EXECUTE_PATH_IN_CLIENT.equalsIgnoreCase(input.getPathInfo());
        }
        @Override
        public ResponseType dispatch(HttpServletRequest input) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                br = input.getReader();
                String str;
                while((str=br.readLine())!=null){sb.append(str);}
            } catch (IOException e) {

            } finally {
                if (br!=null) {
                    try {
                        br.close();
                    } catch (IOException e) {

                    }
                }
            }

            if (sb.length() == 0) {
                throw new WSchedulerRemoteException("w-scheduler client receive an empty request.");
            }

            ExecuteRequestType requestType = JsonUtil.toBean(sb.toString(), ExecuteRequestType.class);
            if (requestType==null||requestType.getParameter()==null) {
                throw new WSchedulerRemoteException("w-scheduler client receive an empty request.");
            }

            if (threadPoolExecutor!=null) {
                //async,if rejected then called by current thread in sync
                threadPoolExecutor.execute(()->executor.execute(requestType.getParameter()));
            }else{
                executor.execute(requestType.getParameter());
            }
            return new DefaultResponseType();
        }
    }
}

package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.business.NoticeHandler;
import com.siiruo.wscheduler.core.bean.Worker;
import com.siiruo.wscheduler.core.bean.NoticeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by siiruo wong on 2020/1/14.
 */
public class MemoryNoticeWorker implements Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryNoticeWorker.class);
    private static final LinkedBlockingQueue<NoticeParameter> notices=new LinkedBlockingQueue<>(1000);
    private NoticeHandler noticeHandler=new NoticeHandler();
    private volatile boolean working;
    @Override
    public void work() {
        for (;;){
            try {
                NoticeParameter firstNotice = notices.take();
                if (firstNotice != null) {
                    List<NoticeParameter> toNotices = new ArrayList<>();
                    toNotices.add(firstNotice);
                    notices.drainTo(toNotices);
                    noticeHandler.notice(toNotices);
                }
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    //exit
                    break;
                }
            }
            if (!this.working) {
                break;
            }
        }

        try {
            List<NoticeParameter> toNotices = new ArrayList<>();
            notices.drainTo(toNotices);
            this.noticeHandler.notice(toNotices);
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                //exit
            }
        }
    }
    @Override
    public void interrupt() {
        this.working=false;
    }

    public static void addNotice(NoticeParameter notice){
        if (notice!=null) {
            if (!notices.offer(notice)) {
                //persist to file
            }
        }
    }
}

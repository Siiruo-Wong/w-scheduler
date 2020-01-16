package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.business.NoticeHandler;
import com.siiruo.wscheduler.core.bean.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by siiruo wong on 2020/1/16.
 */
public class PersistenceNoticeWorker implements Worker{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceNoticeWorker.class);
    private NoticeHandler noticeHandler=new NoticeHandler();
    private volatile boolean working;
    @Override
    public void work() {
        for (;;){
            //todo

            if (!this.working) {
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        this.working=false;
    }
}

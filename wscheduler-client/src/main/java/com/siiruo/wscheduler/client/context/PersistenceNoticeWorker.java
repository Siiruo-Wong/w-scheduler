package com.siiruo.wscheduler.client.context;

import com.siiruo.wscheduler.client.business.NoticeHandler;
import com.siiruo.wscheduler.client.util.PersistenceUtil;
import com.siiruo.wscheduler.core.bean.NoticeParameter;
import com.siiruo.wscheduler.core.bean.Worker;
import com.siiruo.wscheduler.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;


/**
 * Created by siiruo wong on 2020/1/16.
 */
public class PersistenceNoticeWorker implements Worker{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceNoticeWorker.class);
    private NoticeHandler noticeHandler=new NoticeHandler();
    private volatile boolean working=true;
    @Override
    public void work() {
        for (;;){
            try {
                doWork();
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                 break;
            }catch (Exception e) {

            }
            if (!this.working) {
                break;
            }
        }
        doWork();//last clear
    }

    @Override
    public void interrupt() {
        this.working=false;
    }

    private void doWork(){
        Calendar passed1Day;
        Calendar passed2Day;
        //delete passed 2 days
        passed2Day= Calendar.getInstance();
        passed2Day.add(Calendar.DAY_OF_YEAR,-2);
        PersistenceUtil.delete(passed2Day.getTime());
        // extract today
        List<NoticeParameter> toNotices = PersistenceUtil.extract(Calendar.getInstance().getTime(),true);
        noticeHandler.notice(toNotices);
        passed1Day= Calendar.getInstance();
        passed1Day.add(Calendar.DAY_OF_YEAR,-1);
        toNotices = PersistenceUtil.extract(passed1Day.getTime(),true);
        noticeHandler.notice(toNotices);
    }
    public static void persist(List<NoticeParameter> notices){
        if (CollectionUtil.isEmpty(notices)) {
            return;
        }
        notices.forEach(notice->PersistenceUtil.persist(notice));
    }
}

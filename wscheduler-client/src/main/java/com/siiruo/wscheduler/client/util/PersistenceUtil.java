package com.siiruo.wscheduler.client.util;

import com.siiruo.wscheduler.client.context.WSchedulerContextHolder;
import com.siiruo.wscheduler.core.bean.NoticeParameter;
import com.siiruo.wscheduler.core.util.CompressUtil;
import com.siiruo.wscheduler.core.util.JsonUtil;
import com.siiruo.wscheduler.core.util.StringUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by siiruo wong on 2020/1/22.
 */
public final class PersistenceUtil {
    private static final String FORMAT="yyyy-MM-dd";
    private static final String CHARSET="UTF-8";
    private PersistenceUtil(){}
    public static String buildLogPath(Date date, String executeId) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        File logFile = new File(WSchedulerContextHolder.getClientConfig().getLogPath(), sdf.format(date));
        if (!logFile.exists()) {
            logFile.mkdir();
        }
        return logFile.getPath()
                .concat(File.separator)
                .concat(String.valueOf(executeId))
                .concat(".log");
    }

    public static void persist(NoticeParameter notice){
        String logPath=buildLogPath(new Date(),notice.getExecuteId());
        String originalLog= JsonUtil.toJSONString(notice);
        String compressedLog= CompressUtil.compress(originalLog);
        write(logPath,compressedLog);
    }


    public static List<NoticeParameter> extract(Date date,boolean isDelete){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        File logFile = new File(WSchedulerContextHolder.getClientConfig().getLogPath(), sdf.format(date));
        if (!logFile.exists()||!logFile.isDirectory()) {
            return new ArrayList<>();
        }
        File[] files = logFile.listFiles();
        if (files==null||files.length==0) {
            return new ArrayList<>();
        }
        List<NoticeParameter> notices=new ArrayList<>(files.length);
        String content;
        for (File file : files) {
            if (!StringUtil.isBlank(content=read(file))) {
                notices.add(JsonUtil.toBean(CompressUtil.decompress(content),NoticeParameter.class));
            }
            if (isDelete) {
                delete(file);
            }
        }
        return notices;
    }

    public static void delete(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        File logFile = new File(WSchedulerContextHolder.getClientConfig().getLogPath(), sdf.format(date));
        delete(logFile);
    }

    public static void delete(File file){
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files!=null&&files.length>0) {
                    for (File file1 : files) {
                        delete(file1);
                    }
                }
            }
            file.delete();
        }
    }
    public static String read(File file){
        FileInputStream input=null;
        try {
            byte[] bytes=new byte[(int)file.length()];//The file is not large and can be read at one time
            input=new FileInputStream(file);
            input.read(bytes);
            input.close();
            return new String(bytes,CHARSET);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (input!=null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void write(String file, String log) {
        if (StringUtil.isBlank(file)||StringUtil.isBlank(log)) {
            return;
        }
        File logFile = new File(file);
        if (logFile.exists()) {
            logFile.delete();
        }
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            return;
        }

        log += "\r\n";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile, false);//overwide
            fos.write(log.getBytes(CHARSET));
            fos.flush();
        } catch (Exception e) {

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

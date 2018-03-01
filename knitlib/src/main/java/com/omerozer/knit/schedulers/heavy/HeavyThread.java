package com.omerozer.knit.schedulers.heavy;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by omerozer on 2/28/18.
 */

public abstract class HeavyThread extends IntentService {

    private static final String RUNNABLE_KEY = "RUNNABLE_KEY";
    private static final String CALLABLE_KEY = "CALLABLE_KEY";
    private static final String TASK_TYPE_KEY = "TASK_TYPE_KEY";

    public static final int RUNNABLE = 1;
    public static final int CALLABLE = 2;


    private static Map<String,ConcurrentLinkedQueue<TaskPackage>> taskMap;

    static {
        taskMap = new ConcurrentHashMap<>();
    }

    private static TaskPackage getNextTask(String threadId){
        if(!taskMap.containsKey(threadId)){
            taskMap.put(threadId,new ConcurrentLinkedQueue<TaskPackage>());
            taskMap.get(threadId).add(TaskPackage.EMPTY);
            return taskMap.get(threadId).peek();
        }
        return taskMap.get(threadId).poll();
    }

    public static int getPriority(String threadId){
        return taskMap.get(threadId).size();
    }

    private String threadId;

    public HeavyThread(String name) {
        super(name);
        this.threadId = name;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TaskPackage currentTask = getNextTask(threadId);

//        switch (intent.getIntExtra(TASK_TYPE_KEY,RUNNABLE)){
//            case RUNNABLE:
//                extractRunnable(intent).run();
//                break;
//            case CALLABLE:
//                try {
//                    Object data = extractCallable(intent).call();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
    }

}

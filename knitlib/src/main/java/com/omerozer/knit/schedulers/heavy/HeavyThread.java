package com.omerozer.knit.schedulers.heavy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by omerozer on 2/28/18.
 */

public abstract class HeavyThread extends IntentService {

    private static final String TASK_TYPE_KEY = "TASK_TYPE_KEY";

    public static final int RUNNABLE = 1;
    public static final int CALLABLE = 2;


    private static Map<String,ConcurrentLinkedQueue<TaskPackage>> taskMap;

    public static void handleTask(String threadId, TaskPackage taskPackage,Context context,Class<? extends HeavyThread> taskThread){
        getTaskQueueForThread(threadId).add(taskPackage);
        Intent intent = new Intent(context,taskThread);
        intent.putExtra(TASK_TYPE_KEY,taskPackage.getRunnable()==null? CALLABLE:RUNNABLE);
        context.startService(intent);
    }

    static {
        taskMap = new ConcurrentHashMap<>();
    }

    private static TaskPackage getNextTask(String threadId){
        return taskMap.get(threadId).poll();
    }

    private static Queue<TaskPackage> getTaskQueueForThread(String threadId){
        if(!taskMap.containsKey(threadId)) {
            taskMap.put(threadId, new ConcurrentLinkedQueue<TaskPackage>());
        }
        return taskMap.get(threadId);
    }

    public static int getPriority(String threadId){
        return getTaskQueueForThread(threadId).size();
    }

    private String threadId;

    public HeavyThread(String name) {
        super(name);
        this.threadId = name;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final TaskPackage currentTask = getNextTask(threadId);
        Log.d("KNIT_TEST","HEAVY:"+threadId);
        switch (intent.getIntExtra(TASK_TYPE_KEY,RUNNABLE)){
            case RUNNABLE:
                currentTask.getRunnable().run();
                break;
            case CALLABLE:
                try {
                    final Object data = currentTask.getCallable().call();
                    currentTask.getTarget().start();
                    currentTask.getTarget().submit(new Runnable() {
                        @Override
                        public void run() {
                            currentTask.getConsumer().consume(data);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;

        }
    }

}

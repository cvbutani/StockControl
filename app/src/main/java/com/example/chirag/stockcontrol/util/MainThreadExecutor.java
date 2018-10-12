package com.example.chirag.stockcontrol.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {

    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable runnable) {
        mMainThreadHandler.post(runnable);
    }
}

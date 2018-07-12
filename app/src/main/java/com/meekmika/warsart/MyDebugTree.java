package com.meekmika.warsart;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class MyDebugTree extends Timber.DebugTree {
    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        super.log(priority, "Warsart: " + tag, message, t);
    }
}

package com.absurd.rick.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangwenwei on 17/6/23.
 */
public class Global {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());


}

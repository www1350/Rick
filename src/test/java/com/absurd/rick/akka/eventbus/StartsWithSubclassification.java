package com.absurd.rick.akka.eventbus;

import akka.util.Subclassification;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class StartsWithSubclassification implements Subclassification<String> {
    @Override public boolean isEqual(String x, String y) {
        return x.equals(y);
    }

    @Override public boolean isSubclass(String x, String y) {
        return x.startsWith(y);
    }
}
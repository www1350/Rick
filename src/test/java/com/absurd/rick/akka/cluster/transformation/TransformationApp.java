package com.absurd.rick.akka.cluster.transformation;

/**
 * Created by wangwenwei on 2017/10/22.
 */
public class TransformationApp {
    public static void main(String[] args) {
        // starting 2 frontend nodes and 3 backend nodes
        TransformationBackendMain.main(new String[] { "2551" });
        TransformationBackendMain.main(new String[] { "2552" });
        TransformationBackendMain.main(new String[0]);
        TransformationFrontendMain.main(new String[0]);
    }
}

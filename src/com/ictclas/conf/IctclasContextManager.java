package com.ictclas.conf;

/**
 * Created by Lanxiaowei
 * IctclasContext管理器
 */
public class IctclasContextManager {
    public static IctclasContext getContext() {
        return IctclasContext.getInstance();
    }
}

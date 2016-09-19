package com.ictclas.conf;

import com.ictclas.core.OSInfo;

import com.ictclas.conf.DefaultConfiguration;

import static com.ictclas.conf.DefaultConfiguration.DLL_FILE_NAME;

/**
 * Created by Lanxiaowei
 */
public class IctclasContext {
    private Configuration config;

    private IctclasContext() {
        this.config = DefaultConfiguration.getInstance();
    }

    private static class SingletonHolder {
        private static final IctclasContext INSTANCE = new IctclasContext();
    }

    public static final IctclasContext getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String getDllPath() {
        if(null == this.config) {
            return null;
        }
        return this.config.dllPath();
    }

    public void setDllPath(String dllPath) {
        if(null == this.config) {
            return;
        }
        this.config.setDllPath(dllPath);
    }

    public String getUserDic() {
        if(null == this.config) {
            return OSInfo.getModulePath(DefaultConfiguration.DLL_FILE_NAME);
        }
        return this.config.userDicPath();
    }

    public String getStopWordPath() {
        if(null == this.config) {
            return null;
        }
        return this.config.stopwordPath();
    }

    public boolean addSpeech() {
        if(null == this.config) {
            return false;
        }
        return this.config.addSpeech();
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }
}

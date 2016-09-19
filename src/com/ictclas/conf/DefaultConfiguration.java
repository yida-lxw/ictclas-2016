package com.ictclas.conf;

import com.ictclas.core.OSInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Created by Lanxiaowei
 * Ictclas的默认配置管理器
 */
public class DefaultConfiguration implements Configuration {
    private static final String PROPERTIES_FILE_NAME = "ictclas.properties";
    public static final String DLL_FILE_NAME = "NLPIR";

    private Properties props;

    private DefaultConfiguration() {
        if(null != this.props) {
            return;
        }
        this.props = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if(null != is) {
            try {
                this.props.load(is);
            } catch (InvalidPropertiesFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Configuration getInstance(){
        return new DefaultConfiguration();
    }


    /**
     * 返回Ictclas分词器初始化时需要加载的NLPIR.dll文件的加载路径
     * @return
     */
    public String dllPath() {
        if(null == this.props) {
            return OSInfo.getModulePath(DLL_FILE_NAME);
        }
        String dllPath = this.props.getProperty("dll_path");
        if(null == dllPath || "".equals(dllPath)) {
            dllPath = OSInfo.getModulePath(DLL_FILE_NAME);
        }
        return dllPath;
    }

    /**
     * 返回停用词字典文件的加载路径
     * @return
     */
    public String stopwordPath() {
        return props.getProperty("stopword_path");
    }

    /**
     * 返回用户自定义扩展字典文件的加载路径
     * @return
     */
    public String userDicPath() {
        return props.getProperty("userdic_path");
    }

    /**
     * 是否添加词性
     * @return
     */
    public boolean addSpeech() {
        String addSpeech = props.getProperty("add_speech");
        if(null == addSpeech || !"true".equalsIgnoreCase(addSpeech)) {
            return false;
        }
        return true;
    }
}

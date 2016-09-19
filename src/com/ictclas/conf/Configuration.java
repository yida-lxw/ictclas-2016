package com.ictclas.conf;

/**
 * Created by Lanxiaowei
 * Ictclas配置管理接口
 */
public interface Configuration {

    /**
     * 返回Ictclas分词器初始化时需要加载的NLPIR.dll文件的加载路径
     * @return
     */
    public String dllPath();

    /**
     * 返回停用词字典文件的加载路径
     * @return
     */
    public String stopwordPath();

    /**
     * 返回用户自定义扩展字典文件的加载路径
     * @return
     */
    public String userDicPath();

    /**
     * 是否添加词性
     * @return
     */
    public boolean addSpeech();
}

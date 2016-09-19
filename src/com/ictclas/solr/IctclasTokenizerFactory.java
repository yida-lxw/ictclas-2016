package com.ictclas.solr;

import com.ictclas.analysis.tokenizer.IctclasTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Created by Lanxiaowei
 * IctclasTokenizer的工厂类，兼容Solr5.x
 */
public class IctclasTokenizerFactory extends TokenizerFactory {
    /** 是否添加词性*/
    private Boolean addSpeech;
    /**停用词字典文件加载路径*/
    private String stopwordsDir;
    /**用户扩展字典文件加载路径*/
    private String userDic;
    /**NLPIR.dll文件的加载路径，可以是绝对路径*/
    private String dllPath;

    public IctclasTokenizerFactory(Map<String, String> args) {
        super(args);
        this.stopwordsDir = get(args, "stopwords");
        this.userDic = get(args, "userDic");
        this.dllPath = get(args, "dllPath");
        Object obj = get(args, "addSpeech","");
        if(null == obj || "".equals(obj.toString())) {
            this.addSpeech = null;
        } else {
            this.addSpeech = Boolean.valueOf(obj.toString());
        }
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new IctclasTokenizer(attributeFactory,this.addSpeech,
                this.stopwordsDir,this.userDic,this.dllPath);
    }
}

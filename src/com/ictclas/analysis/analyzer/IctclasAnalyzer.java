package com.ictclas.analysis.analyzer;

import com.ictclas.analysis.tokenizer.IctclasTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Created by Lanxiaowei
 * Ictclas Analyzer 兼容Lucene5.x
 */
public class IctclasAnalyzer extends Analyzer {
    /** 是否添加词性*/
    private boolean addSpeech;
    /**自定义停用词*/
    private Set<String> filter;
    /**停用词字典文件加载路径*/
    private String stopwordsDir;
    /**用户扩展字典文件加载路径*/
    private String userDic;

    private String text;

    public IctclasAnalyzer(boolean addSpeech, String stopwordsDir,String userDic) {
        this.addSpeech = addSpeech;
        this.stopwordsDir = stopwordsDir;
        this.userDic = userDic;
    }

    public IctclasAnalyzer(boolean addSpeech,Set<String> filter,String userDic) {
        this.addSpeech = addSpeech;
        this.filter = filter;
        this.userDic = userDic;
    }

    public IctclasAnalyzer(boolean addSpeech,String userDic) {
        this(addSpeech,"",userDic);
    }

    public IctclasAnalyzer(String userDic) {
        this(true,userDic);
    }

    public IctclasAnalyzer() {
        this(true,null);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        Reader rd = super.initReader(fieldName, reader);
        this.text = read(rd);
        return rd;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = null;
        if(null == stopwordsDir || "".equals(stopwordsDir)) {
            tokenizer = new IctclasTokenizer(this.addSpeech,this.filter,this.userDic)
                    .setText(this.text);

        } else {
            tokenizer = new IctclasTokenizer(this.addSpeech,this.stopwordsDir,this.userDic)
                    .setText(this.text);
        }
        return new TokenStreamComponents(tokenizer);
    }

    private String read(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String line = null;
        try {
            while((line = bufferedReader.readLine())!=null) {
                buffer.append(line);
                buffer.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}

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
    /**NLPIR.dll文件的加载路径*/
    private static String dllPath;

    /**自定义停用词*/
    private Set<String> filter;
    /**停用词字典文件加载路径*/
    private String stopwordsDir;

    private String text;

    public IctclasAnalyzer(String dllPath, boolean addSpeech, String stopwordsDir) {
        this.dllPath = dllPath;
        this.addSpeech = addSpeech;
        this.stopwordsDir = stopwordsDir;
    }

    public IctclasAnalyzer(String dllPath,boolean addSpeech,Set<String> filter) {
        this.dllPath = dllPath;
        this.addSpeech = addSpeech;
        this.filter = filter;
    }

    public IctclasAnalyzer(String dllPath,boolean addSpeech) {
        this(dllPath,addSpeech,"");
    }

    public IctclasAnalyzer(String dllPath) {
        this(dllPath,true);
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
            tokenizer = new IctclasTokenizer(this.dllPath,this.addSpeech,this.filter)
                    .setText(this.text);

        } else {
            tokenizer = new IctclasTokenizer(this.dllPath,this.addSpeech,this.stopwordsDir)
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

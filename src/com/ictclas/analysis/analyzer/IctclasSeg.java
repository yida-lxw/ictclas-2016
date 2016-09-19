package com.ictclas.analysis.analyzer;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;

/**
 * Created by Lanxiaowei
 */
public class IctclasSeg {
    /** 是否添加词性*/
    private boolean addSpeech;
    /**原始文本*/
    private String text;

    private TokenIterator iterator;

    static {
        initDll();
    }

    public IctclasSeg(String text,boolean addSpeech) {
        this.text = text;
        this.addSpeech = addSpeech;
        this.iterator = iterator();
    }

    public Word readNext() {
        if(this.iterator.hasNext()) {
            return this.iterator.next();
        }
        return null;
    }

    private static void initDll() {
        String argu = "";
        //UTF-8:1 GBK:0，BIG5:2 含繁体字的GBK:3
        String system_charset = "UTF-8";
        int charset_type = 1;
        int init_flag = 0;
        try {
            init_flag = IctclasAnalyzer.CLibrary.Instance.NLPIR_Init(argu
                    .getBytes(system_charset), charset_type, "0"
                    .getBytes(system_charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("NLPIR dll init fail.");
        }
        if (0 == init_flag) {
            throw new RuntimeException("NLPIR dll init fail.");
        }
    }

    private String analysis(String text,boolean addSpeech) {
        return IctclasAnalyzer.CLibrary.Instance
                .NLPIR_ParagraphProcess(text, addSpeech? 1 : 0);
    }

    private TokenIterator iterator() {
        String text = analysis(this.text,this.addSpeech);
        if(null == text || "".equals(text)) {
            return null;
        }
        this.text = text;
        replaceWhiteSpace();
        return new TokenIterator(this.text.split(" "));
    }

    private void replaceWhiteSpace() {
        for(int i=2; i <= 101; i++) {
            if(i <= 3) {
                this.text = this.text.replaceAll(padSpace(i),padSpace(i - 1));
            } else {
                this.text = this.text.replaceAll(padSpace(i),padSpace(i - 2));
            }
        }
        this.text = this.text.replace("\r\n","");
        this.text = this.text.replace("\r","").replace("\n","");
    }

    public String padSpace(int count) {
        String s = "";
        for(int i=0; i < count; i++) {
            s += " ";
        }
        return s;
    }

    public TokenIterator getIterator() {
        return iterator;
    }
}

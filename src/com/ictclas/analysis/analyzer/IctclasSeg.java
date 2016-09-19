package com.ictclas.analysis.analyzer;

import com.ictclas.conf.IctclasContext;
import com.ictclas.conf.IctclasContextManager;
import com.ictclas.core.NlpirMethod;

/**
 * Created by Lanxiaowei
 */
public class IctclasSeg {
    /** 是否添加词性*/
    private Boolean addSpeech;
    /**原始文本*/
    private String text;

    private TokenIterator iterator;

    private IctclasContext context;

    public IctclasSeg(String text,Boolean addSpeech) {
        this.text = text;
        this.addSpeech = addSpeech;
        this.iterator = iterator();
        this.context = IctclasContextManager.getContext();
    }

    public Word readNext() {
        if(this.iterator.hasNext()) {
            return this.iterator.next();
        }
        return null;
    }

    private String analysis(String text,boolean addSpeech) {
        return NlpirMethod.NLPIR_ParagraphProcess(text, addSpeech? 1 : 0);
    }

    private TokenIterator iterator() {
        String text = analysis(this.text,isAddSpeech());
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

    public Boolean isAddSpeech() {
        if(null == this.context) {
            return (this.addSpeech == null) ? false : this.addSpeech;
        }
        if(null == this.addSpeech) {
            return this.context.addSpeech();
        }
        return this.addSpeech;
    }
}

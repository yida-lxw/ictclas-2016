package com.ictclas.analysis.analyzer;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Lanxiaowei
 * Token遍历器
 */
public class TokenIterator implements Iterator<Word> {
    private String[] items;
    /**当前位置索引*/
    int cursor;
    /**上一次返回的item的索引*/
    int lastRet = -1;

    public TokenIterator(String[] items) {
        this.items = items;
        reset();
    }

    public boolean hasNext() {
        return cursor != items.length;
    }

    public void reset() {
        this.cursor = 0;
        this.lastRet = -1;
    }

    public Word next() {
        int i = cursor;
        if (i >= items.length) {
            throw new NoSuchElementException();
        }
        cursor = i + 1;
        String str = items[lastRet = i];
        if(null == str) {
            return null;
        }
        Word word = null;
        if((str.startsWith("/") && str.indexOf("/") == str.lastIndexOf("/")) ||
                -1 == str.indexOf("/") || "".equals(str)) {
            if("".equals(str)) {
                str = " ";
            } else if(str.indexOf(" ") != -1) {
                str += " ";
            }
            int len = str.length();
            int start = start(str,i+1);
            word = new Word(str,"word",i+1,start,len);
        } else {
            String text = str.substring(0,str.lastIndexOf("/"));
            String type = str.substring(str.lastIndexOf("/") + 1);
            int len = text.length();
            int start = start(text,i+1);
            word = new Word(text,type,i+1,start,len);
        }
        return word;
    }

    public void remove() {
        throw new UnsupportedOperationException("Unsupported Operation[remove] for TokenIterator");
    }

    private int start(String target,int position) {
        if(null == this.items || this.items.length <= 0) {
            return -1;
        }
        int len = this.items.length;
        int total = 0;
        int index = 0;
        for(int i=0; i < len; i++) {
            String it = items[i];
            if("".equals(it)){
               it = " ";
            }
            String text = null;
            if((it.startsWith("/") && it.indexOf("/") == it.lastIndexOf("/")) ||
                    -1 == it.indexOf("/") || " ".equals(it)) {
                text = it;
            } else {
                text = it.substring(0,it.indexOf("/"));
            }
            if((i+1) == position && target.equals(text)) {
                index = i;
                break;
            }
        }
        for(int j=0; j < index; j++) {
            String it = items[j];
            if("".equals(it)){
                it = " ";
            }
            int length = 0;
            if((it.startsWith("/") && it.indexOf("/") == it.lastIndexOf("/")) ||
                    -1 == it.indexOf("/")) {
                length = it.length();
            } else {
                length = it.substring(0,it.lastIndexOf("/")).length();
            }
            total += length;
        }
        return total;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getLastRet() {
        return lastRet;
    }

    public void setLastRet(int lastRet) {
        this.lastRet = lastRet;
    }
}

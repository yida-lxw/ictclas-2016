package com.ictclas.analysis.tokenizer;

import com.ictclas.analysis.analyzer.IctclasSeg;
import com.ictclas.analysis.analyzer.Word;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lanxiaowei
 * Ictclas Tokenizer 兼容Lucene5.x
 */
public class IctclasTokenizer extends Tokenizer {
    /** 是否添加词性*/
    private boolean addSpeech;

    private IctclasSeg seg;

    private CharTermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private PositionIncrementAttribute positionAtt;
    private TypeAttribute typeAtt;
    /**自定义停用词*/
    private Set<String> filter;
    /**停用词字典文件加载路径*/
    private String stopwordsDir;

    private String text;

    public IctclasTokenizer(AttributeFactory factory, boolean addSpeech,String stopwordsDir) {
        super(factory);
        this.addSpeech = addSpeech;
        //this.seg = new IctclasSeg(this.text,this.dllPath,this.addSpeech);
        termAtt = addAttribute(CharTermAttribute.class);
        offsetAtt = addAttribute(OffsetAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        positionAtt = addAttribute(PositionIncrementAttribute.class);
        this.stopwordsDir = stopwordsDir;
        addStopwords(stopwordsDir);
    }

    public IctclasTokenizer(AttributeFactory factory, boolean addSpeech,Set<String> filter) {
        super(factory);
        this.addSpeech = addSpeech;
        //this.seg = new IctclasSeg(this.text,this.dllPath,this.addSpeech);
        termAtt = addAttribute(CharTermAttribute.class);
        offsetAtt = addAttribute(OffsetAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        positionAtt = addAttribute(PositionIncrementAttribute.class);
        this.filter = filter;
    }

    public IctclasTokenizer(AttributeFactory factory,boolean addSpeech) {
        this(factory,addSpeech,"");
    }

    public IctclasTokenizer(boolean addSpeech,Set<String> filter) {
        this.addSpeech = addSpeech;
        //this.seg = new IctclasSeg(this.text,this.dllPath,this.addSpeech);
        termAtt = addAttribute(CharTermAttribute.class);
        offsetAtt = addAttribute(OffsetAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        positionAtt = addAttribute(PositionIncrementAttribute.class);
        this.filter = filter;
    }

    public IctclasTokenizer(boolean addSpeech,String stopwordsDir) {
        this.addSpeech = addSpeech;
        termAtt = addAttribute(CharTermAttribute.class);
        offsetAtt = addAttribute(OffsetAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        positionAtt = addAttribute(PositionIncrementAttribute.class);
        this.stopwordsDir = stopwordsDir;
        addStopwords(stopwordsDir);
    }

    public IctclasTokenizer(boolean addSpeech) {
        this(addSpeech,"");
    }

    public IctclasTokenizer() {
        this(true,"");
    }

    public boolean incrementToken() throws IOException {
        clearAttributes();
        int position = 0;
        Word word = null;
        String wordStr = null;
        boolean flag = true;
        do {
            word = seg.readNext();
            position++;
            if(null == word) {
                break;
            }
            wordStr = word.getText();
            if (filter != null && filter.contains(wordStr)) {
                continue;
            } else {
                flag = false;
            }
        } while(flag);
        if(null != wordStr && !"".equals(wordStr)) {
            positionAtt.setPositionIncrement(position);
            termAtt.setEmpty().append(word.getText());
            offsetAtt.setOffset(word.getStart(), word.getStart() + word.getLength());
            typeAtt.setType(word.getType());
            return true;
        }
        end();
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        seg.getIterator().reset();
    }

    public IctclasTokenizer setText(String text) {
        this.text = text;
        this.seg = new IctclasSeg(this.text,this.addSpeech);
        return this;
    }

    /**
     * 添加停用词
     * @param dir
     */
    private void addStopwords(String dir) {
        if (dir == null || "".equals(dir)) {
            return;
        }
        this.filter = new HashSet<String>();
        InputStreamReader reader;
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(dir);
            if(null == is) {
                is = new FileInputStream(dir);

            }
            try {
                is = new FileInputStream(dir);
                reader = new InputStreamReader(is,"UTF-8");
                BufferedReader br = new BufferedReader(reader);
                String word = br.readLine();
                while (word != null) {
                    this.filter.add(word);
                    word = br.readLine();
                }
            } catch (FileNotFoundException e1) {
                throw new RuntimeException("No custom stopword file found");
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException("stopword.dic encoding Unsupported");
            } catch (IOException e1) {
                throw new RuntimeException("read stopword.dic occur IO Exception");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No custom stopword file found");
        } catch (IOException e) {
            throw new RuntimeException("Custom stopword file io exception");
        }
    }
}

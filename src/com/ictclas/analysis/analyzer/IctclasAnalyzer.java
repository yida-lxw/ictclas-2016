package com.ictclas.analysis.analyzer;

import com.ictclas.analysis.tokenizer.IctclasTokenizer;
import com.sun.jna.Library;
import com.sun.jna.Native;
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
            tokenizer = new IctclasTokenizer(this.addSpeech,this.filter)
                    .setText(this.text);

        } else {
            tokenizer = new IctclasTokenizer(this.addSpeech,this.stopwordsDir)
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

    // 定义接口CLibrary，继承自com.sun.jna.Library
    public interface CLibrary extends Library {
        // 定义并初始化接口的静态变量
        CLibrary Instance = (CLibrary) Native.loadLibrary(
                dllPath + "NLPIR", CLibrary.class);

        // 初始化函数声明：sDataPath是初始化路径地址，
        // 包括核心词库和配置文件的路径，encoding为输入字符的编码格式
        public int NLPIR_Init(byte[] sDataPath, int encoding,
                              byte[] sLicenceCode);
        // 分词函数声明：sSrc为待分字符串，bPOSTagged=0表示不进行词性标注，
        // bPOSTagged=1表示进行词性标注
        public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

        // 添加用户词汇,如果添加成功，返回值为1，否则返回值为0
        public int NLPIR_AddUserWord(String sWord);
        // 删除用户词汇
        public int NLPIR_DelUsrWord(String sWord);
        // 保存用户词汇到用户词典
        public int NLPIR_SaveTheUsrDic();
        // 导入用户自定义词典：自定义词典路径，bOverwrite=true表示替代当前的自定义词典，false表示添加到当前自定义词典后
        public int NLPIR_ImportUserDict(String sFilename, boolean bOverwrite);

        //获取关键词
        public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
                                        boolean bWeightOut);
        public void NLPIR_Exit();
    }
}

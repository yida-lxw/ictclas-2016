package com.ictclas.analysis.analyzer;

import code.NlpirTest;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;

/**
 * Created by Lanxiaowei
 */
public class IctclasSeg {
    /** 是否添加词性*/
    private boolean addSpeech;
    /**NLPIR.dll文件的加载路径*/
    private static String dllPath;
    /**原始文本*/
    private String text;

    private TokenIterator iterator;

    static {
        initDll();
    }

    public IctclasSeg(String text, String dllPath, boolean addSpeech) {
        this.text = text;
        this.dllPath = dllPath;
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
            init_flag = NlpirTest.CLibrary.Instance.NLPIR_Init(argu
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
        return NlpirTest.CLibrary.Instance
                .NLPIR_ParagraphProcess(text, addSpeech? 1 : 0);
    }

    private TokenIterator iterator() {
        String text = analysis(this.text,this.addSpeech);
        if(null == text || "".equals(text)) {
            return null;
        }
        this.text = text;
        return new TokenIterator(text.split(" "));
    }

    // 定义接口CLibrary，继承自com.sun.jna.Library
    public interface CLibrary extends Library {
        // 定义并初始化接口的静态变量
        NlpirTest.CLibrary Instance = (NlpirTest.CLibrary) Native.loadLibrary(
                dllPath + "NLPIR", NlpirTest.CLibrary.class);

        // 初始化函数声明：sDataPath是初始化路径地址，
        // 包括核心词库和配置文件的路径，encoding为输入字符的编码格式
        public int NLPIR_Init(byte[] sDataPath, int encoding,
                              byte[] sLicenceCode);
        // 分词函数声明：sSrc为待分字符串，bPOSTagged=0表示不进行词性标注，
        // bPOSTagged=1表示进行词性标注
        public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

        //获取关键词
        public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
                                        boolean bWeightOut);
        public void NLPIR_Exit();
    }

    public TokenIterator getIterator() {
        return iterator;
    }
}

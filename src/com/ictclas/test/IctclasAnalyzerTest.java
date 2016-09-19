package com.ictclas.test;

import com.ictclas.analysis.analyzer.IctclasAnalyzer;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;

import static com.ictclas.test.AnalyzerUtils.displayTokens;

/**
 * Created by Lanxiaowei
 */
public class IctclasAnalyzerTest {
    public static void main(String[] args) throws IOException {
        //待分词的文本
        String text = "《大话3》被吐槽：情怀很珍贵 请勿滥消费么么哒";
        //停用词字典文件加载路径
        String stopwordPath = "E:/apache-tomcat-7.0.55/webapps/solr/WEB-INF/classes/stopword.dic";
        //用户自定义扩展字典文件加载路径
        String userDicPath = "dict/user_dict.txt";
        Analyzer analyzer = new IctclasAnalyzer(false,stopwordPath,userDicPath);
        displayTokens(analyzer, text);
    }
}

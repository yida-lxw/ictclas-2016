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
        String text = "《大话3》被吐槽：情怀很珍贵 请勿滥消费";
        String dllPath = "E:/git-space/ictclas-2016/Data/";
        Analyzer analyzer = new IctclasAnalyzer(dllPath);
        displayTokens(analyzer, text);
    }
}

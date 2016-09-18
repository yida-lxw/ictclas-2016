package com.ictclas.test;

import com.ictclas.analysis.analyzer.IctclasAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

/**
 * Created by Lanxiaowei
 */
public class AnalyzerUtils {
    public static void main(String[] args) throws IOException {
        String text = "《大话3》被吐槽：情怀很珍贵 请勿滥消费";
        String dllPath = "";
        Analyzer analyzer = new IctclasAnalyzer(dllPath);
        displayTokens(analyzer, text);
    }
    public static void displayTokens(Analyzer analyzer,String text) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("text", text);
        displayTokens(tokenStream);
    }

    public static void displayTokens(TokenStream tokenStream) throws IOException {
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);

        tokenStream.reset();
        int position = 0;
        while (tokenStream.incrementToken()) {
            int increment = positionIncrementAttribute.getPositionIncrement();
            if(increment > 0) {
                position = position + increment;
                System.out.print(position + ":");
            }
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = charTermAttribute.toString();
            System.out.println("[" + term + "]" + ":(" + startOffset + "-->" + endOffset + "):" + typeAttribute.type());
        }
        tokenStream.close();
    }
}

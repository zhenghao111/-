package com.zhenghao.test;

import com.zhenghao.segmentor.JSegmentor;
import com.zhenghao.type.JStringVector;
import com.zhenghao.util.JSentenceReader;
import com.zhenghao.util.JSentenceWriter;

/**
 * Created by zhenghao on 16/5/12.
 */
public class SegmentorTest {

    public static void main(String[] args) {
        //执行测试时的参数设置
        System.out.println("模型文件:"+args[0]);
        System.out.println("测试文件:"+args[1]);
        System.out.println("输出文件:"+args[2]);

        System.out.println("测试分词开始 ...");
        long startTest = System.currentTimeMillis();

        //执行测试
        test(args[0], args[1], args[2]);

        long endTest = System.currentTimeMillis();
        System.out.println("测试分词结束...,耗时"+(endTest-startTest)/1000+"秒");
    }

    private static void test(String modelFile, String inputFile, String outputFille) {

        JSegmentor segmentor = new JSegmentor(modelFile, false);
        JSentenceReader reader = new JSentenceReader(inputFile);
        JSentenceWriter writer = new JSentenceWriter(outputFille);
        JStringVector inputSentence;
        JStringVector outputSentence;
        int count = 0;

        while ((inputSentence=reader.readRawSentence()) != null) {
            count++;
            outputSentence = segmentor.segment(inputSentence);
            writer.writeSegmentedSentence(outputSentence);
        }
        System.out.println("完成测试句子:"+count);

        reader.close();
        writer.close();
    }
}

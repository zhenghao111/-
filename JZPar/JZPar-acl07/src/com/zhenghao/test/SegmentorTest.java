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
        //ִ�в���ʱ�Ĳ�������
        System.out.println("ģ���ļ�:"+args[0]);
        System.out.println("�����ļ�:"+args[1]);
        System.out.println("����ļ�:"+args[2]);

        System.out.println("���Էִʿ�ʼ ...");
        long startTest = System.currentTimeMillis();

        //ִ�в���
        test(args[0], args[1], args[2]);

        long endTest = System.currentTimeMillis();
        System.out.println("���Էִʽ���...,��ʱ"+(endTest-startTest)/1000+"��");
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
        System.out.println("��ɲ��Ծ���:"+count);

        reader.close();
        writer.close();
    }
}

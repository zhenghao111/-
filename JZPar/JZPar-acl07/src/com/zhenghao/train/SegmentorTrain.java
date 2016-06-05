package com.zhenghao.train;

import com.zhenghao.segmentor.JSegmentor;
import com.zhenghao.type.JStringVector;
import com.zhenghao.util.JSentenceReader;
import com.zhenghao.util.JSentenceUtil;

/**
 * Created by zhenghao on 16/5/12.
 */
public class SegmentorTrain {

    /**
     *
     * @param args:ѵ���ļ� ģ���ļ� ��������
     */
    public static void main(String[] args) {

        //ִ��ѵ��ʱ�Ĳ�������
        System.out.println("ѵ���ļ�:"+args[0]);
        System.out.println("ģ���ļ�:"+args[1]);
        System.out.println("��������:"+args[2]);

        //��������
        int training_rounds = Integer.parseInt(args[2]);

        System.out.println("ѵ����ʼ...");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < training_rounds; i++) {
            train(args[0], args[1]);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("ѵ������...,��ʱ"+(endTime-startTime)/1000+"��");

    }

    /**
     * ִ��1��ѵ��
     * @param trainFile ѵ���ļ�
     * @param modelFile ģ���ļ�
     */
    private static void train(String trainFile, String modelFile) {

        JSegmentor segmentor = new JSegmentor(modelFile, true);

        JSentenceReader reader = new JSentenceReader(trainFile);
        JStringVector inputSentence;
        JStringVector refSentence;
        JStringVector outputSentence;

        int count = 0;
        int errorCount = 0;

        while ((refSentence = reader.readSegmentedSentence()) != null) {
            //��ȡ��������
            if (refSentence.size() == 0)
                continue;
            inputSentence = JSentenceUtil.DesegmentSentence(refSentence);
            count++;

            //TODO:���Էִʿ�ʼ
            System.out.println("count:" + count+" input:"+inputSentence.toString());
            long startSegment = System.currentTimeMillis();

            outputSentence = segmentor.segment(inputSentence);
            if (outputSentence.size() == 0)
                continue;

            //TODO:���Էִʽ���
            long endSegment = System.currentTimeMillis();
            System.out.println("segment:" + (endSegment-startSegment)+" output:"+outputSentence.toString());

            //TODO:���Ը��²���������ʼ
            long startUpdateScores = System.currentTimeMillis();

            segmentor.updateScores(outputSentence, refSentence, count);

            //TODO:���Ը��²�����������
            long endUpdateScores = System.currentTimeMillis();
            System.out.println("updateScores:" + (endUpdateScores-startUpdateScores));

            if (!outputSentence.equals(refSentence)) {
                errorCount++;
            }
        }

        //TODO:�������ѵ����ʼ
        long startFinish = System.currentTimeMillis();

        segmentor.finishTraining(count);

        //TODO:�������ѵ������
        long endFinish = System.currentTimeMillis();
        System.out.println("finishTraining:" + (endFinish-startFinish)/1000+ "��");

        System.out.println("Done.�ܹ�����:"+errorCount);

        reader.close();

        //TODO: delete segment ��Ҳ�е���FeatureHandle.saveScores
    }
}

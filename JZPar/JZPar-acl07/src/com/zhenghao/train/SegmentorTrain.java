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
     * @param args:训练文件 模型文件 迭代次数
     */
    public static void main(String[] args) {

        //执行训练时的参数设置
        System.out.println("训练文件:"+args[0]);
        System.out.println("模型文件:"+args[1]);
        System.out.println("迭代次数:"+args[2]);

        //迭代次数
        int training_rounds = Integer.parseInt(args[2]);

        System.out.println("训练开始...");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < training_rounds; i++) {
            train(args[0], args[1]);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("训练结束...,耗时"+(endTime-startTime)/1000+"秒");

    }

    /**
     * 执行1次训练
     * @param trainFile 训练文件
     * @param modelFile 模型文件
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
            //读取空行跳过
            if (refSentence.size() == 0)
                continue;
            inputSentence = JSentenceUtil.DesegmentSentence(refSentence);
            count++;

            //TODO:调试分词开始
            System.out.println("count:" + count+" input:"+inputSentence.toString());
            long startSegment = System.currentTimeMillis();

            outputSentence = segmentor.segment(inputSentence);
            if (outputSentence.size() == 0)
                continue;

            //TODO:调试分词结束
            long endSegment = System.currentTimeMillis();
            System.out.println("segment:" + (endSegment-startSegment)+" output:"+outputSentence.toString());

            //TODO:调试更新参数向量开始
            long startUpdateScores = System.currentTimeMillis();

            segmentor.updateScores(outputSentence, refSentence, count);

            //TODO:调试更新参数向量结束
            long endUpdateScores = System.currentTimeMillis();
            System.out.println("updateScores:" + (endUpdateScores-startUpdateScores));

            if (!outputSentence.equals(refSentence)) {
                errorCount++;
            }
        }

        //TODO:调试完成训练开始
        long startFinish = System.currentTimeMillis();

        segmentor.finishTraining(count);

        //TODO:调试完成训练结束
        long endFinish = System.currentTimeMillis();
        System.out.println("finishTraining:" + (endFinish-startFinish)/1000+ "秒");

        System.out.println("Done.总共错误:"+errorCount);

        reader.close();

        //TODO: delete segment 中也有调用FeatureHandle.saveScores
    }
}

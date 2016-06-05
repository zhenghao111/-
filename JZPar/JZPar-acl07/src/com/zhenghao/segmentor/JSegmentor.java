package com.zhenghao.segmentor;

import com.zhenghao.conf.JConfigure;
import com.zhenghao.segmentor.beamsearch.JStateItem;
import com.zhenghao.type.JStringVector;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by zhenghao on 16/5/12.
 */
public class JSegmentor {

    private boolean isTrain;
    private JFeatureHandle featureHandle;
    private String[] wordCache;
    //定义优先级队列的比较器
    //比较器more:分数从高到低
    Comparator<JStateItem> more = new Comparator<JStateItem>() {
        @Override
        public int compare(JStateItem o1, JStateItem o2) {
            int score1 = o1.getScore();
            int score2 = o2.getScore();
            return (int)(score2-score1);
        }
    };
    //比较器less:分数从低到高
    Comparator<JStateItem> less = new Comparator<JStateItem>() {
        @Override
        public int compare(JStateItem o1, JStateItem o2) {
            int score1 = o1.getScore();
            int score2 = o2.getScore();
            return (int)(score1-score2);
        }
    };

    public JSegmentor(String modelFile, boolean isTrain) {
        featureHandle = new JFeatureHandle(this, modelFile, isTrain);
        wordCache = new String[JConfigure.MAX_SENTENCE_SIZE * JConfigure.MAX_SENTENCE_SIZE];
    }

    private JStringVector outputSentence = new JStringVector();
    Queue<JStateItem> src = new PriorityQueue<>(JConfigure.BEAM_SIZE, more);
    Queue<JStateItem> tgt = new PriorityQueue<>(JConfigure.BEAM_SIZE, less);
    JStateItem firstItem = new JStateItem();
    public JStringVector segment(JStringVector inputSentence) {
        outputSentence.clear();
        src.clear();
        tgt.clear();
        clearWordCache();

        if (inputSentence.size() > JConfigure.MAX_SENTENCE_SIZE) {
            System.out.println("句子的字数超过"+JConfigure.MAX_SENTENCE_SIZE);
            return outputSentence;
        }

        src.add(firstItem);
        for (int char_index = 0; char_index < inputSentence.size(); char_index++) {

            int src_size = src.size();
            for (int i=0; (i < src_size)&&(i < JConfigure.BEAM_SIZE); i++){
                JStateItem item = src.poll();

                //1. 添加新词
                JStateItem item1 = item.copy();
                item1.append(char_index);

                item1.setScore(item1.getScore()+featureHandle.getLocalScore(inputSentence, item1, item1.getLength()-1));
                //添加到tgt中
                tgt.add(item1);

                //2. 追加到最后一个词后面
                if(char_index > 0) {
                    JStateItem item2 = item.copy();
                    int subtract_score = featureHandle.getLocalScore(inputSentence, item2, item2.getLength()-1);
                    item2.setScore(item2.getScore()-subtract_score);
                    item2.replace(char_index);
                    item2.setScore(item2.getScore()+featureHandle.getLocalScore(inputSentence, item2, item2.getLength()-1));
                    //添加到tgt中
                    tgt.add(item2);
                }
            }
            //src=tgt,tgt清空
            src.clear();
            int tgt_size = tgt.size();
            while (tgt_size > JConfigure.BEAM_SIZE) {
                tgt.poll();
                tgt_size--;
            }
            for (int j = 0; (j < tgt_size)&&(j < JConfigure.BEAM_SIZE); j++) {
                src.add(tgt.poll());
            }
            tgt.clear();
        }

        JStateItem best = src.poll();

        //产生最优切分
        for (int i = 0; i < best.getLength(); i++) {
            String temp = "";

            for (int j=best.getWordStart(i); j<=best.getWordEnd(i); j++ ) {
                temp += inputSentence.get(j);
            }

            outputSentence.add(temp);
        }
        return outputSentence;
    }

    public String findWordFromCache(int start, int length, JStringVector sentence) {
        if (wordCache[start*JConfigure.MAX_SENTENCE_SIZE+length-1]==null) {
            String temp = "";
            for (int i = start; i < start+length; i++) {
                temp += sentence.get(i);
            }
            wordCache[start*JConfigure.MAX_SENTENCE_SIZE+length-1] = temp;
        }
        return wordCache[start*JConfigure.MAX_SENTENCE_SIZE+length-1];
    }

    public void updateScores(JStringVector output, JStringVector correct, int round) {
        featureHandle.updateScoreVector(output, correct, round);
    }

    public void finishTraining(int round) {
        featureHandle.computerAverageFeatureWeights(round);
        featureHandle.saveScores();
    }

    private void clearWordCache() {
        for (int i = 0; i < JConfigure.MAX_SENTENCE_SIZE; i++) {
            for (int j = 0; j < JConfigure.MAX_SENTENCE_SIZE; j++) {
                wordCache[i*JConfigure.MAX_SENTENCE_SIZE+j] = null;
            }
        }
    }
}

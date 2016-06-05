package com.zhenghao.segmentor;

import com.zhenghao.conf.JConfigure;
import com.zhenghao.segmentor.beamsearch.JStateItem;
import com.zhenghao.type.JStringVector;
import com.zhenghao.type.featuretype.JTwoWords;
import com.zhenghao.type.featuretype.JWord;
import com.zhenghao.type.featuretype.JWordInt;
import com.zhenghao.weight.JScore;
import com.zhenghao.weight.JScoreMap;
import com.zhenghao.weight.JWeight;
import java.io.*;
import java.util.List;

/**
 * Created by zhenghao on 16/5/12.
 */
public class JFeatureHandle
{
    private JSegmentor parent;
    private String modelFile;
    private boolean isTrain;
    private JWeight weights;
    private JScore zeroScore;
    private boolean isScoreModified;
    private JWord emptyWord = new JWord("");

    public JFeatureHandle(JSegmentor segmentor, String modelFile, boolean isTrain) {
        this.parent = segmentor;
        this.modelFile = modelFile;
        this.isTrain = isTrain;
        zeroScore = new JScore();
        weights = new JWeight();

        loadScores();
    }

    public JWeight getWeights() {
        return weights;
    }

    //加载模型文件
    private void loadScores() {

        long startTime = System.currentTimeMillis();
        System.out.println("Loading model ...");

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(modelFile), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            //模型文件不存在
            System.out.println("empty.");
            List<JScoreMap> featureTemplateList = weights.getList();
            for (JScoreMap featureTemplate : featureTemplateList) {
                featureTemplate.init();
            }
            return;
        }
        //模型文件存在,读取特征向量和参数向量
        BufferedReader reader = new BufferedReader(inputStreamReader);
        List<JScoreMap> featureTemplateList = weights.getList();
        String titleAndCount;
        for (JScoreMap featureTemplate : featureTemplateList) {

            try {
                //读取特征模版名称 特征数量
                if ((titleAndCount=reader.readLine()) != null) {
                    //System.out.println(titleAndCount);
                    String[] str = titleAndCount.split(" ");
                    if (str.length > 0) {
                        if (!str[0].equals(featureTemplate.getName())) {
                            System.out.println(str[0]+"和"+featureTemplate.getName()+"的特征模版名称不匹配");
                            return;
                        }
                    }
                    int size = 0;
                    if (str.length > 1) {
                        size = Integer.parseInt(str[1]);
                    }
                    if (size > 0) {
                        String featureAndWeight;
                        while ((featureAndWeight=reader.readLine()) != null) {
                            //System.out.println(featureAndWeight.toString());
                            if (featureAndWeight.isEmpty())
                                break;
                            //str2[0]:特征 str2[1]:weight
                            String[] str2 = featureAndWeight.split(" : ");
                            //weight[0]:current weight[1]:total
                            String[] weight = str2[1].split(" / ");
                            switch (featureTemplate.getName()) {
                                case "SeenWords":
                                case "SeparateChars":
                                case "ConsecutiveChars":
                                case "OneCharWord":
                                    String word = str2[0];
                                    featureTemplate.put(new JWord(word), new JScore(Integer.parseInt(weight[0]), Integer.parseInt(weight[1])));
                                    break;
                                case "LastWordByWord":
                                case "LastWordFirstChar":
                                case "CurrentWordLastChar":
                                case "FirstCharLastWordByWord":
                                case "LastWordByLastChar":
                                case "FirstAndLastChars":
                                    String[] twoWords = str2[0].split(" , ");
                                    String secondString = "";//第2个如果为"",则只有一个twoWords[0]
                                    if (twoWords.length > 1) {
                                        secondString = twoWords[1];
                                    }
                                    featureTemplate.put(new JTwoWords(twoWords[0], secondString), new JScore(Integer.parseInt(weight[0]), Integer.parseInt(weight[1])));
                                    break;
                                case "LengthByFirstChar":
                                case "LengthByLastChar":
                                case "LengthByLastWord":
                                case "LastLengthByWord":
                                    String[] wordInt = str2[0].split(" , ");
                                    featureTemplate.put(new JWordInt(wordInt[0], Integer.parseInt(wordInt[1])), new JScore(Integer.parseInt(weight[0]), Integer.parseInt(weight[1])));
                                    break;
                            }
                        }
                    }
                }
                else {
                    System.out.println("模型文件格式错误,没有标题");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (reader != null)
                reader.close();
            if (inputStreamReader != null)
                inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isScoreModified = false;

        long endTime = System.currentTimeMillis();
        System.out.println("Loading model1 done.(" + (endTime-startTime)/1000+"s).");

    }

    //保存模型文件
    public void saveScores() {
        System.out.println("Save model ...");

        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(modelFile), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
//        try {
//            writer.write(weights.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        List<JScoreMap> list = weights.getList();
        for (JScoreMap scoreMap : list) {
            scoreMap.write(writer);
        }

        try{
            if (writer != null)
                writer.close();
            if (outputStreamWriter != null)
                outputStreamWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        isScoreModified = false;
        System.out.println("Save model1 done.");
    }

    //计算完整的句子分词情况的全局分数
    public int getGlobalScore(JStringVector sentence, JStateItem item) {

        int globalScore = 0;
        for (int i = 0; i < item.getLength(); i++) {
            globalScore += getLocalScore(sentence, item, i);
        }

        return globalScore;
    }

    //计算部分的句子分词情况的局部分数(一个词)
    public int getLocalScore(JStringVector sentence, JStateItem item, int index) {

        int localScore = 0;

        int score_index = isTrain ? 0 : 1;
        int length, last_length, word_length;
        int start, end, last_start, last_end;
        start = item.getWordStart(index);
        end = item.getWordEnd(index);
        length = item.getWordLength(index);
        last_start = index > 0 ? item.getWordStart(index-1) : 0;
        last_end = index > 0 ? item.getWordEnd(index-1) : 0;
        last_length = index > 0 ? item.getWordLength(index-1) : 0;
        word_length = length;

        //words
        JWord word = new JWord(parent.findWordFromCache(start, length, sentence));
        JWord last_word = index > 0 ? new JWord(parent.findWordFromCache(last_start, last_length, sentence)) : emptyWord;
        JTwoWords two_word = new JTwoWords(word, last_word);

        //chars
        JWord first_char = new JWord(parent.findWordFromCache(start, 1, sentence));
        JWord last_char = new JWord(parent.findWordFromCache(end, 1, sentence));
        JWord first_char_last_word = index > 0 ? new JWord(parent.findWordFromCache(last_start, 1, sentence)) : emptyWord;
        JWord last_char_last_word = index > 0 ? new JWord(parent.findWordFromCache(last_end, 1, sentence)) : emptyWord;
        JTwoWords first_and_last_char = new JTwoWords(first_char, last_char);
        JWord two_char = index > 0 ? new JWord(parent.findWordFromCache(last_end, 2, sentence)) : emptyWord;
        JTwoWords lastword_firstchar = null, currentword_lastchar = null, firstcharlastword_word = null, lastword_lastchar = null;
        if(index > 0) {
            lastword_firstchar = new JTwoWords(last_word, first_char);
            currentword_lastchar = new JTwoWords(word, last_char_last_word);
            firstcharlastword_word = new JTwoWords(first_char_last_word, first_char);
            lastword_lastchar = new JTwoWords(last_char_last_word, last_char);
        }

        //length
        if (length > JConfigure.LENGTH_MAX - 1)
            length = JConfigure.LENGTH_MAX - 1;
        if (last_length > JConfigure.LENGTH_MAX - 1)
            last_length = JConfigure.LENGTH_MAX - 1;

        //计算score
        localScore = weights.getScoreMapByName("SeenWords").getScore(word, score_index);
        localScore += weights.getScoreMapByName("LastWordByWord").getScore(two_word, score_index);
        if (length == 1) {
            localScore += weights.getScoreMapByName("OneCharWord").getScore(word, score_index);
        }
        else {
            localScore += weights.getScoreMapByName("FirstAndLastChars").getScore(first_and_last_char, score_index);
            for (int i = 0; i < word_length - 1; i++) {
                localScore += weights.getScoreMapByName("ConsecutiveChars").getScore(new JWord(parent.findWordFromCache(start+i, 2, sentence)), score_index);
            }
            localScore += weights.getScoreMapByName("LengthByFirstChar").getScore(new JWordInt(first_char, length), score_index);
            localScore += weights.getScoreMapByName("LengthByLastChar").getScore(new JWordInt(last_char, length), score_index);
        }
        if (index > 0) {
            localScore += weights.getScoreMapByName("SeparateChars").getScore(two_char, score_index);

            localScore += weights.getScoreMapByName("LastWordFirstChar").getScore(lastword_firstchar, score_index);
            localScore += weights.getScoreMapByName("CurrentWordLastChar").getScore(currentword_lastchar, score_index);
            localScore += weights.getScoreMapByName("FirstCharLastWordByWord").getScore(firstcharlastword_word, score_index);
            localScore += weights.getScoreMapByName("LastWordByLastChar").getScore(lastword_lastchar, score_index);

            localScore += weights.getScoreMapByName("LengthByLastWord").getScore(new JWordInt(last_word, length), score_index);
            localScore += weights.getScoreMapByName("LastLengthByWord").getScore(new JWordInt(word, last_length), score_index);
        }
        return localScore;
    }

    //更新参数向量 a=a-错误+正确
    public void updateScoreVector(JStringVector output, JStringVector correct, int round) {

        if (output.equals(correct))
            return;
        for (int i = 0; i < output.size(); i++) {
            updateLocalFeatureVector(1, output, i, round);
        }
        for (int i = 0; i < correct.size(); i++) {
            updateLocalFeatureVector(0, correct, i, round);
        }
        isScoreModified = true;
    }

    //更新参数向量的每个特征的分数
    public void updateLocalFeatureVector(int method, JStringVector output, int index, int round) {

        //words
        JWord word = new JWord(output.get(index));
        JWord last_word = index > 0 ? new JWord(output.get(index-1)) : emptyWord;
        JTwoWords two_word = new JTwoWords(word, last_word);

        //length
        int length = word.getString().length();
        if (length > JConfigure.LENGTH_MAX - 1)
            length = JConfigure.LENGTH_MAX - 1;
        int last_length = last_word.getString().length();
        if (last_length > JConfigure.LENGTH_MAX - 1)
            last_length = JConfigure.LENGTH_MAX - 1;

        //chars
        JWord first_char = word.getFirstChar();
        JWord last_char = word.getLastChar();
        JWord first_char_last_word = index > 0 ? last_word.getFirstChar() : emptyWord;
        JWord last_char_last_word = index > 0 ? last_word.getLastChar() : emptyWord;
        JWord two_char = index > 0 ? new JWord(last_char_last_word.getString()+first_char.getString()) : emptyWord;
        JTwoWords first_and_last_char = new JTwoWords(first_char, last_char);

        JTwoWords lastword_firstchar=null, currentword_lastchar=null, firstcharlastword_word=null, lastword_lastchar=null;
        if (index > 0) {
            lastword_firstchar = new JTwoWords(last_word, first_char);
            currentword_lastchar = new JTwoWords(word, last_char_last_word);
            firstcharlastword_word = new JTwoWords(first_char_last_word, first_char);
            lastword_lastchar = new JTwoWords(last_char_last_word, last_char);
        }

        int amount = (method==0) ? 1 : -1;
        weights.getScoreMapByName("SeenWords").updateScore(word, amount, round);
        weights.getScoreMapByName("LastWordByWord").updateScore(two_word, amount, round);

        if (length == 1)
            weights.getScoreMapByName("OneCharWord").updateScore(first_char, amount, round);
        else {
            weights.getScoreMapByName("FirstAndLastChars").updateScore(first_and_last_char, amount, round);
            String chars = word.getString();
            for (int i = 0; i < chars.length()-1; i++) {
                weights.getScoreMapByName("ConsecutiveChars").updateScore(new JWord(chars.substring(i, i+2)), amount, round);
            }
            weights.getScoreMapByName("LengthByFirstChar").updateScore(new JWordInt(first_char, length), amount, round);
            weights.getScoreMapByName("LengthByLastChar").updateScore(new JWordInt(last_char, length), amount, round);
        }

        if (index > 0) {
            weights.getScoreMapByName("SeparateChars").updateScore(two_char, amount, round);

            weights.getScoreMapByName("LastWordFirstChar").updateScore(lastword_firstchar, amount, round);
            weights.getScoreMapByName("CurrentWordLastChar").updateScore(currentword_lastchar, amount, round);

            weights.getScoreMapByName("FirstCharLastWordByWord").updateScore(firstcharlastword_word, amount, round);
            weights.getScoreMapByName("LastWordByLastChar").updateScore(lastword_lastchar, amount, round);

            weights.getScoreMapByName("LengthByLastWord").updateScore(new JWordInt(last_word, length), amount, round);
            weights.getScoreMapByName("LastLengthByWord").updateScore(new JWordInt(word, last_length), amount, round);
        }
    }

    //计算平均参数向量
    public void computerAverageFeatureWeights(int round) {
        System.out.println("Computer average feature weights ...");

        List<JScoreMap> list = weights.getList();
        for (JScoreMap scoreMap : list) {
            scoreMap.computerAverage(round);
        }

        System.out.println("Computer average feature weights done.");
    }

    public static void main(String[] args) {
        JFeatureHandle featureHandle = new JFeatureHandle(new JSegmentor("",true), "/Users/zhenghao/WorkSpace/IDEA/JZPar/JZPar-acl07/data/model1.txt", true);
        System.out.print(featureHandle.getWeights());
        featureHandle.saveScores();

        JWord emptyWord = new JWord("");
        System.out.println(emptyWord.getString().length());

    }

}

package com.zhenghao.weight;

import com.zhenghao.type.featuretype.JTwoWords;
import com.zhenghao.type.featuretype.JWord;
import com.zhenghao.type.featuretype.JWordInt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenghao on 16/5/12.
 */
public class JWeight {

    //14个特征模版映射表存放各自的特征
    private JScoreMap<JWord>     mapSeenWords;
    private JScoreMap<JTwoWords> mapLastWordByWord;
    private JScoreMap<JTwoWords> mapLastWordFirstChar;
    private JScoreMap<JTwoWords> mapCurrentWordLastChar;
    private JScoreMap<JTwoWords> mapFirstCharLastWordByWord;
    private JScoreMap<JTwoWords> mapLastWordByLastChar;
    private JScoreMap<JWord>     mapSeparateChars;
    private JScoreMap<JWord>     mapConsecutiveChars;
    private JScoreMap<JTwoWords> mapFirstAndLastChars;
    private JScoreMap<JWord>     mapOneCharWord;
    private JScoreMap<JWordInt>  mapLengthByFirstChar;
    private JScoreMap<JWordInt>  mapLengthByLastChar;
    private JScoreMap<JWordInt>  mapLengthByLastWord;
    private JScoreMap<JWordInt>  mapLastLengthByWord;

    private List<JScoreMap> list = new ArrayList<>();

    public JWeight() {
        mapSeenWords = new JScoreMap<JWord>("SeenWords", 65537);
        mapLastWordByWord = new JScoreMap<JTwoWords>("LastWordByWord", 65537);
        mapLastWordFirstChar = new JScoreMap<JTwoWords>("LastWordFirstChar", 65537);
        mapCurrentWordLastChar = new JScoreMap<JTwoWords>("CurrentWordLastChar", 65537);
        mapFirstCharLastWordByWord = new JScoreMap<JTwoWords>("FirstCharLastWordByWord", 65537);
        mapLastWordByLastChar = new JScoreMap<JTwoWords>("LastWordByLastChar", 65537);
        mapSeparateChars = new JScoreMap<JWord>("SeparateChars", 65537);
        mapConsecutiveChars = new JScoreMap<JWord>("ConsecutiveChars", 65537);
        mapFirstAndLastChars = new JScoreMap<JTwoWords>("FirstAndLastChars", 65537);
        mapOneCharWord = new JScoreMap<JWord>("OneCharWord", 65537);
        mapLengthByFirstChar = new JScoreMap<JWordInt>("LengthByFirstChar", 65537);
        mapLengthByLastChar = new JScoreMap<JWordInt>("LengthByLastChar", 65537);
        mapLengthByLastWord = new JScoreMap<JWordInt>("LengthByLastWord", 65537);
        mapLastLengthByWord = new JScoreMap<JWordInt>("LastLengthByWord", 65537);

        list.add(mapSeenWords);
        list.add(mapLastWordByWord);
        list.add(mapLastWordFirstChar);
        list.add(mapCurrentWordLastChar);
        list.add(mapFirstCharLastWordByWord);
        list.add(mapLastWordByLastChar);
        list.add(mapSeparateChars);
        list.add(mapConsecutiveChars);
        list.add(mapFirstAndLastChars);
        list.add(mapOneCharWord);
        list.add(mapLengthByFirstChar);
        list.add(mapLengthByLastChar);
        list.add(mapLengthByLastWord);
        list.add(mapLastLengthByWord);
    }

    @Override
    public String toString() {

        String string = "";
        for (JScoreMap featureTemplate : list) {
            string += featureTemplate.toString();
        }

        //System.out.println(string);
        return string;
    }

    public List<JScoreMap> getList() {
        return list;
    }

    //根据特征模版名称返回JScoreMap对象
    public JScoreMap getScoreMapByName(String featureTemplate) {
        JScoreMap retScoreMap = null;
        for (JScoreMap scoreMap : list) {
            if (scoreMap.getName().equals(featureTemplate)) {
                retScoreMap = scoreMap;
                break;
            }
        }
        return retScoreMap;
    }

    public static void main(String[] args) {
        JWeight weights = new JWeight();
        List<JScoreMap> list = weights.getList();

//        for (JScoreMap scoreMap : list) {
//            System.out.println(scoreMap.getName());
//        }

        System.out.println(weights.getScoreMapByName("SeenWords"));
        System.out.println(weights.getScoreMapByName("LastWordByWord"));
    }
}

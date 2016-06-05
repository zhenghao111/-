package com.zhenghao.weight;

import com.zhenghao.type.featuretype.JTwoWords;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghao on 16/5/12.
 */
public class JScoreMap<K> extends HashMap<K, JScore>{

    private JScore zero = new JScore();
    private String name;//特征模版的名称
    private int count;//映射表存储的元素个数,特征的个数

    public JScoreMap(String input_name, int tableSize) {
        super(tableSize);
        this.name = input_name;
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    //根据特征key,返回分数JScore.score(which),不存在则返回zero.score(which)
    public int getScore(K key, int which) {
        JScore score = this.get(key);
        return score != null ? score.score(which) : zero.score(which);
    }

    //更新特征模版中的指定特征key的current
    public void updateScore(K key, int amount, int round) {
        JScore score = get(key);
        if (score == null) {
            JScore zero = new JScore();
            this.put((K) key, zero);
            zero.updateCurrent(amount, round);
            return;
        }
        score.updateCurrent(amount, round);
    }

    //对特征模版的每个特征的分数调用updateAverage(round)
    public void computerAverage(int round) {
        this.count  = 0;
        for(Map.Entry<K, JScore> entry : this.entrySet()) {
            entry.getValue().updateAverage(round);
            count++;
        }
    }

    //输出特征模版的映射表里的所有特征向量和参数向量
    @Override
    public String toString() {
        String str = "";
        //输出特征模版的名称和特征数量
        if (this.count > 0)
            str = this.name + " " + this.count + "\n";
        else
            str = this.name + "\n";
        for(Map.Entry<K, JScore> entry : this.entrySet()) {
            if (entry.getValue().score(0) != 0 ||
                    entry.getValue().score(1) != 0) {
                str += entry.getKey().toString() + " : " + entry.getValue().toString() + "\n";
            }
        }
        str += "\n";
        return str;
    }

    public void init() {
        this.clear();
    }

    public static void main(String[] args) {
//        JScoreMap<JWord> test1 = new JScoreMap<>("map1");
//        test1.put(new JWord("key1"), new JScore(1, 1, 1));
//        test1.put(new JWord("key2"), new JScore(2, 2, 2));
//        test1.computerAverage(4);
//        System.out.println(test1.getScore(new JWord("key1"), 0));

        JScoreMap<JTwoWords> test2 = new JScoreMap<>("map2", 65537);
        test2.put(new JTwoWords("str1", "str2"), new JScore(1, 1, 1));
        test2.put(new JTwoWords("str3", "str4"), new JScore(2, 2, 2));
        System.out.println(test2.getScore(new JTwoWords("str1", "str2"), 0));
    }

    public void write(BufferedWriter writer) {
        try {
            if (this.count > 0)
                writer.write(this.name + " " + this.count + "\n");
            else
                writer.write(this.name + "\n");

            for (Map.Entry<K, JScore> entry : this.entrySet()) {
                if(entry.getValue().score(0) != 0 || entry.getValue().score(1) != 0) {
                    writer.write(entry.getKey().toString() + " : " + entry.getValue().toString() + "\n");
                }
                //writer.write(entry.getKey().toString() + " : " + entry.getValue().toString() + "\n");
            }

            writer.write("\n");
        }
        catch (IOException e) {
            e.printStackTrace();;
        }
    }
}

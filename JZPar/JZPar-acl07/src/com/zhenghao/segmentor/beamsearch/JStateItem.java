package com.zhenghao.segmentor.beamsearch;

import com.zhenghao.conf.JConfigure;

/**
 * Created by zhenghao on 16/5/14.
 *
 */
public class JStateItem {

    private int[] words;//记录每个词最后一个字的索引
    private int length;//当前状态下词的数量
    private int score;

    public JStateItem() {
        words = new int[JConfigure.MAX_SENTENCE_SIZE];
        clear();
    }

    public int[] getWords() {
        return words;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public void setWords(int[] words) {
        this.words = words;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //小于
    public boolean isLE(JStateItem item) {
        return this.score < item.score;
    }

    //大于
    public boolean isGE(JStateItem item) {
        return this.score > item.score;
    }

    //等于
    public boolean isEQ(JStateItem item) {
        if (this.length != item.length)
            return false;
        for (int i = 0; i < this.length; i++) {
            if (this.words[i] != item.words[i])
                return false;
        }
        return true;
    }

    //不等于
    public boolean isNEQ(JStateItem item) {
        return !this.isEQ(item);
    }

    public int getWordStart(int i) {
        return i > 0 ? this.words[i-1]+1 : 0;
    }

    public int getWordEnd(int i) {
        return this.words[i];
    }

    public int getWordLength(int i) {
        return i > 0 ? this.words[i] - this.words[i-1] : this.words[0] + 1;
    }

    private void clear() {
        score = 0;
        length = 0;
    }

    public JStateItem copy() {
        JStateItem copyItem = new JStateItem();
        for (int i = 0; i < this.length; i++) {
            copyItem.words[i] = this.words[i];
        }
        copyItem.length = this.length;
        copyItem.score = this.score;
        return copyItem;
    }

    public void append(int char_index) {
        this.words[length] = char_index;
        length++;
    }

    public void replace(int char_index) {

        words[length-1] = char_index;

    }
}

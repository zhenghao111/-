package com.zhenghao.type.featuretype;

import java.util.Objects;

/**
 * Created by zhenghao on 16/5/13.
 */
public class JTwoWords {

    private String first;
    private String second;

    public JTwoWords(String firstString, String secondString) {
        this.first = firstString;
        this.second = secondString ;
    }

    public JTwoWords(JWord firstWord, JWord secondWord) {
        this.first = firstWord.getString();
        this.second = secondWord.getString();
    }

    public String getFirst() {
        return first;
    }

    public String getSecondS() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JTwoWords jTwoWords = (JTwoWords) o;

        if (first != null ? !first.equals(jTwoWords.first) : jTwoWords.first != null) return false;
        return second != null ? second.equals(jTwoWords.second) : jTwoWords.second == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return first + " , " + second;
    }

    public static void main(String[] args) {
        //System.out.println(new JTwoWords("123", "1234").equals(new JTwoWords("123", "1234")));
        //System.out.println(new JTwoWords("", " ").toString());

        //hashcode
        System.out.println(new JTwoWords("123", "234").hashCode());
        System.out.println(new JTwoWords("123", "234").hashCode());
    }
}

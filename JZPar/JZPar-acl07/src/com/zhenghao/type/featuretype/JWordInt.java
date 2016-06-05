package com.zhenghao.type.featuretype;

import java.util.Objects;

/**
 * Created by zhenghao on 16/5/14.
 */
public class JWordInt {

    private String first;
    private int   second;

    public JWordInt(String firstString, int secondInt) {
        this.first = firstString;
        this.second = secondInt;
    }

    public JWordInt(JWord firstWord, int secondInt) {
        this.first = firstWord.getString();
        this.second = secondInt;
    }

    public String getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JWordInt jWordInt = (JWordInt) o;

        if (second != jWordInt.second) return false;
        return first != null ? first.equals(jWordInt.first) : jWordInt.first == null;

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
        System.out.println(new JWordInt("123", 12311).hashCode()+":"+(new JWordInt("123", 12311).hashCode()));
    }
}

package com.zhenghao.type.featuretype;

import java.util.Objects;

/**
 * Created by zhenghao on 16/5/13.
 */
public class JWord {

    private String string;

    public JWord(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JWord word = (JWord) o;

        return string != null ? string.equals(word.string) : word.string == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    public String getString() {
        return string;
    }

    public JWord getFirstChar() {
        if (string.equals(""))
            return new JWord("");
        return new JWord(string.substring(0, 1));
    }

    public JWord getLastChar() {
        if (string.equals(""))
            return new JWord("");
        return new JWord(string.substring(string.length()-1));
    }

    @Override
    public String toString() {
        return string;
    }

    public static void main(String[] args) {

        //String��hashcode�Ѿ���д,ֻ���������.�Զ�����಻��дhashcode,��ʹ��Object.hashcode,���صĶ����ַ
        System.out.println(new JWord("ss") == new JWord("ss"));//�Ƚϵ��ǵ�ַ
        System.out.println(new JWord("ss").equals(new JWord("ss")));//equals����, equalsΪtrue ��������ͬ��hashcode
        System.out.println(new JWord("ss").hashCode() + ":" + new JWord("ss").hashCode());//hashcodeһ��

//        System.out.println(new JWord(" ").getFirstChar());
//        System.out.println(new JWord("").getLastChar());
//        System.out.println(new JWord(" ").getLastChar());
//        System.out.println(new JWord("s").getLastChar());
//        System.out.println(new JWord("sd").getLastChar());
    }
}

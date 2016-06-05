package com.zhenghao.type;

import java.util.ArrayList;

/**
 * Created by zhenghao on 16/5/14.
 */
public class JStringVector extends ArrayList<String> {

    public static void main(String[] args) {
        JStringVector vector1 = new JStringVector();
        vector1.add("123");
        vector1.add("456");

        JStringVector vector2 = new JStringVector();
        vector2.add("123");
        vector2.add("456");

        System.out.println(vector1.equals(vector2));
    }

}

package com.zhenghao.util;

import com.zhenghao.type.JStringVector;

/**
 * Created by zhenghao on 16/5/14.
 */
public class JSentenceUtil {

    public static JStringVector DesegmentSentence(JStringVector refSentence) {
        JStringVector inputSentence = new JStringVector();
        if (refSentence == null)
            return null;
        for (String string: refSentence) {
            for (int index = 0; index < string.length(); index++) {
                inputSentence.add(string.substring(index, index+1));
            }
        }
        return inputSentence;
    }
}

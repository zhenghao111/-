package com.zhenghao.main;

import com.zhenghao.test.SegmentorTest;
import com.zhenghao.train.SegmentorTrain;

/**
 * Created by zhenghao on 16/5/18.
 */
public class JZparMain {

    public static void main(String[] args) {

        if (args[0].equals("train")) {
            SegmentorTrain.main(new String[]{args[1], args[2], args[3]});
        }
        else if (args[0].equals("test")) {
            SegmentorTest.main(new String[]{args[1], args[2], args[3]});
        }
    }

}

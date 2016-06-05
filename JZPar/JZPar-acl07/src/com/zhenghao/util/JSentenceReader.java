package com.zhenghao.util;

import com.zhenghao.type.JStringVector;

import java.io.*;

/**
 * Created by zhenghao on 16/5/14.
 */
public class JSentenceReader {

    private InputStreamReader inputStreamReader;
    private BufferedReader reader;

    public JSentenceReader(String fileName)  {

        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(inputStreamReader);
    }

    public JStringVector readSegmentedSentence() {
        JStringVector segmentedSentence = null;

        String str1;
        try {
            if ((str1 = reader.readLine()) != null) {
                segmentedSentence = new JStringVector();
                String[] strings = str1.split(" ");
                for (String str2 : strings) {
                    //空行会有一项为空的元素,所以空的不添加,空行返回元素个数为0的数组列表
                    if (!str2.isEmpty())
                        segmentedSentence.add(str2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return segmentedSentence;
    }

    public JStringVector readRawSentence() {
        JStringVector rawSentence = null;
        String str1;
        try {
            if ((str1 = reader.readLine()) != null) {
                rawSentence = new JStringVector();

                for (int i=0; i<str1.length(); i++) {
                    String oneChar = str1.substring(i, i+1).trim();
                    if (!oneChar.isEmpty()) {
                        rawSentence.add(oneChar);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawSentence;
    }

    public void close() {
        try {
            if (reader != null)
                reader.close();
            if (inputStreamReader != null)
                inputStreamReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        JSentenceReader reader = new JSentenceReader("/Users/zhenghao/WorkSpace/IDEA/JZPar/JZPar-acl07/data/msr_training4.txt");

        JStringVector stringVector;
        while ((stringVector = reader.readSegmentedSentence())!=null) {
            for (String string : stringVector) {
                System.out.println(string);
            }
        }

    }
}

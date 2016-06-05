package com.zhenghao.util;

import com.zhenghao.type.JStringVector;

import java.io.*;

/**
 * Created by zhenghao on 16/5/17.
 */
public class JSentenceWriter {

    private OutputStreamWriter outputStreamWriter;
    private BufferedWriter writer;

    public JSentenceWriter(String outputFile) {

        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer = new BufferedWriter(outputStreamWriter);
    }

    public void writeSegmentedSentence(JStringVector outputSentence) {
        try {
            if (outputSentence.size() > 0)
                writer.write(outputSentence.get(0));
            for (int i = 1; i < outputSentence.size(); i++) {
                writer.write(" "+outputSentence.get(i));
            }
            writer.write("\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (writer != null)
                writer.close();
            if (outputStreamWriter != null)
                outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

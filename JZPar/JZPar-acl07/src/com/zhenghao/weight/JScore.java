package com.zhenghao.weight;

/**
 * Created by zhenghao on 16/5/12.
 */
public class JScore {


    public enum SCORE_AVERAGE{
        eNonAverage, eAverage
    }
    private int total;
    private int current;
    private int lastupdate;

    public JScore()
    {
        total = 0;
        current = 0;
        lastupdate = 0;
    }

    public  JScore(int current, int total) {
        this.current = current;
        this.total = total;
        this.lastupdate = 0;
    }

    public JScore(int current, int total, int lastupdate) {
        this.current = current;
        this.total = total;
        this.lastupdate = lastupdate;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrent() {
        return current;
    }

    public int getLastupdate() {
        return lastupdate;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setLastupdate(int lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int score(int n) {
        if(n == 0)
            return current;
        else
            return total;
    }

    public void updateCurrent(int added, int round) {
        if (round > lastupdate) {
            updateAverage(round);
            lastupdate = round;
        }
        current += added;
        total += added;
    }

    public void updateAverage(int round) {

        if (round > lastupdate) {
            total += current*(round - lastupdate);
        }
        else if (round < lastupdate) {
            System.out.println("Round is:"+round+"<"+lastupdate);
        }
    }

    @Override
    public String toString() {
        return getCurrent()+" / "+getTotal();
    }

    public static void main(String[] args) {
        JScore score = new JScore();
        System.out.println(score.toString());
        System.out.println(SCORE_AVERAGE.eNonAverage.ordinal());
        System.out.println(SCORE_AVERAGE.eAverage.ordinal());

    }

}

package EAHMP;

import java.util.ArrayList;

public class ValueTableItem {
    private int tID = 0; // 事务编号
    private ArrayList<Integer> optNums = new ArrayList<Integer>();// 物品编号集合
    private double objVal = 0d;// 价值

    public ValueTableItem(ArrayList<Integer> optNums, double objVal) {
        this.optNums = optNums;
        this.objVal = objVal;

    }

    public ValueTableItem(Integer[] optNums, double objVal) {
        for (Integer optNum : optNums) {
            this.optNums.add(optNum);
        }
        this.objVal = objVal;
    }

    public int getTID() {
        return tID;
    }

    public void setTID(int tID) {
        this.tID = tID;
    }

    public ArrayList<Integer> getOptNums() {
        return optNums;
    }

    public void setOptNums(ArrayList<Integer> optNums) {
        this.optNums = optNums;
    }

    public double getObjVal() {
        return objVal;
    }

    public void setObjVal(double objVal) {
        this.objVal = objVal;
    }
}

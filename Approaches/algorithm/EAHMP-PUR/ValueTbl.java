import sun.security.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValueTbl {
    public static int tID=0;//事务编号
    // 存放表项集
    private ArrayList<ValueTableItem> valueTblItems = new ArrayList<ValueTableItem>();
    // 数组工具类
    ArrayUtil arrayUtil = new ArrayUtil();

    public ValueTbl(){
        tID=0;
    }

    public ArrayList<ValueTableItem> getValueTableItems() {
        return valueTblItems;
    }

    public ArrayList<Individual> curGaIndividuals=new ArrayList<>();

    public void setValueTblItems(ArrayList<ValueTableItem> valueTblItems) {
        this.valueTblItems = valueTblItems;
    }

    /**
     * @param item
     *            :表项
     */
    public void add(ValueTableItem item) {
        tID++;
        item.setTID(tID);
        valueTblItems.add(item);
    }

    /**
     * 获取选择物品构成的项集
     *
     * @return
     */

    //当前种群
    public void initValueItems(List<Individual> GaIndividuals){
        int size=GaIndividuals.size();
        List<Individual>  sortGaIndividuals=new ArrayList<>();
        for(int i=0;i<size;i++) {
            Individual newFP=new Individual(GaIndividuals.get(i));
            sortGaIndividuals.add(newFP);
        }
        Collections.sort(sortGaIndividuals);

        for(int i=0;i<size;i++){
            curGaIndividuals.add(sortGaIndividuals.get(i));
            ArrayList<Integer> optNums=new ArrayList<>();
            for(int j=0;j<sortGaIndividuals.get(i).getCode().length;j++){
                if(sortGaIndividuals.get(i).getCode()[j]==1){
                    optNums.add(j);
                }
            }
            if(optNums.size() !=0){
                ValueTableItem valueTableItem=new ValueTableItem(optNums,sortGaIndividuals.get(i).getFitness());
                add(valueTableItem);
            }
        }
    }

    /**
     * 获取能耗表的大小
     *
     * @return
     */
    public int getSize() {
        return valueTblItems.size();
    }




    public String getOpNumsStr(ArrayList<Integer> optNums) {
        String rltStr = "[";
        int size = optNums.size();
        for (int i = 0; i < size; i++) {
            Integer opNum = optNums.get(i);
            if (i == size - 1) {
                rltStr += opNum + "]";
            } else {
                rltStr += opNum + ",";
            }
        }

        return rltStr;
    }

    public ArrayList<Individual> getCurMindividuals() {
        return curGaIndividuals;
    }

    public void setCurMindividuals(ArrayList<Individual> curGaIndividuals) {
        this.curGaIndividuals = curGaIndividuals;
    }
}

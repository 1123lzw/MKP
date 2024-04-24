package EAHMP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CondPattBase {
    // 项集的集合，相当于事务数据库
    private ArrayList<ArrayList<Item>> itemSets = new ArrayList<ArrayList<Item>>();
    // 最小支持度计数
    private int minSupportCount = 0;
    // 频繁1项集
    private ArrayList<Item> fq1ItemSetBySuptDesOrd = new ArrayList<Item>();
    //贡献度表
    private ArrayList<Item> conList = new ArrayList<Item>();

    /**
     * 按项名称在项集查找指定项，若存在同名的项，则返回其第一次在出现在项集中的下标位置，否则返回-1
     *
     * @param item:    项
     * @param items：项集
     * @return
     */
    public int find(Item item, ArrayList<Item> items) {
        int foundIdx = -1;
        String curItemName = item.getItemName();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            String freItemName = items.get(i).getItemName();
            if (curItemName.equals(freItemName)) {
                foundIdx = i;
                break;
            }
        }
        return foundIdx;
    }

    /**
     * 加载原始的项集的集合
     *
     * @param tbl：带目标标注的数据表
     * @return
     */

    public ArrayList<ArrayList<Item>> loadItemSets(ValueTbl tbl, MKPProb solveProb) {
        ArrayList<ArrayList<Item>> origItemSets = new ArrayList<ArrayList<Item>>();  //标注<编号，次数，总价值，物品价值>的表
        ArrayList<ValueTableItem> MBDEValueTableItems = tbl.getValueTableItems(); //事务
        double[] value=solveProb.getValueVectortran();
        for (ValueTableItem MBDEValueTableItem : MBDEValueTableItems) {
            // 选用的编译选项编号的集合
            ArrayList<Integer> optNums = MBDEValueTableItem.getOptNums();
            ArrayList<Item> itemSet = new ArrayList<Item>();
            double objVal = MBDEValueTableItem.getObjVal();
            for (Integer optNum : optNums) {  //<编号，次数，价值,物品价值>
                Item item = new Item(String.valueOf(optNum), 1, objVal,value[optNum]);
                itemSet.add(item);
            }
            origItemSets.add(itemSet);  //一条数据
        }
        itemSets = origItemSets;
        return origItemSets;
    }

    /**
     * 获取按价值比排序的频繁1项集
     *
     * @param itemSets：项集的集合，也称模式库
     * @param minSupportCount:最小支持度
     * @return
     */
    public ArrayList<Item> getFre1ItemSetByValDesOrd(ArrayList<ArrayList<Item>> itemSets, int minSupportCount) {
        ArrayList<Item> fre1ItemSet = new ArrayList<Item>();
        conList=new ArrayList<>();
        for (ArrayList<Item> items : itemSets) {
            for (Item item : items) {
                int foundIdx = find(item, fre1ItemSet);
                if (foundIdx == -1) {  //不在频繁一项集中加入
                    Item newPattItem = item.copy();
                    fre1ItemSet.add(newPattItem);
                } else {
                    Item foundItem = fre1ItemSet.get(foundIdx);
                    foundItem.setCount(foundItem.getCount() + 1);  //次数相加
                    foundItem.setTotValue(foundItem.getTotValue()+item.getTotValue());//累加价值
                }
            }
        }

        Collections.sort(fre1ItemSet, Collections.reverseOrder());  //逆序排序
        int i = 0;
        while (i < fre1ItemSet.size()) {
            Item freItem = fre1ItemSet.get(i);
            int supCnt = freItem.getCount();
            if (supCnt < minSupportCount) {
                fre1ItemSet.remove(freItem);  //移除不频繁的项
            } else {
                i++;
            }
        }
        setMinSupportCount(minSupportCount);
        fq1ItemSetBySuptDesOrd = fre1ItemSet;
        return fre1ItemSet;
    }



    /**
     * 去除模式库中各模式中的非频繁1项的项后， 再按fq1Itemset中各项出现的顺序进行排序，返回新的模式库
     *
     * @param origPatts：原始的模式库（相当于事务数据库），其各模式(项集)中可能包含非频繁1项集中的项
     * @param fq1Itemset：频繁1项集且按支持计数降序排列
     * @return
     */
    public ArrayList<ArrayList<Item>> getNewPattBaseByFq1ItemSet(ArrayList<ArrayList<Item>> origPatts,
                                                                      ArrayList<Item> inFq1ItemSetBySuptDesOrd) {
        ArrayList<ArrayList<Item>> newPattbase = new ArrayList<ArrayList<Item>>();
        for (ArrayList<Item> items : origPatts) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            for (Item item : inFq1ItemSetBySuptDesOrd) {  //查找频繁项集的每一项
                int foundIdx = find(item, items);//在一行
                if (foundIdx != -1) {  //找到
                    newItems.add(items.get(foundIdx).copy());
                }
            }
            if (newItems.size() > 0) {
                newPattbase.add(newItems);
            }
        }
        itemSets = newPattbase;
        return newPattbase;
    }



    /**
     * 根据项集的集合和支持计数，重置条件模式库
     *
     * @param patts:模式库
     * @param minSupportCount：支持计数
     */
    public void initNewPattbase(ArrayList<ArrayList<Item>> patts, int minSupportCount) {
        this.minSupportCount = minSupportCount;
        fq1ItemSetBySuptDesOrd = getFre1ItemSetByValDesOrd(patts, minSupportCount);
        ArrayList<ArrayList<Item>> newPatts = getNewPattBaseByFq1ItemSet(patts, fq1ItemSetBySuptDesOrd);
        itemSets = newPatts;
    }

    /**
     * 获取初始模式库，相当于初始事务数据库
     *
     * @param tbl: 有改进效果编译选项集对应的能耗数据表
     * @return
     */
    public void initPattbase(List<Individual> gaIndividuals, int minSupportCount,  MKPProb solveProb) {
        ValueTbl valueTbl=new ValueTbl();
        valueTbl.initValueItems(gaIndividuals);
        ArrayList<ArrayList<Item>> oriItemSets = loadItemSets(valueTbl,solveProb);
        initNewPattbase(oriItemSets, minSupportCount);
    }


    public ArrayList<ArrayList<Item>> getItemSets() {
        return itemSets;
    }

    public void setItemSets(ArrayList<ArrayList<Item>> itemSets) {
        this.itemSets = itemSets;
    }

    public int getMinSupportCount() {
        return minSupportCount;
    }

    public void setMinSupportCount(int minSupportCount) {
        this.minSupportCount = minSupportCount;
    }

    public ArrayList<Item> getFq1ItemSetBySuptDesOrd() {
        return fq1ItemSetBySuptDesOrd;
    }

    public void setFq1ItemSetBySuptDesOrd(ArrayList<Item> fq1ItemSetBySuptDesOrd) {
        this.fq1ItemSetBySuptDesOrd = fq1ItemSetBySuptDesOrd;
    }

    public ArrayList<Item> getConList() {
        return conList;
    }

    public void setConList(ArrayList<Item> conList) {
        this.conList = conList;
    }
}


package MFCO;

import EAHMP.MKPProb;

import java.util.ArrayList;
import java.util.List;

public class FPTree {
    // FP树的根结点
    private TreeNode rootNode = null;
    // FP对应的条件模式库
    CondPattBase condPattBase = null;



    /**
     * 按项名在结点集查找指定结点，若存在同名的结点，
     * 则返回其第一次在出现在结点集中的下标位置，否则返回-1
     * @param itemName: 项名
     * @param nodes：FP树的结点列表
     * @return
     */
    public int findNode(String itemName, ArrayList<TreeNode> nodes) {
        int foundIdx = -1;
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            String nodeName = nodes.get(i).getItemName();
            if (itemName.equals(nodeName)) {
                foundIdx = i;
                break;
            }
        }
        return foundIdx;
    }



    /**
     * 将一个项集（相当于事务数据库一条记录）插入到树中
     *
     * @param pattItems
     *            : 待插入的项集
     * @param hdTbl
     *            ：头表
     * @param tree
     *            ：树根结点
     */
    public void insertTreeRatio(ArrayList<Item> items, TreeNode node, MKPProb solveProb) {
        double[] value = solveProb.getValueVectortran();
        double[][] constrain = solveProb.getContraintMatrixtran();
        if (items.size() > 0) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            for (Item item : items) {
                newItems.add(item);
            }
            Item item = newItems.get(0);
            int count = item.getCount();
            String itemName = item.getItemName();
            double totValue = item.getTotValue();
            ArrayList<TreeNode> childNodes = node.getChildNodes();
            int foundIdx = findNode(itemName, childNodes);  //找到孩子结点的下标
            TreeNode newNode = null;
            if (foundIdx == -1) {// 未找到
                newNode = new TreeNode(itemName, count, totValue,0d);
                childNodes.add(newNode);
                newNode.setParenNode(node);
                TreeNode ratioNode=newNode;
                ArrayList<Integer> pars=new ArrayList<>();  //记录父节点
                while( !ratioNode.getParenNode().getItemName().equals("null")){ //有父节点
                    int parsNum=Integer.parseInt(ratioNode.getParenNode().getItemName());
                    pars.add(parsNum);
                    ratioNode=ratioNode.getParenNode();
                }

                double sum=0;
                int itemNum=Integer.parseInt(itemName);
                for(int j=0;j<constrain[0].length;j++){ //约束个数
                    double chooseSum=0;
                    for(int i=0;i<pars.size();i++){
                        chooseSum += constrain[pars.get(i)][j];
                    }
                    double jRatio=constrain[itemNum][j]/(constrain[constrain.length-1][j]-chooseSum);
                    sum += jRatio;
                }
                double ratio=value[itemNum]/(sum/constrain[0].length);
                newNode.setRatio(ratio);
            } else {// 找到
                newNode = childNodes.get(foundIdx);
                count = newNode.getCount() + 1;
                totValue += newNode.getValue();
                newNode.setCount(count);
                newNode.setValue(totValue);

            }
            newItems.remove(0); //删除第一个元素
            insertTreeRatio(newItems,newNode,solveProb);
        }
    }

    public int getMinSupportCount() {
        int rlt = 0;
        if (condPattBase != null) {
            rlt = condPattBase.getMinSupportCount();
        }
        return rlt;
    }

    /**
     *
     * @param patts
     *            :已按频繁1项集排序好的条件模式库
     * @param minSupportCount
     * @return
     */
    public void buildCondFPTree(CondPattBase condPattBase, MKPProb solveProb) {
        //CondPattBase condPattBase,MKPProb solveProb
        this.condPattBase = condPattBase;
        ArrayList<ArrayList<Item>> itemSets = condPattBase.getItemSets();
        //double[] ratios=comRatio(solveProb);
        // 树的根结点
        //rootNode = new MBDEFPTreeNode("null", 0, 0d);
        rootNode = new TreeNode("null", 0, 0d,0d);
        for (ArrayList<Item> items : itemSets) {
            //insertTree(items, rootNode,solveProb);
            insertTreeRatio(items,rootNode,solveProb);
        }
    }

    /**
     * 始化FP树
     *
     *
     * @param minSupportCount
     *            ：最小支持计数
     */
    public void buildInitiFPTree(List<Individual> mindividuals, int minSupportCount, MKPProb solveProb) {
        // 获取初始的条件模式库
        condPattBase = new CondPattBase();
        condPattBase.initPattbase(mindividuals, minSupportCount,solveProb);
        buildCondFPTree(condPattBase,solveProb);

    }


    /**
     * 判断FP树是否单一路径，约定根为null,或仅含根的FP树均为单一路径
     * @return
     */
    public boolean isSinglePath() {
        boolean rlt = true;
        TreeNode curNode = rootNode;
        while (curNode != null) {
            int numChld = curNode.getChildNodes().size();
            if (numChld > 1) {
                rlt = false;
                break;
            }
            if (numChld == 0) {
                break;
            }
            if (numChld == 1) {
                curNode = curNode.getChildNodes().get(0);
            }
        }
        return rlt;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public CondPattBase getCondPattBase() {
        return condPattBase;
    }

    public void setCondPattBase(CondPattBase condPattBase) {
        this.condPattBase = condPattBase;
    }
}


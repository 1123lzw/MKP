import java.util.ArrayList;

public class TreeNode {
    // 项名
    private String itemName = "";
    // 支持计数数量
    private Integer count = 1;
    // 与目标值之和
    private double value=0f;
    private double ratio=0; //计算伪功效
    // 孩子结点，可以为多个
    private ArrayList<TreeNode> childNodes = new ArrayList<TreeNode>();
    // 双亲结点
    private TreeNode parenNode = null;


    public TreeNode(String name, int count,double value,double ratio) {
        this.itemName = name;
        this.count = count;
        this.value=value;
        this.ratio=ratio;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public ArrayList<TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(ArrayList<TreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    public TreeNode getParenNode() {
        return parenNode;
    }

    public void setParenNode(TreeNode parenNode) {
        this.parenNode = parenNode;
    }




    /**
     * 判断结点是否仅有一个儿子且无孙子
     * @return
     */
    public boolean hasOnlyOneChd(){
        boolean rlt=false;

        int size=childNodes.size();
        if(size==1){
            TreeNode chdNode=childNodes.get(0);
            if(chdNode.getChildNodes().size()==0){
                rlt=true;
            }
        }
        return rlt;
    }

    /**
     * 判断是否没有孩子
     * @return
     */
    public boolean noChild(){
        boolean rlt=false;
        int size=childNodes.size();
        if(size==0){
            rlt=true;
        }
        return rlt;
    }

    public TreeNode copy() {  //复制结点


        TreeNode newNode = new TreeNode(itemName, count,value,ratio);
        // 因为对象内部有引用，需要采用深拷贝
        if(parenNode!=null){
            newNode.parenNode=parenNode.copy();
        }
        ArrayList<TreeNode> cpyChildNodes = newNode.getChildNodes();
        for (TreeNode childNode : childNodes) {
            cpyChildNodes.add(childNode.copy());
        }
        return newNode;
    }
}

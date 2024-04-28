package MFCO;

public class Item implements Comparable<Item>{
    //项名
    private String itemName = null;
    //支持计数
    private int count = 0;
    // 出现该项时关联的总价值
    private double totValue=0d;
    //该项的伪功效比
    private  double value=0d;

    public Item(String itemName, int count, double totValue, double value) {
        this.itemName = itemName;
        this.count = count;
        this.totValue=totValue;
        this.value=value;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public double getTotValue() {
        return totValue;
    }

    public void setTotValue(double totValue) {
        this.totValue = totValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    @Override  //顺序  从大到小排序 按照物品价值排序
    public int compareTo(Item taget) {
        int rlt=0;
        if(this.value<taget.value){
            rlt=-1;
        }
        if(this.value>taget.value){
            rlt=1;
        }
        return rlt;
    }



    public Item copy(){
        Item newPattItem=new Item(itemName, count,totValue,value);

        return newPattItem;
    }
}


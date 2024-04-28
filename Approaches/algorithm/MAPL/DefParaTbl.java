package MAPL;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DefParaTbl {
    private int archSize;
    private double lR;
    private int maxGenNum;
    private int popSize;

    public DefParaTbl(){

    }


    public DefParaTbl(int archSize,double lR,int maxGenNum,int popSize){
        this.archSize=archSize;
        this.lR=lR;
        this.maxGenNum=maxGenNum;
        this.popSize=popSize;
    }
    public DefParaTbl(DefParaTbl maplDefParaTbl){
        this.archSize=maplDefParaTbl.archSize;
        this.lR=maplDefParaTbl.lR;
        this.maxGenNum=maplDefParaTbl.maxGenNum;
        this.popSize=maplDefParaTbl.popSize;
    }
    public static DefParaTbl getMAPLDefParaTbl(List<DefPara> defParas) throws IOException, ClassNotFoundException {
        DefParaTbl maplDefParaTbl=null;
        //装配bFOADefParaTbl特有的参数表
        int archSize=0;
        double lR=0d;
        int maxGenNum=0;
        int popSize=0;
        for (DefPara aPara:defParas ) {
            String paraName=aPara.getParaName().trim();
            if(paraName.equals("archSize")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                archSize=(Integer)objInt.readObject();
            }
            if(paraName.equals("lR")){
                ByteArrayInputStream byteInt= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                lR=(Double) objInt.readObject();
            }
            if(paraName.equals("maxGenNum")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                maxGenNum=(Integer)objInt.readObject();
            }
            if(paraName.equals("popSize")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                popSize=(Integer)objInt.readObject();
            }
        }
        maplDefParaTbl =new DefParaTbl(archSize,lR,maxGenNum,popSize);
        return maplDefParaTbl;
    }
    public List<RunParaConf> getRunParaConfs(String curProbName, int curRunNum) throws IOException {
        List<RunParaConf> runParaConfs=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix=new ObjectOutputStream(bytweightMatrix);
        objweightMatrix.writeObject(archSize);
        byte[] a=bytweightMatrix.toByteArray();

        RunParaConf runParaConf=new RunParaConf("MAPLAlg",curProbName,curRunNum,"archSize",a);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs2=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix2=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix2=new ObjectOutputStream(bytweightMatrix2);
        objweightMatrix2.writeObject(lR);
        byte[] b=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("MAPLAlg",curProbName,curRunNum,"lR",b);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs3=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix3=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix3=new ObjectOutputStream(bytweightMatrix3);
        objweightMatrix3.writeObject(maxGenNum);
        byte[] c=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("MAPLAlg",curProbName,curRunNum,"maxGenNum",c);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs4=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix4=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix4=new ObjectOutputStream(bytweightMatrix4);
        objweightMatrix4.writeObject(popSize);
        byte[] d=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("MAPLAlg",curProbName,curRunNum,"popSize",d);
        runParaConfs.add(runParaConf);
        return  runParaConfs;
    }
    public int getArchSize() {
        return archSize;
    }

    public void setArchSize(int archSize) {
        this.archSize = archSize;
    }

    public double getlR() {
        return lR;
    }

    public void setlR(double lR) {
        this.lR = lR;
    }

    public int getMaxGenNum() {
        return maxGenNum;
    }

    public void setMaxGenNum(int maxGenNum) {
        this.maxGenNum = maxGenNum;
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }
    public void finalize() throws Throwable {

    }
}

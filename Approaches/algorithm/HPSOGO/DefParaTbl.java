package HPSOGO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DefParaTbl {
    private int maxGenNum;//最大迭代次数
    private double personRatio;//个体学习率
    private double globalRatio;//社会学习率
    private int particleNum;//粒子群大小
    private long kMax;//惩罚函数值放大倍数

    public DefParaTbl(){

    }

    public DefParaTbl(int maxGenNum,double personRatio,double globalRatio,int particleNum,long kMax){
        this.maxGenNum=maxGenNum;
        this.personRatio=personRatio;
        this.globalRatio=globalRatio;
        this.particleNum=particleNum;
        this.kMax=kMax;
    }

    public DefParaTbl(DefParaTbl hpsogCurParaTbl){
        this.maxGenNum=hpsogCurParaTbl.getMaxGenNum();
        this.personRatio=hpsogCurParaTbl.getPersonRatio();
        this.globalRatio=hpsogCurParaTbl.getGlobalRatio();
        this.particleNum=hpsogCurParaTbl.getParticleNum();
        this.kMax=hpsogCurParaTbl.getkMax();
    }

    public static DefParaTbl getHPSOGDefParaTbl(List<DefPara> defParas) throws IOException, ClassNotFoundException {
        DefParaTbl hpsogDefParaTbl =null;
        //装配HPSOGOAlg特有的参数表
        int maxGenNum=0;//最大迭代次数
        double personRatio=0d;//个体学习率
        double globalRatio=0d;//社会学习率
        int particleNum=0;//粒子群大小
        long kMax=0;//惩罚函数值放大倍数  所有物品的价值和
        for (DefPara aPara:defParas ) {
            String paraName=aPara.getParaName().trim();
            if(paraName.equals("maxGenNum")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                maxGenNum=(Integer)objInt.readObject();
            }
            if(paraName.equals("personRatio")){
                ByteArrayInputStream byteDouble= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteDouble);
                personRatio=(Double) objInt.readObject();
            }
            if(paraName.equals("globalRatio")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                globalRatio=(Double)objInt.readObject();
            }
            if(paraName.equals("particleNum")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                particleNum=(Integer)objInt.readObject();
            }
            if(paraName.equals("kMax")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                kMax=(Long)objInt.readObject();
            }
        }
        hpsogDefParaTbl =new DefParaTbl(maxGenNum,personRatio,globalRatio,particleNum,kMax);
        return hpsogDefParaTbl;
    }

    /**
     *
     * @param curProbName
     * @param curRunNum
     * @return
     */

    public List<RunParaConf> getRunParaConfs(String curProbName, int curRunNum) throws IOException {
        List<RunParaConf> runParaConfs=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix=new ObjectOutputStream(bytweightMatrix);
        objweightMatrix.writeObject(maxGenNum);
        byte[] a=bytweightMatrix.toByteArray();

        RunParaConf runParaConf=new RunParaConf("HPSOGOAlg",curProbName,curRunNum,"maxGenNum",a);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs2=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix2=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix2=new ObjectOutputStream(bytweightMatrix2);
        objweightMatrix2.writeObject(personRatio);
        byte[] b=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HPSOGOAlg",curProbName,curRunNum,"personRatio",b);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs3=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix3=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix3=new ObjectOutputStream(bytweightMatrix3);
        objweightMatrix3.writeObject(globalRatio);
        byte[] c=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HPSOGOAlg",curProbName,curRunNum,"globalRatio",c);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs4=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix4=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix4=new ObjectOutputStream(bytweightMatrix4);
        objweightMatrix2.writeObject(particleNum);
        byte[] d=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HPSOGOAlg",curProbName,curRunNum,"particleNum",d);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs5=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix5=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix5=new ObjectOutputStream(bytweightMatrix5);
        objweightMatrix5.writeObject(kMax);
        byte[] e=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HPSOGOAlg",curProbName,curRunNum,"kMax",e);
        runParaConfs.add(runParaConf);
        return  runParaConfs;
    }

    public int getMaxGenNum() {
        return maxGenNum;
    }

    public void setMaxGenNum(int maxGenNum) {
        this.maxGenNum = maxGenNum;
    }

    public double getPersonRatio() {
        return personRatio;
    }

    public void setPersonRatio(double personRatio) {
        this.personRatio = personRatio;
    }

    public double getGlobalRatio() {
        return globalRatio;
    }

    public void setGlobalRatio(double globalRatio) {
        this.globalRatio = globalRatio;
    }

    public int getParticleNum() {
        return particleNum;
    }

    public void setParticleNum(int particleNum) {
        this.particleNum = particleNum;
    }

    public long getkMax() {
        return kMax;
    }

    public void setkMax(long kMax) {
        this.kMax = kMax;
    }
}

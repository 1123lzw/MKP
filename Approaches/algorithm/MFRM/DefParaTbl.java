package MFRM;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DefParaTbl {
    private    int  popSize;  //种群大小
    private  double Pc; //交叉概率
    private int maxGenNum;//最大迭代次数
    private int  Nc;//变异算子变更代数

    public DefParaTbl(){

    }

    public DefParaTbl(int popSize,double Pc,int maxGenNum,int Nc){
        this.popSize=popSize;
        this.Pc=Pc;
        this.maxGenNum=maxGenNum;
        this.Nc=Nc;

    }
    public DefParaTbl(DefParaTbl fpgaDefParaTbl){
        this.popSize=fpgaDefParaTbl.getPopSize();
        this.Pc=fpgaDefParaTbl.getPc();
        this.maxGenNum=fpgaDefParaTbl.getMaxGenNum();
        this.Nc=fpgaDefParaTbl.getNc();

    }

    public static DefParaTbl getFPGA1DefParaTbl(List<DefPara> defParas) throws IOException, ClassNotFoundException {
        DefParaTbl fpga1DefParaTbl =null;
        //装配HPSOGOAlg特有的参数表
        int  popSize = 0;  //种群大小
        double Pc=0; //交叉系数
        int maxGenNum=0;//最大迭代次数
        int Nc=0;
        for (DefPara aPara:defParas ) {
            String paraName=aPara.getParaName().trim();
            if(paraName.equals("popSize")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                popSize=(Integer)objInt.readObject();
            }
            if(paraName.equals("Pc")){
                ByteArrayInputStream byteDouble= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteDouble);
                Pc=(Double) objInt.readObject();
            }
            if(paraName.equals("maxGenNum")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                maxGenNum=(Integer)objInt.readObject();
            }
            if(paraName.equals("Nc")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                Nc=(Integer)objInt.readObject();
            }
        }
        fpga1DefParaTbl =new DefParaTbl(popSize,Pc,maxGenNum,Nc);
        return fpga1DefParaTbl;
    }

    public List<RunParaConf> getRunParaConfs(String curProbName, int curRunNum) throws IOException {
        List<RunParaConf> runParaConfs=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix=new ObjectOutputStream(bytweightMatrix);
        objweightMatrix.writeObject(popSize);
        byte[] a=bytweightMatrix.toByteArray();

        RunParaConf runParaConf=new RunParaConf("EAHMPAlg",curProbName,curRunNum,"popSize",a);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs2=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix2=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix2=new ObjectOutputStream(bytweightMatrix2);
        objweightMatrix2.writeObject(Pc);
        byte[] b=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("EAHMPAlg",curProbName,curRunNum,"Pc",b);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs3=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix3=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix3=new ObjectOutputStream(bytweightMatrix3);
        objweightMatrix3.writeObject(maxGenNum);
        byte[] c=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("EAHMPAlg",curProbName,curRunNum,"maxGenNum",c);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs4=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix4=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix4=new ObjectOutputStream(bytweightMatrix4);
        objweightMatrix4.writeObject(Nc);
        byte[] d=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("EAHMPAlg",curProbName,curRunNum,"Nc",d);
        runParaConfs.add(runParaConf);
        return  runParaConfs;
    }


    public int getNc() {
        return Nc;
    }

    public void setNc(int nc) {
        Nc = nc;
    }

    public void finalize() throws Throwable {

    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }


    public double getPc() {
        return Pc;
    }

    public void setPc(double pc) {
        Pc = pc;
    }

    public int getMaxGenNum() {
        return maxGenNum;
    }

    public void setMaxGenNum(int maxGenNum) {
        this.maxGenNum = maxGenNum;
    }
}

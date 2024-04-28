package GGA;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DefParaTbl {
    private    int  popSize;  //种群大小
    private int maxFunNum;//最大迭代次数
    private  double pc; //交叉概率
    private  double pm; //变异概率
    private  double pr;//复制概率
    private  double a;//占x1的比例


    public DefParaTbl(){

    }

    public DefParaTbl(int popSize,int maxFunNum,double pc,double pm,double pr,double a){
        this.popSize=popSize;
        this.maxFunNum=maxFunNum;
        this.pc=pc;
        this.pm=pm;
        this.pr=pr;
        this.a=a;
    }
    public DefParaTbl(DefParaTbl ggaDefParaTbl){
        this.popSize=ggaDefParaTbl.getPopSize();
        this.maxFunNum=ggaDefParaTbl.getMaxFunNum();
        this.pc=ggaDefParaTbl.getPc();
        this.pm=ggaDefParaTbl.getPm();
        this.pr=ggaDefParaTbl.getPr();
        this.a=ggaDefParaTbl.getA();

    }

    public static DefParaTbl getGGADefParaTbl(List<DefPara> defParas) throws IOException, ClassNotFoundException {
        DefParaTbl ggaDefParaTbl =null;
        //装配HPSOGOAlg特有的参数表
        int  popSize=0;  //种群大小
        int maxFunNum=0;//最大迭代次数
        double pc=0; //交叉概率
        double pm=0; //变异概率
        double pr=0;//复制概率
        double a=0;//占x1的比例
        for (DefPara aPara:defParas ) {
            String paraName=aPara.getParaName().trim();
            if(paraName.equals("popSize")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                popSize=(Integer)objInt.readObject();
            }
            if(paraName.equals("maxFunNum")){
                ByteArrayInputStream byteDouble= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteDouble);
                maxFunNum=(Integer) objInt.readObject();
            }
            if(paraName.equals("pc")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                pc=(Double) objInt.readObject();
            }
            if(paraName.equals("pm")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                pm=(Double) objInt.readObject();
            }
            if(paraName.equals("pr")){
                ByteArrayInputStream byteDouble= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteDouble);
                pr=(Double) objInt.readObject();
            }
            if(paraName.equals("a")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                a=(Double) objInt.readObject();
            }
        }
        ggaDefParaTbl =new DefParaTbl(popSize,maxFunNum,pc,pm,pr,a);
        return ggaDefParaTbl;
    }

    public List<RunParaConf> getRunParaConfs(String curProbName, int curRunNum) throws IOException {
        List<RunParaConf> runParaConfs=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix=new ObjectOutputStream(bytweightMatrix);
        objweightMatrix.writeObject(popSize);
        byte[] x=bytweightMatrix.toByteArray();

        RunParaConf runParaConf=new RunParaConf("GGAlg",curProbName,curRunNum,"popSize",x);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs2=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix2=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix2=new ObjectOutputStream(bytweightMatrix2);
        objweightMatrix2.writeObject(maxFunNum);
        byte[] b=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("ggaDefParaTbl",curProbName,curRunNum,"macFunNum",b);
        runParaConfs.add(runParaConf);

        List<RunParaConf> runParaConfs3=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix3=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix3=new ObjectOutputStream(bytweightMatrix3);
        objweightMatrix3.writeObject(pc);
        byte[] c=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("ggaDefParaTbl",curProbName,curRunNum,"pc",c);
        runParaConfs.add(runParaConf);

        List<RunParaConf> runParaConfs4=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix4=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix4=new ObjectOutputStream(bytweightMatrix4);
        objweightMatrix4.writeObject(pm);
        byte[] d=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("ggaDefParaTbl",curProbName,curRunNum,"pm",d);
        runParaConfs.add(runParaConf);

        List<RunParaConf> runParaConfs5=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix5=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix5=new ObjectOutputStream(bytweightMatrix5);
        objweightMatrix5.writeObject(pr);
        byte[] e=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("ggaDefParaTbl",curProbName,curRunNum,"pr",e);
        runParaConfs.add(runParaConf);

        List<RunParaConf> runParaConfs6=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix6=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix6=new ObjectOutputStream(bytweightMatrix6);
        objweightMatrix6.writeObject(a);
        byte[] i=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("ggaDefParaTbl",curProbName,curRunNum,"a",i);
        runParaConfs.add(runParaConf);

        return  runParaConfs;
    }

    public void finalize() throws Throwable {

    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public int getMaxFunNum() {
        return maxFunNum;
    }

    public void setMaxFunNum(int maxFunNum) {
        this.maxFunNum = maxFunNum;
    }

    public double getPc() {
        return pc;
    }

    public void setPc(double pc) {
        this.pc = pc;
    }

    public double getPm() {
        return pm;
    }

    public void setPm(double pm) {
        this.pm = pm;
    }

    public double getPr() {
        return pr;
    }

    public void setPr(double pr) {
        this.pr = pr;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }
}

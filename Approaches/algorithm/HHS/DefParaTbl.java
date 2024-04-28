package HHS;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DefParaTbl {
    private int gn;
    private int hms;
    private int L;
    private int s;
    private int maxGenNum;

    public DefParaTbl(){

    }

    public DefParaTbl(int gn,int hms,int L,int s,int maxGenNum){
        this.gn=gn;
        this.hms=hms;
        this.L=L;
        this.s=s;
        this.maxGenNum=maxGenNum;
    }

    public DefParaTbl(DefParaTbl hhsDefParaTbl){
        this.gn=hhsDefParaTbl.getGn();
        this.hms=hhsDefParaTbl.getHms();
        this.L=hhsDefParaTbl.getL();
        this.s=hhsDefParaTbl.getS();
        this.maxGenNum=hhsDefParaTbl.getMaxGenNum();
    }
    public static DefParaTbl getHHSDefParaTbl(List<DefPara> defParas) throws IOException, ClassNotFoundException {
        DefParaTbl hhsDefParaTbl =null;
        //装配HPSOGOAlg特有的参数表
        int gn=0;
        int hms=0;
        int L=0;
        int s=0;
        int maxGenNum=0;
        for (DefPara aPara:defParas ) {
            String paraName=aPara.getParaName().trim();
            if(paraName.equals("gn")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                gn=(Integer)objInt.readObject();
            }
            if(paraName.equals("hms")){
                ByteArrayInputStream byteDouble= new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteDouble);
                hms=(Integer) objInt.readObject();
            }
            if(paraName.equals("L")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                L=(Integer) objInt.readObject();
            }
            if(paraName.equals("s")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                s=(Integer)objInt.readObject();
            }
            if(paraName.equals("maxGenNum")){
                ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) aPara.getValue());
                ObjectInputStream objInt = new ObjectInputStream(byteInt);
                maxGenNum=(Integer) objInt.readObject();
            }
        }
        hhsDefParaTbl =new DefParaTbl(gn,hms,L,s,maxGenNum);
        return hhsDefParaTbl;
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
        objweightMatrix.writeObject(gn);
        byte[] a=bytweightMatrix.toByteArray();

        RunParaConf runParaConf=new RunParaConf("HHSAlg",curProbName,curRunNum,"gn",a);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs2=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix2=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix2=new ObjectOutputStream(bytweightMatrix2);
        objweightMatrix2.writeObject(hms);
        byte[] b=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HHSAlg",curProbName,curRunNum,"hms",b);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs3=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix3=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix3=new ObjectOutputStream(bytweightMatrix3);
        objweightMatrix3.writeObject(L);
        byte[] c=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HHSAlg",curProbName,curRunNum,"L",c);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs4=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix4=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix4=new ObjectOutputStream(bytweightMatrix4);
        objweightMatrix4.writeObject(s);
        byte[] d=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HHSAlg",curProbName,curRunNum,"s",d);
        runParaConfs.add(runParaConf);
        List<RunParaConf> runParaConfs5=new ArrayList<RunParaConf>();
        ByteArrayOutputStream bytweightMatrix5=new ByteArrayOutputStream();
        ObjectOutputStream objweightMatrix5=new ObjectOutputStream(bytweightMatrix5);
        objweightMatrix5.writeObject(maxGenNum);
        byte[] e=bytweightMatrix.toByteArray();
        runParaConf=new RunParaConf("HHSAlg",curProbName,curRunNum,"maxGenNum",e);
        runParaConfs.add(runParaConf);
        return  runParaConfs;
    }

    public void finalize() throws Throwable {

    }

    public int getGn() {
        return gn;
    }

    public void setGn(int gn) {
        this.gn = gn;
    }

    public int getHms() {
        return hms;
    }

    public void setHms(int hms) {
        this.hms = hms;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getMaxGenNum() {
        return maxGenNum;
    }

    public void setMaxGenNum(int maxGenNum) {
        this.maxGenNum = maxGenNum;
    }
}

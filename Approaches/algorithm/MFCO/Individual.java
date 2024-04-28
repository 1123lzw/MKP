package MFCO;

import EAHMP.MKPProb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Individual implements Comparable<Individual>{
    private int[] code;
    MKPProb mkpProb;
    private DefParaTbl curParaTbl=null;//当前的参数表
    protected double fitness;//适度度值
    private Random random=null;


    public Individual(){

    }

    public Individual(Individual newGaIndividual){//对象复制
        this.mkpProb= newGaIndividual.mkpProb;
        this.curParaTbl= newGaIndividual.curParaTbl;//
        this.fitness= newGaIndividual.fitness;//
        this.random= newGaIndividual.random;//
        int[] curCode=new int[newGaIndividual.getCode().length];
        for(int i=0;i<newGaIndividual.getCode().length;i++){
            curCode[i]=newGaIndividual.getCode()[i];
        }
        this.code=curCode;
    }

    public double comFitness(int[] curCode){ //计算适应度值
        double[] value=mkpProb.getValueVectortran();
        int dimension=value.length;
        double curFitness=0;
        for(int i=0;i<dimension;i++){
            curFitness += value[i]*curCode[i];
        }
        return curFitness;
    }

    public  void initGaIndividual(MKPProb prob, DefParaTbl curParaTbl)throws IOException, ClassNotFoundException{  //初始化
        if(random==null){
            random=new Random();
        }
        this.mkpProb=prob;
        this.curParaTbl=curParaTbl;
        int dimension =mkpProb.getValueVectortran().length;//维度
        code=new int[dimension];
        for(int i=0;i<dimension;i++){
            double r=random.nextDouble();
            if(Double.compare(r,0.5)>=0){
                code[i]=1;
            }else
                code[i]=0;
        }
    }


    //根据当前个体进行交叉
    public Individual crossCS(Individual fpga1Individual1,Individual fpga1Individual2) {  //交叉  相同的就确定
        if (random == null) {
            random = new Random();
        }
        int len=code.length;
        int[] newCode1=new int[len];
        int[] newCode2=new int[len];
        for(int i=0;i<len/2;i++){
            newCode1[i]=fpga1Individual1.getCode()[i];
            newCode2[i]=fpga1Individual1.getCode()[len/2+i];
        }
        for(int i=len/2;i<len;i++){
            newCode1[i]=fpga1Individual2.getCode()[i];
            newCode2[i]=fpga1Individual1.getCode()[i-(len/2)];
        }

        repair(newCode1);
        repair(newCode2);
        double fitness1=comFitness(newCode1);
        double fitness2=comFitness(newCode2);
        Individual gaIndividual=new Individual();
        gaIndividual.setMkpProb(mkpProb);
        if(fitness1>fitness2){
            gaIndividual.setCode(newCode1);
            gaIndividual.setFitness(fitness1);
        }else{
            gaIndividual.setCode(newCode2);
            gaIndividual.setFitness(fitness2);
        }
        return gaIndividual;
    }


    /*
    变异  changNum为变异位数
     */

    public void mutate(int changNum){
        if(random==null){
            random = new Random();
        }
        ArrayList<Integer> loation=new ArrayList<>();
        for(int i=0;i<code.length;i++){
            loation.add(i); //记录变异位置
        }
        for(int i=0;i<changNum;i++){
            int ranNum=random.nextInt(loation.size());
            if(code[loation.get(ranNum)]==0){
                code[loation.get(ranNum)]=1;
            }else{
                code[loation.get(ranNum)]=0;
            }
            loation.remove(ranNum);
        }

        repair(code);    //修复
        setCode(code);   //计算适应度值
        setFitness(comFitness(code));  //计算适应度值

    }

    public void repair(int[] code) {  //修复
        double[] value = mkpProb.getValueVectortran();
        Helper particleHelper = new Helper();
        double[][] constraint = particleHelper.transform(mkpProb.getContraintMatrixtran());
        int[] p = particleHelper.Sort3(value, constraint);
        boolean flag = particleHelper.isValid(code, constraint);
        if (flag == false) {
            drop(p, constraint, code);
            add(p,constraint,code);
        } else {
            add(p, constraint, code);
        }
    }

    //修复不满足约束的解
    public void drop(int[] p, double[][] constraint, int[] code) {
        Helper particleHelper = new Helper();
        boolean flag;
        int dimension = p.length;
        for (int i = dimension - 1; i > -1; i--) {
            if (code[p[i]] == 1) {
                code[p[i]] = 0;
            }
            flag = particleHelper.isValid(code, constraint);
            if (flag == true) {
                break;
            }

        }
    }

    //add操作: 按照物品顺序对物品进行增加
    public void add(int[] p, double[][] constraint, int[] code) {
        Helper particleHelper = new Helper();
        boolean flag;
        int dimension = p.length;
        //System.out.println("repair"+Arrays.toString(p));
        for (int i = 0; i < dimension; i++) {
            if (code[p[i]] == 0) {
                code[p[i]] = 1;
                flag = particleHelper.isValid(code, constraint);
                if (flag == false) {
                    code[p[i]] = 0;
                }
            }
        }
    }


    @Override  //逆序排序
    public int compareTo(Individual o) {
        if(this.fitness<o.fitness){
            return 1;
        }else if(this.fitness>o.fitness){
            return -1;
        }
        return 0;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public MKPProb getMkpProb() {
        return mkpProb;
    }

    public void setMkpProb(MKPProb mkpProb) {
        this.mkpProb = mkpProb;
    }

    public DefParaTbl getCurParaTbl() {
        return curParaTbl;
    }

    public void setCurParaTbl(DefParaTbl curParaTbl) {
        this.curParaTbl = curParaTbl;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}

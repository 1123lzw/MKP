package MFPM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual>{

    private int[] code;
    private double fitness;
    private Random random=null;

    public Individual(){

    }
    public Individual(Individual newIndividual){  //对象复制

        this.fitness= newIndividual.fitness;
        int[] newCode=new int[newIndividual.code.length];
        for(int i=0;i<newIndividual.code.length;i++){
            newCode[i]=newIndividual.code[i];
        }
        this.code=newCode;
    }
    public void comfiness(MKPProb solveProb){
        int dimension=code.length;
        double[] value=solveProb.getValueVectortran();
        fitness=0;
        for(int i=0;i<dimension;i++){
            fitness +=code[i]*value[i];
        }
        setFitness(fitness);
    }

    /**
     *
     * @param mpd
     * @param jpd
     */
    public int[] genIndividual(MKPProb solveProb,ExternalArch exa,int[] route){
        exa.binaryMarkov(route);
        double[][] jpd=exa.getJPD();
        double[][] mpd=exa.getMPD();
        if(random==null){
            random=new Random();
        }
        int dimension=solveProb.getValueVectortran().length;
        int[] curCode=new int[dimension];
        Arrays.fill(curCode,0);
        for(int i=0;i<curCode.length;i++){
            if(i==0){
                if(Double.compare(random.nextDouble(),mpd[route[i]][1])<0){
                    curCode[route[i]]=1;
                }
            }else{
                double jpdSum0=jpd[i-1][0]+jpd[i-1][2]; //后一位为0的值
                double jpdSum1=jpd[i-1][1]+jpd[i-1][3];  // 后一位为1的值
                double cpro0=0;
                double cpro1=0;
                double tmpPro;
                if(curCode[route[i-1]]==0){  //前一位为0
//					System.out.println("the befor is 0");
//					System.out.println(jpdSum0+" "+jpdSum1);
                    if(jpdSum1 !=0){
                        cpro1=jpd[i-1][1]/jpdSum1;  //0，1
                    }
                    if (jpdSum0 !=0){
                        cpro0=jpd[i-1][0]/jpdSum0;   //0，0
                    }
                }else{   //前一位为1
//					System.out.println("the befor is 1");
//					System.out.println(jpdSum0+" "+jpdSum1);
                    if(jpdSum1 !=0){
                        cpro1=jpd[i-1][3]/jpdSum1;  //1，1
                    }
                    if (jpdSum0 !=0){
                        cpro0=jpd[i-1][2]/jpdSum0;  //1，0
                    }
                }
                tmpPro=cpro1*mpd[route[i]][1]/(cpro1*mpd[route[i]][1]+cpro0*mpd[route[i]][0]);  //为1的概率
                //System.out.println(tmpPro+" "+cpro0+" "+mpd[route[i]][0]+" "+cpro1+" "+mpd[route[i]][1]);
                if(Double.compare(random.nextDouble(),tmpPro)<0){
                    curCode[route[i]]=1;
                }
            }
        }

        return curCode;
    }

    /**
     *
     * @param solveProb
     * @param curParaTbl
     */
    public void initIndividual(MKPProb solveProb){
        int dimension=solveProb.getValueVectortran().length;
        if(random==null){
            random=new Random();
        }
        code = new int[dimension];
        for(int i=0;i<dimension;i++){
            if (Double.compare(random.nextDouble(),0.5d) > 0){
                code[i] = 1;
            }else{
                code[i] = 0;
            }
        }
    }

    /**
     *
     * @param solveProb
     */
    public void lsIndividual(MKPProb solveProb){   //提升操作
        boolean flag=isLegal(solveProb);
        int dimension=code.length;
        int contraDimension=solveProb.getContraintMatrixtran()[0].length;
        double[][]  contraMatrix=solveProb.getContraintMatrixtran();
        double[]  value=solveProb.getValueVectortran();
        while(flag==true){
            double[] sc=new double[contraDimension];   //每个维度超出约束的值
            for (int i=0;i<contraDimension;i++){
                double w=0;
                for(int j=0;j<dimension;j++){
                    w += code[j]*contraMatrix[j][i];
                }
                sc[i]=contraMatrix[dimension][i]-w;
            }
            List<Integer> Cl =new ArrayList<>();//被取出物品的候选项
            List<Double> dimSum=new ArrayList<>();
            double pMax=0;  //在背包中的物品最大价值
            double dMax=0;   //在背包中维度差值最大值
            for(int i=0;i<dimension;i++){
                if (code[i]==0){
                    code[i]=1;   //单个加入该物品时合法
                    boolean flag2=isLegal(solveProb);
                    if(flag2==true){
                        code[i]=0;    //复原
                        Cl.add(i);
                        if(value[i]>pMax){
                            pMax=value[i];
                        }
                        double dimValue=0;   //计算每个维度的和
                        for(int j=0;j<contraDimension;j++){
                            dimValue +=sc[j]-contraMatrix[i][j];
                        }
                        dimValue +=0.001;
                        dimSum.add(dimValue);
                        if(dimValue>dMax){
                            dMax=dimValue;
                        }
                    }else{  //加入该物品不合法
                        code[i]=0;
                    }
                }
            }
            //System.out.println(Cl+" "+dimSum);
            //System.out.println(pMax+" "+dMax);
            if(Cl.size() !=0){
                double weightMin=Double.MAX_VALUE;  //
                int weightIndex=-1;
                for(int i=0;i<Cl.size();i++){
                    int itemNo=Cl.get(i);
                    double weight=0.5*Math.log(pMax/value[itemNo])+0.5*Math.log(dMax/dimSum.get(i));  //每个在背包的物品的权重值
                    if(weight< weightMin){
                        weightMin=weight;
                        weightIndex=i;
                    }
                }
                //System.out.println(Cl.get(weightIndex));
                code[Cl.get(weightIndex)]=1;
                flag=isLegal(solveProb);
            }
            else{
                break;
            }
        }
        comfiness(solveProb);
    }

    /**
     *
     * @param solveProb
     */
    public int[] randomSampling(MKPProb solveProb){
        if(random==null){
            random=new Random();
        }
        int dimension=solveProb.getValueVectortran().length;
        int[]  route=new int[dimension];
        for(int i=0;i<dimension;i++){
            route[i]=i;
        }
        for(int i=dimension;i>0;i--){
            int rand=random.nextInt(i);
            int temp=route[rand];
            route[rand]=route[i-1];
            route[i-1]=temp;
        }
        return route;
    }

    /**
     * 修复不可行解,每次删除一个物品后要重新计算
     * @param solveProb
     */
    public void roIndividual(MKPProb solveProb){  //修复操作
        boolean flag=isLegal(solveProb);
        int dimension=code.length;
        int contraDimension=solveProb.getContraintMatrixtran()[0].length;
        double[][]  contraMatrix=solveProb.getContraintMatrixtran();
        double[]  value=solveProb.getValueVectortran();
        while(flag==false){
            double[] ol=new double[contraDimension];   //每个维度超出约束的值
            for (int i=0;i<contraDimension;i++){
                double w=0;
                for(int j=0;j<dimension;j++){
                    w += code[j]*contraMatrix[j][i];
                }
                ol[i]=w-contraMatrix[dimension][i];
            }
            List<Integer> Cr =new ArrayList<>();//被取出物品的候选项
            List<Double> dimSum=new ArrayList<>();
            double pMax=0;  //在背包中的物品最大价值
            double dMax=0;   //在背包中维度差值最大值
            for(int i=0;i<dimension;i++){
                if (code[i]==1){
                    Cr.add(i);
                    double dimValue=0;  //计算每一个维度的和
                    if(value[i]>pMax){
                        pMax=value[i];
                    }
                    for(int j=0;j<contraDimension;j++){
                        double max=0.001;
                        double dis=ol[j]-contraMatrix[i][j];
                        if(dis>max)
                            max=dis;
                        dimValue +=max;
                    }
                    dimSum.add(dimValue);
                    if(dimValue>dMax){
                        dMax=dimValue;
                    }
                }
            }

            double weightMax=-1;  //一定大于0
            int weightIndex=-1;
            for(int i=0;i<Cr.size();i++){
                int itemNo=Cr.get(i);
                double weight=0.5*Math.log(pMax/value[itemNo])+0.5*Math.log(dMax/dimSum.get(i));  //每个在背包的物品的权重值
                if(weight>weightMax){
                    weightMax=weight;
                    weightIndex=i;
                }
            }

            code[Cr.get(weightIndex)]=0;
            flag=isLegal(solveProb);
        }
        comfiness(solveProb);
    }


    public boolean isLegal(MKPProb solveProb){
        boolean flag=true;
        int dimension = code.length;
        double[][] contraMatrix=solveProb.getContraintMatrixtran();
        for(int j=0;j<contraMatrix[0].length;j++){
            double weight=0;
            for(int i=0;i<contraMatrix.length-1;i++){
                weight+=contraMatrix[i][j]*code[i];
            }
            if(contraMatrix[dimension][j]<weight){
                flag=false;
                break;
            }
        }
        return flag;
    }

    @Override
    public int compareTo(Individual o) {
        if(this.fitness<o.fitness){
            return 1;   //逆序 交换
        }else if(this.fitness>o.fitness){
            return -1;  //顺序
        }
        return 0;
    }
    public void mutate(int changNum,MKPProb mkpProb){
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

        repair(code,mkpProb);    //修复
        setCode(code);   //计算适应度值
        comfiness(mkpProb);  //计算适应度值

    }

    public void repair(int[] code,MKPProb mkpProb) {  //修复
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




    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public double getFitness() {
        return fitness;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    public void finalize() throws Throwable {

    }
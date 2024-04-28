package HPSOGO;

import java.io.IOException;
import java.util.Random;

public class Particle {
    MKPProb mkpProb;
    private  DefParaTbl curParaTbl=null;//HPSOGOAlg当前的参数表
    private int[] positions;//粒子位置
    private double[] speeds;//速度
    protected double fitness;//适度度值
    private String constrainStr ="";//约束函数值构成的字符串，形如[v1,v2,..,vn]
    private Random random=null;
    private double objValue=0;
    public Particle(){

    }

    public double evaObjValue(int[] curPosition) {
        int[] positions=curPosition;
        objValue=0;
        double[] valueVector= mkpProb.getValueVectortran();
        for (int i=0;i<positions.length;i++){
            this.objValue+=positions[i]*valueVector[i];
        }
        return objValue;
    }

    public Particle(Particle newParticle){//对象复制
        this.mkpProb= newParticle.mkpProb;
        this.curParaTbl= newParticle.curParaTbl;//
//		this.positions = newParticle.positions;//
//		this.speeds = newParticle.speeds;//
        this.objValue=newParticle.getObjValue();
        this.fitness= newParticle.fitness;//
        this.constrainStr = newParticle.constrainStr;//
        this.random= newParticle.random;//
        int[] curPosition=new int[newParticle.getPositions().length];
        for(int i=0;i<newParticle.getPositions().length;i++){
            curPosition[i]=newParticle.getPositions()[i];
        }
        this.positions=curPosition;

        double[] curSpeeds=new double[newParticle.getSpeeds().length];
        for(int i=0;i<newParticle.getSpeeds().length;i++){
            curSpeeds[i]=newParticle.getSpeeds()[i];
        }
        this.speeds=curSpeeds;
    }

    /**
     *
     * @param prob
     */
    public void initialize(MKPProb prob, DefParaTbl curParaTbl) throws IOException, ClassNotFoundException {
        if(random==null){
            random=new Random();
        }
        this.mkpProb=prob;
//		ByteArrayInputStream byteInt = new ByteArrayInputStream((byte[]) prob.getValueVector());
//		ObjectInputStream objInt = new ObjectInputStream(byteInt);
        //     System.out.println((List<Double>)objInt.readObject()+"向量");
        int dimension =mkpProb.getValueVectortran().length;//粒子维度
        //this.dimension = dimension;
        this.curParaTbl=curParaTbl;//HPSOGOAlg当前的参数表
        //初始化速度
        speeds=new double[dimension];
        for (int i=0;i<dimension;i++){
            speeds[i] = random.nextDouble() * 10 - 5;
        }
        //初始化位置
        positions=new int[dimension];
        for (int j=0;j<dimension;j++){
            if (Double.compare(random.nextDouble(),0.5d) > 0){
                positions[j] = 1;
            }else{
                positions[j] = 0;
            }
        }
    }

    /**
     *
     * @param pBest
     * @param gBest
     * @param pRand
     * @param gRand
     */
    public double[] speedUpdate(Particle pBest, Particle gBest, double pRand, double gRand){
        //要更新本类中speed变量的值   只关心当前粒子的速度算是一维向量

        int size=speeds.length;
        double[] curSpeeds=new double[size];


        double c1=2.05;
        double c2=2.05;
        int [] pBestPosition = pBest.getPositions();
        int [] gBestPosition = gBest.getPositions();
        int dimension =speeds.length;
        for ( int i = 0;i<dimension;i++){
            curSpeeds[i] = 0.729*(speeds[i]
                    +c1*pRand*(pBestPosition[i]-positions[i])
                    +c2*gRand*(gBestPosition[i]-positions[i]));

        }
        setSpeeds(curSpeeds);
        return curSpeeds;
    }

    /**
     *  @param pBest
     * @param gBest
     * @param pRand
     * @param gRand
     * @return
     */
    public int[] locationUpdate(){
        if(random==null){
            random=new Random();
        }
        int dimension=speeds.length;
        for (int i=0;i<dimension;i++){
            double sigmoid = (1/(1+(Math.exp(-speeds[i]))));
            if (Double.compare(random.nextDouble(),sigmoid)<0){
                positions[i] = 0;
            }else {
                positions[i] =1;
            }
        }
        setPositions(positions);
        return positions;
    }

    public void updateFitness() throws IOException, ClassNotFoundException{
        //////1.计算惩罚函数值,
        // 2.构造形如[v1,v2,..,vn]的约束条件字符串constraints,
        // 3.计算适应度函数
        double fitness = 0;
        double[] valueVector=mkpProb.getValueVectortran();
        for (int i=0;i<valueVector.length;i++){
            fitness+=positions[i]*valueVector[i];
        }
        String constraintStr="[";

        double constrains [][] = mkpProb.getContraintMatrixtran();
        double kMax = curParaTbl.getkMax();
        //计算惩罚函数
        double[] penalty = new double[constrains[0].length];
        int row=constrains.length; //constrainsMatrix的行
        int columns=constrains[0].length;//constrainsMatrix的列
        double[] limit = new double[columns];
        for (int i=0;i<columns;i++){
            limit[i]=constrains[row-1][i];
        }
        for (int i=0;i<columns;i++){
            for (int j=0;j<row-1;j++){
                penalty[i] =limit[i]-constrains[j][i]*positions[j];
                limit[i]=penalty[i];
            }
            if(Double.compare(penalty[i],0)<0){
                penalty[i] =0;
            }
            if(i==columns-1){
                constraintStr+=penalty[i]+"]";
            }
            else{
                constraintStr+=penalty[i]+",";
            }
            fitness+=kMax*penalty[i];
        }
        this.fitness=fitness;
        this.constrainStr=constraintStr;
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

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int[] getPositions() {
        return positions;
    }

    public void setPositions(int[] positions) {
        this.positions = positions;
    }

    public double[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(double[] speeds) {
        this.speeds = speeds;
    }

    public void setCurParaTbl(DefParaTbl curParaTbl) {
        this.curParaTbl = curParaTbl;
    }

    public String getConstrainStr() {
        return constrainStr;
    }
    public void setConstrainStr(String constrainStr) {
        this.constrainStr = constrainStr;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
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

    public double getObjValue() {
        return objValue;
    }

    public void setObjValue(double objValue) {
        this.objValue = objValue;
    }
}

package GGA;

import java.io.IOException;
import java.util.*;

public class Population {
    private DefParaTbl curParaTbl = null;//当前的参数表
    private List<Individual> gaIndividuals;
    private  Individual bestIndividual;
    private  List<Integer> x1;
    private MKPProb mkpProb = null;
    private Random random = null;
    private  List<Individual> newIndividuals;


    public DefParaTbl getCurParaTbl() {
        return curParaTbl;
    }

    public  int[] comSort(){   //将物品排序
        int dimension=mkpProb.getValueVectortran().length;
        double[] value=mkpProb.getValueVectortran();
        double[][] contra=mkpProb.getContraintMatrixtran();
        double[] e=new double[dimension];
        for(int i=0;i<dimension;i++){
            double sum=0;
            for(int j=0;j<contra[0].length;j++){
                sum += contra[i][j];
            }
            e[i]=value[i]/sum;
        }
        int[] sortItem=new int[dimension];
        for(int i=0;i<dimension;i++){
            sortItem[i]=i;
        }

        for(int i=0;i<dimension-1;i++){
            for(int j=0;j<dimension-i-1;j++){
                if(e[j]<e[j+1]) {
                    double dTemp = e[j];
                    e[j] = e[j + 1];
                    e[j + 1] = dTemp;

                    int iTemp = sortItem[j];
                    sortItem[j] = sortItem[j + 1];
                    sortItem[j + 1] = iTemp;
                }
            }
        }
        return sortItem;
    }

    public void initX1(){
        int[] itemSort=comSort();
        x1=new ArrayList<>();
        int dimenison=itemSort.length;
        GGAHelper ggAhelper=new GGAHelper();
        double[][] contra=ggAhelper.transform(mkpProb.getContraintMatrixtran());
        int[] curCode=new int[dimenison];
        for(int i=0;i<dimenison;i++){
            curCode[itemSort[i]]=1;
            boolean flag=ggAhelper.isValid(curCode,contra);
            if(flag==true){
                x1.add(itemSort[i]);
            }else{
                if(x1.size()!=0){
                    break;
                } else{
                    curCode[itemSort[i]]=0;
                }
            }
        }

    }

    public void initGgaPopulation(MKPProb solveProb, DefParaTbl curParaTbl) throws IOException, ClassNotFoundException {
        this.mkpProb = solveProb;
        this.curParaTbl = curParaTbl;//
        gaIndividuals = new ArrayList<>();//种群中的个体
        bestIndividual = new Individual();
        int particleNum =500;
        double bestFitness = Double.MIN_VALUE;
        initX1();
        for(int i=0;i<particleNum;i++){
            Individual ggaIndividual=new Individual();
            ggaIndividual.initGaIndividual(mkpProb,curParaTbl,x1); //初始化
            ggaIndividual.repair(ggaIndividual.getCode()); //修复
            ggaIndividual.setFitness(ggaIndividual.comFitness(ggaIndividual.getCode()));//计算适应度值函数
            gaIndividuals.add(ggaIndividual); //加入种群
            double curFitness=ggaIndividual.getFitness();
            if(curFitness>bestFitness){
                bestFitness=curFitness;
                Individual curGgaIndividual=new Individual(ggaIndividual);
                bestIndividual=curGgaIndividual;
            }

        }

        setGaIndividuals(gaIndividuals);
        setBestIndividual(bestIndividual);

    }

    public Individual select(){  //选择操作
        double bestFitness=Double.MIN_VALUE;
        if(random==null){
            random=new Random();
        }
        int index=-1;
        Set<Integer> set=new HashSet<>();
        int count=0;
        while(count<5){
            int randNum=random.nextInt(gaIndividuals.size());

            if( !set.contains(randNum)){
                set.add(randNum);
                count++;
            }
        }
        for(int number:set){
            double curFitness=gaIndividuals.get(number).getFitness();
            if(bestFitness<curFitness){
                bestFitness=curFitness;
                index=number;
            }
        }
        return gaIndividuals.get(index);
    }


    public void crossPopulation(){  //生成新的种群
        if(random==null){
            random=new Random();
        }
        newIndividuals =new ArrayList<>();
        double pc=0.2;
        for(int i=0;i<gaIndividuals.size();i++){
            double rand1=random.nextDouble();
            if(rand1<pc) {//交叉
                Individual curGgaIndividual = new Individual();
                curGgaIndividual.setCurParaTbl(curParaTbl);
                curGgaIndividual.setMkpProb(mkpProb);
                Individual ggaIndividual1 = select();
                Individual ggaIndividual2 = select();
                curGgaIndividual.cross(ggaIndividual1.getCode(), ggaIndividual2.getCode()); //交叉生成新个体
                newIndividuals.add(curGgaIndividual);
            } else{//不交叉
                newIndividuals.add(select());  //新种群
            }
        }

    }

    public void mutatePopulation() {
        double pm=curParaTbl.getPm();
        for(int i=0;i<newIndividuals.size();i++){
            double rand2=random.nextDouble();

            if(rand2<pm){
                newIndividuals.get(i).mutate();
            }
            newIndividuals.get(i).repair(newIndividuals.get(i).getCode());
            newIndividuals.get(i).setFitness(newIndividuals.get(i).comFitness(newIndividuals.get(i).getCode()));//计算适应度值函数

            newIndividuals.set(i,newIndividuals.get(i));
        }
    }


    public  void updatePoplation(){ //保留种群中最好的五个个体  pr
        if(random==null){
            random=new Random();
        }
       double pr=curParaTbl.getPr();

        int size=newIndividuals.size();
        Collections.sort(newIndividuals);
        Collections.sort(gaIndividuals);
        double rand=random.nextDouble();
        if(rand<pr) {
            for (int i = 0; i < 5; i++) {
                Individual curBest = new Individual(getGaIndividuals().get(i));
                newIndividuals.set(size - i - 1, curBest);
            }
        }else{//保留最好个体
            Individual curBest = new Individual(getGaIndividuals().get(0));//旧种群中的最优个体
            //System.out.println(Arrays.toString(curBest.getCode())+"curBest"+curBest.getFitness());
            newIndividuals.set(size - 1, curBest);
        }
        for(int i=0;i<newIndividuals.size();i++){
            Individual curGGa=new Individual(newIndividuals.get(i));
            gaIndividuals.set(i,curGGa);
        }
        setGaIndividuals(gaIndividuals); //更新种群
    }

    public void upDateBestIndividual() { //更新种群中最好值
        int popSize = gaIndividuals.size();
        double bestVal = bestIndividual.getFitness();
        int index = -1; //记录最好的下标
        for (int i = 0; i < popSize; i++) {
            if (gaIndividuals.get(i).getFitness() > bestVal) {
                index = i;
                bestVal = gaIndividuals.get(i).getFitness();
            }
        }

        if (index != -1) {
            Individual gaIndividual = new Individual(gaIndividuals.get(index));
            setBestIndividual(gaIndividual);
        }

    }




    public void setCurParaTbl(DefParaTbl curParaTbl) {
        this.curParaTbl = curParaTbl;
    }

    public List<Individual> getGaIndividuals() {
        return gaIndividuals;
    }

    public void setGaIndividuals(List<Individual> gaIndividuals) {
        this.gaIndividuals = gaIndividuals;
    }

    public Individual getBestIndividual() {
        return bestIndividual;
    }

    public void setBestIndividual(Individual bestIndividual) {
        this.bestIndividual = bestIndividual;
    }

    public List<Integer> getX1() {
        return x1;
    }

    public void setX1(List<Integer> x1) {
        this.x1 = x1;
    }

    public MKPProb getMkpProb() {
        return mkpProb;
    }

    public void setMkpProb(MKPProb mkpProb) {
        this.mkpProb = mkpProb;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}

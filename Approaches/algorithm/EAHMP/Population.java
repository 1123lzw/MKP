package EAHMP;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population {

    private DefParaTbl curParaTbl = null;//当前的参数表
    private List<Individual> gaIndividuals;
    private Individual bestIndividual;

    private MKPProb mkpProb = null;
    private Random random = null;

    int changeNum;
    int N;


    public Population() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 初始化：找到最好的个体
     *
     * @param solveProb
     * @param curParaTbl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void initGaPopulation(MKPProb solveProb, DefParaTbl curParaTbl) throws IOException, ClassNotFoundException {
        this.mkpProb = solveProb;
        this.curParaTbl = curParaTbl;//
        gaIndividuals = new ArrayList<Individual>();//粒子群
        bestIndividual = new Individual();
        //int particleNum = curParaTbl.getPopSize();
        int particleNum=50;
        N=0;
        changeNum=1;
        double bestFitness = Double.MIN_VALUE;
        for (int i = 0; i < particleNum; i++) {
            //初始化群中一个粒子
            Individual curgaIndividual= new Individual();
            curgaIndividual.initGaIndividual(solveProb, curParaTbl);  //初始化
            curgaIndividual.repair(curgaIndividual.getCode()); //修复
            curgaIndividual.setFitness(curgaIndividual.comFitness(curgaIndividual.getCode())); //计算适应度值
            gaIndividuals.add(curgaIndividual);
            ///找最优的个体
            double curFitness = curgaIndividual.getFitness();
            if (Double.compare(curFitness, bestFitness) > 0) {
                bestFitness = curFitness;
                bestIndividual = new Individual(curgaIndividual);
            }
        }

    }






    /**
     * 更新最优解
     */


    public void upDateBestGaIndividual() {
        int popSize = gaIndividuals.size();
        double bestVal = bestIndividual.getFitness();
        //int Nc=curParaTbl.getNc();
        int Nc=10;
        int index = -1; //记录最好的下标
        for (int i = 0; i < popSize; i++) {
            if (gaIndividuals.get(i).getFitness() > bestVal) {
                index = i;
                bestVal = gaIndividuals.get(i).getFitness();
            }
        }
        if (index != -1) {  //最好值有变化
            Individual gaIndividual = new Individual(gaIndividuals.get(index));
            setBestIndividual(gaIndividual);
            N=0;
        }
        else{ //最好值无变化
            N=N+1;
            if(N%Nc==0){
                mutatePopution();
            }
        }

    }


    public void  genPoplation(FPTree fpgafpTree){
        if (random == null) {
            random = new Random();
        }
        int size=gaIndividuals.size();
        ArrayList<Individual> newIndividuals = new ArrayList<Individual>();
        double pc=0.9;
        for(int i=0;i<size;i++){
            double rand1 =random.nextDouble();
            if(rand1<pc){
                Individual newGaIndividual=gaIndividuals.get(i).cross(fpgafpTree);
                newIndividuals.add(newGaIndividual);
            }
        }
        stateReplacement(newIndividuals);
        upDateBestGaIndividual();
    }



    public void  mutatePopution() {
        int index = -1;
        double bestVal=bestIndividual.getFitness();
        for (int i = 0; i < gaIndividuals.size(); i++) {
            Individual fpga1Individual = new Individual(gaIndividuals.get(i));
            fpga1Individual.mutate(changeNum);
            if (fpga1Individual.getFitness() > gaIndividuals.get(i).fitness) { //比之前更好会保留
                gaIndividuals.set(i, fpga1Individual);
                if(gaIndividuals.get(i).getFitness()>bestVal) {
                    index=i;
                }
            }
        }
        if(index !=-1) { //更新最好值N为0，changNum=1；
            Individual gaIndividual = new Individual(gaIndividuals.get(index));
            setBestIndividual(gaIndividual);
            N = 0;
            changeNum=1;
        }else{ //未更新 增加变异程度
            if(changeNum==bestIndividual.getCode().length){
                changeNum=1;
            }else{
                changeNum=changeNum+1;
            }

        }
    }




    public void stateReplacement(ArrayList<Individual> newIndividuals){  //使用新种群中的个体替换最差的个体
        for(int i=0;i<newIndividuals.size();i++){
            int worstIndex = 0;
            double worstFitness = gaIndividuals.get(0).getFitness();
            for (int j = 1; j < gaIndividuals.size(); j++) {
                double currentFitness = gaIndividuals.get(j).getFitness();
                if (currentFitness < worstFitness) {
                    worstIndex = j;
                    worstFitness = currentFitness;
                }
            }
            if (newIndividuals.get(i).getFitness() > worstFitness) { //大于种群中最差解
                Boolean flag=true;
                for(int j=0;j<gaIndividuals.size();j++){  //先判断和种群中是否有相似
                    if(Arrays.equals(gaIndividuals.get(j).getCode(),newIndividuals.get(i).getCode())){
                        flag=false;
                        break;
                    }
                }
                if(flag==true){
                    Individual gaIndividual=new Individual(newIndividuals.get(i));
                    gaIndividuals.set(worstIndex,gaIndividual);
                }
            }
        }
        setGaIndividuals(gaIndividuals);
    }



    public DefParaTbl getCurParaTbl() {
        return curParaTbl;
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

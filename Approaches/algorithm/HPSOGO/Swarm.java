package HPSOGO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Swarm {
    private DefParaTbl curParaTbl=null;//HPSOGOAlg当前的参数表
    public List<Particle> fruitFlies;//种群所含粒子 //modify public
    //private BestParticles bestParticles;//个体和社会的最优粒子
    private List<Particle> pBests;
    private  Particle gBest;
    //private int particleNum=0;//种群粒子数
    private MKPProb mkpProb=null;
    private Random random=null;
    @Autowired
    private HPSOGOAlg hpsogoAlg;//modify

    public Swarm(){

    }

    /**
     *
     * @param solveProb
     * @param
     * //@param runNum
     */
    public void initSwarm(MKPProb solveProb, DefParaTbl curParaTbl) throws IOException, ClassNotFoundException {
        this.mkpProb=solveProb;
        this.curParaTbl=curParaTbl;//HPSOGOAlg当前的参数表
        fruitFlies =new ArrayList<Particle>();//粒子群
        pBests=new ArrayList<>();
        gBest=new Particle();
//		int particleNum=curParaTbl.getParticleNum();
        int particleNum=150;
        double bestFitness=Double.MIN_VALUE;
        Particle bestParticle =null;
        for(int i=0;i<particleNum;i++){
            //初始化群中一个粒子
            Particle curParticle =new Particle();
            curParticle.initialize(solveProb,curParaTbl); //初始化
            //curParticle.updateFitness();
            curParticle.repair(curParticle.getPositions()); //修复
            curParticle.evaObjValue(curParticle.getPositions()); //计算适应度值
            fruitFlies.add(curParticle);
            /////处理pBest数组
            Particle newParticle =new Particle(curParticle);
            pBests.add(newParticle);
            ///找最优的粒子
            double curFitness= curParticle.getObjValue();
            if(Double.compare(curFitness,bestFitness)>0){
                bestFitness=curFitness;
                bestParticle = curParticle;
            }
            //处理全局最优粒子
        }
        gBest=new Particle(bestParticle);
        setpBests(pBests);
        setgBest(gBest);
        setParticles(fruitFlies);
        //syPersonalBestParticles();
    }

    /**
     *
     * @param pBests
     * @param gBest
     * @param pRand
     * @param gRand
     */
    public void updateLoc(List<Particle> pBests, Particle gBest){
        int particleNum=this.fruitFlies.size();
        if(random==null){
            random=new Random();
        }
        for(int i=0;i<particleNum;i++){
            double pRand=random.nextDouble();
            double gRand=random.nextDouble();
            fruitFlies.get(i).speedUpdate(pBests.get(i),gBest,pRand,gRand);  //更新速度
            fruitFlies.get(i).locationUpdate(); //更新位置
        }
    }

    public void updatePersonalBestParticles(){
        int particleNum=this.fruitFlies.size();
        for(int i=0;i<particleNum;i++){
            Particle curParticle = fruitFlies.get(i);
            Particle oldParticle =pBests.get(i);
            double curFitness= curParticle.getObjValue();
            double oldFitness= oldParticle.getObjValue();
            if(Double.compare(curFitness,oldFitness)>0){
                //System.out.println(i+" "+"before"+oldFitness+"after"+oldFitness);
                Particle newBestParticle = new Particle(curParticle);//复制粒子
                pBests.set(i, newBestParticle);
            }
        }
        setpBests(pBests);
    }

    public void syPersonalBestParticles(){
        int particleNum=this.fruitFlies.size();
        for(int i=0;i<particleNum;i++){
            Particle curParticle = fruitFlies.get(i);
            Particle oldParticle =pBests.get(i);
            double curFitness= curParticle.getObjValue();
            double oldFitness= oldParticle.getObjValue();
            System.out.println(i+" "+"before"+oldFitness+"after"+curFitness);
            Particle newBestParticle = new Particle(curParticle);//复制粒子
            pBests.set(i, newBestParticle);
        }
    }



    public void updateGlobalParticle(){
        int size= pBests.size();
        int bestIdx=-1;
        double bestVal= gBest.getObjValue();
        for(int i=0;i<size;i++){
            Particle curParticle = pBests.get(i);
            double curFitness= curParticle.getObjValue();
            if(Double.compare(curFitness,bestVal)>0){
                bestVal=curFitness;
                bestIdx=i;
            }
        }
        if(bestIdx !=-1){
            Particle newParticle =new Particle(pBests.get(bestIdx));//复制粒子
            setgBest(newParticle);
        }

    }




    public void updateFitness() throws IOException, ClassNotFoundException {
        for (Particle curParticle : fruitFlies) {
            curParticle.evaObjValue(curParticle.getPositions());

        }
    }

    //粒子变异
    public void mutate(){
        mutateByPBest();
        mutateByGBest();
        for(int i=0;i<fruitFlies.size();i++){
            fruitFlies.get(i).repair(fruitFlies.get(i).getPositions());
        }
    }

    public void mutateByPBest(){
        if(random==null){
            random=new Random();
        }
        int particleNum=this.fruitFlies.size();

        int dimention= pBests.get(0).getPositions().length;
        //按pbest随机变异
        for(int i=0;i<particleNum;i++){
            Particle curParticles= fruitFlies.get(i);
            Particle pbest= pBests.get(i);
            int mutNum=(int)(random.nextDouble()*(dimention+1));//随机变异次数0~dimention
            int[] gene=curParticles.getPositions();
            if(mutNum==0) {
                curParticles.setPositions(gene);
            } else{
                for(int j=0;j<mutNum;j++){
                    int mutPositions=random.nextInt(dimention);//随机位置0~dimention-1
                    gene[mutPositions]=pbest.getPositions()[mutPositions];
                }
            }
            curParticles.setPositions(gene);
        }
    }

    public void mutateByGBest(){
        if(random==null){
            random=new Random();
        }
        int particleNum=this.fruitFlies.size();
        int dimention=gBest.getPositions().length;
        //按gbest随机变异
        for(int i=0;i<particleNum;i++){
            Particle curParticles= fruitFlies.get(i);
            int mutNum=(int)(random.nextDouble()*dimention+1);
            int[] gene=curParticles.getPositions();
            if(mutNum==0){
                curParticles.setPositions(gene);
            } else{
                for(int j=0;j<mutNum;j++){
                    int mutPositions=(int)(random.nextDouble()*dimention);
                    gene[mutPositions]=gBest.getPositions()[mutPositions];
                }
            }
            curParticles.setPositions(gene);
        }
    }

    public List<Particle> getParticles(){
        return this.fruitFlies;
    }

    public List<Particle> getpBests() {
        return pBests;
    }

    public void setpBests(List<Particle> pBests) {
        this.pBests = pBests;
    }

    public Particle getgBest() {
        return gBest;
    }

    public void setgBest(Particle gBest) {
        this.gBest = gBest;
    }

    public void setParticles(List<Particle> fruitFlies) {
        this.fruitFlies = fruitFlies;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}

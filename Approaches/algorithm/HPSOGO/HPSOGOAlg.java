package HPSOGO;

import java.io.IOException;
import java.util.*;

public class HPSOGOAlg {
    private Swarm swarm;
    private DefParaTbl hpsogDefParaTbl;//HPSOGOAlg缺省的参数表
    private DefParaTbl hpsogCurParaTbl;//HPSOGOAlg当前的参数表
    private int curRunID;//從數據庫讀取當前的運行次數

    private Timer timer;
    private  boolean stopFlag;

    @Autowired
    private BestParticles bestParticles= null;
	/*public HPSOGOAlg(){
		super("HPSOGOAlg");
		List<DefPara> defParas=getDefParas("HPSOGOAlg");
		hpsogDefParaTbl =HPSOGDefParaTbl.getHPSOGDefParaTbl(defParas);
		hpsogCurParaTbl=new HPSOGDefParaTbl(hpsogDefParaTbl);//复制参数表
	}*/

    public void init() throws IOException, ClassNotFoundException {
        List<DefPara> defParas=getDefParas("HPSOGOAlg");
        hpsogDefParaTbl =DefParaTbl.getHPSOGDefParaTbl(defParas);
        hpsogCurParaTbl=new DefParaTbl(hpsogDefParaTbl);//复制参数表
    }

    public void addRunRcd(String probName) throws IOException {
        List<RunParaConf> runParaConfs = hpsogCurParaTbl.getRunParaConfs(probName, -1);
        Long curTime = System.currentTimeMillis();
        AlgRunRcd aAlgRunRcd = new AlgRunRcd("HPSOGOAlg", probName, -1, curTime, curTime);
        curRunID=super.addAlgRunRcd(runParaConfs, aAlgRunRcd);
    }



    /**
     *
     * @param problem
     */
    public void run(MKPProb problem,double time) throws IOException, ClassNotFoundException {
        //如不改参数，即按缺省参数运行算法，可按下面代码
        problem.transconstrains();
        problem.transVector();
        String probName=problem.getProbName();
        addRunRcd(probName);
        /////////////
//		int maxGenNum= hpsogCurParaTbl.getMaxGenNum();
        int maxGenNum= 5000;
        //int particleNum=hpsogCurParaTbl.getParticleNum();
        // 产生随机粒子群

        TimerTask stopTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Timeout reached. Stopping loop...");
                stopFlag = true;  // 设置停止标志
            }
        };

        timer = new Timer();
        double delayInSeconds = time;
        long delayInMillis = (long) (delayInSeconds * 1000);
        timer.schedule(stopTask, delayInMillis);
        stopFlag = false;
        swarm.initSwarm(problem,hpsogDefParaTbl);
        Random random=new Random();
        List<Particle> pBests= swarm.getpBests();
        Particle gBest=swarm.getgBest();
        addGenBestInfo(probName,0,gBest);
        int gen=1;
//		for(int gen=1;gen<=maxGenNum;gen++){//算法主体
        while(!stopFlag){
            //更新粒子群
            swarm.updateLoc(pBests,gBest);
            //变异
            swarm.mutate();
            swarm.updateFitness();
            //更新个体和全局最优
            swarm.updatePersonalBestParticles();
            swarm.updateGlobalParticle();
            pBests=swarm.getpBests();
            gBest=swarm.getgBest();
            //注意发布每代最優解
            addGenBestInfo(probName,gen,gBest);
            gen=gen+1;
        }
        Long curTime = System.currentTimeMillis();
        stopRun("HPSOGOAlg",probName, curRunID,curTime);
        curRunID++;
    }


    /**
     *
     * @param probName
     * @param genNum
     * @param curGenBestParticle
     */
    public void addGenBestInfo(String probName, int genNum, Particle curGenBestParticle){
        String algName="HPSOGOAlg";

        int[] codeIntArray= curGenBestParticle.getPositions();
        int len=codeIntArray.length;
        String code="[";
        for(int i=0;i<len;i++){
            if(i!=len-1) {
                code += codeIntArray[i] + ",";
            }
            else{
                code+=codeIntArray[i]+"]";
            }
        }
        double objValue= curGenBestParticle.evaObjValue(codeIntArray);
        String constrains= curGenBestParticle.getConstrainStr();
        long outputTime=System.currentTimeMillis();
        GeneResult geneBestResult=new GeneResult(algName,probName, curRunID,genNum,code,objValue,constrains,outputTime);
        super.addGenBestInfo(geneBestResult);
    }

    @Override
    public List<DefPara> getDefParas(String algName) {
        QueryWrapper<DefPara> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("alg_name",algName);
        List<DefPara> defParas=new ArrayList<>();
        //施文华给出调用DAO中的实现findDefParas(algName)
        defParas=defParaDao.selectList(queryWrapper);
        return defParas;
    }

    public void setBestParticles(BestParticles bestParticles) {
        this.bestParticles = bestParticles;
    }

    public BestParticles getBestParticles() {
        return bestParticles;
    }
}

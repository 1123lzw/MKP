package EAHMP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Alg1 {

    private Population population;
    private DefParaTbl fpgaDefParaTbl;//缺省的参数表
    private DefParaTbl fpgaCurParaTbl;//当前的参数表
    private int curRunID;
    private Timer timer;
    private  boolean stopFlag;


    public Alg1(){

    }


    public void init() throws IOException, ClassNotFoundException {
        List<DefPara> defParas=getDefParas("EAHMPAlg");
        fpgaDefParaTbl = DefParaTbl.getFPGA1DefParaTbl(defParas);
        fpgaCurParaTbl=new DefParaTbl(fpgaDefParaTbl);//复制参数表
    }
    public void addRunRcd(String probName) throws IOException {
        List<RunParaConf> runParaConfs = fpgaCurParaTbl.getRunParaConfs(probName, -1);
        Long curTime = System.currentTimeMillis();
        AlgRunRcd aAlgRunRcd = new AlgRunRcd("EAHMPAlg", probName, -1, curTime, curTime);
        curRunID=super.addAlgRunRcd(runParaConfs, aAlgRunRcd);
    }

    /**
     *
     * @param problem
     */
    public void run(MKPProb problem,Double time) throws IOException, ClassNotFoundException {
        //如不改参数，即按缺省参数运行算法，可按下面代码
        population=new Population();
        problem.transconstrains();
        problem.transVector();
        String probName=problem.getProbName();

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
        population.initGaPopulation(problem,fpgaCurParaTbl);
        int gen=1;
        Long curTime = System.currentTimeMillis();

        while(!stopFlag ){
            //变异，交叉和选择
            FPTree fpTree=new FPTree();
            fpTree.buildInitiFPTree(population.getGaIndividuals(),4,problem);  //全部个体构建树
            population.genPoplation(fpTree);
            curTime = System.currentTimeMillis();

            //System.out.println(gen);
            //注意发布每代最優解
            addGenBestInfo(probName,gen,population.getBestIndividual());
            gen=gen+1;
        }
        stopRun("EAHMPAlg",probName, curRunID,curTime);
        curRunID++;
    }



    /**
     *
     * @param probName
     * @param genNum
     * @param curGenBestParticle
     */
    public void addGenBestInfo(String probName, int genNum, Individual curGenBestIndividual){
        String algName="EAHMPAlg";
        //int runNum,int genNum,
        int[] codeIntArray= curGenBestIndividual.getCode();
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
        double objValue= curGenBestIndividual.getFitness();
        String constrains= " ";
        long outputTime=System.currentTimeMillis();
        GeneResult geneBestResult=new GeneResult(algName,probName, curRunID,genNum,code,objValue,constrains,outputTime);
        super.addGenBestInfo(geneBestResult);
    }



    @Override
    public List<DefPara> getDefParas(String algName) {
        QueryWrapper<DefPara> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("alg_name",algName);
        List<DefPara> defParas=new ArrayList<>();
        defParas=defParaDao.selectList(queryWrapper);
        return defParas;
    }

    public void finalize() throws Throwable {

    }
    public void run(){

    }
}

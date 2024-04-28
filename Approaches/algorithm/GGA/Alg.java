package GGA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Alg {
    private Population population;
    private DefParaTbl ggaDefParaTbl;//缺省的参数表
    private DefParaTbl ggaCurParaTbl;//当前的参数表
    private Individual bestGgaIndividual;

    private Timer timer;
    private  boolean stopFlag;

    private int curRunID;


    public Alg(){

    }
    public void init() throws IOException, ClassNotFoundException {
        List<DefPara> defParas=getDefParas("GGAlg");
        ggaDefParaTbl = GGADefParaTbl.getGGADefParaTbl(defParas);
        ggaCurParaTbl=new GGADefParaTbl(ggaDefParaTbl);//复制参数表
    }
    public void addRunRcd(String probName) throws IOException {
        List<RunParaConf> runParaConfs = ggaCurParaTbl.getRunParaConfs(probName, -1);
        Long curTime = System.currentTimeMillis();
        AlgRunRcd aAlgRunRcd = new AlgRunRcd("GGAlg", probName, -1, curTime, curTime);
        curRunID=super.addAlgRunRcd(runParaConfs, aAlgRunRcd);
    }



    /**
     *
     * @param problem
     */


    public void run(MKPProb problem,double time) throws IOException, ClassNotFoundException {
        //如不改参数，即按缺省参数运行算法，可按下面代码
        //population=new FPGA3Population();
        problem.transconstrains();
        problem.transVector();
        String probName=problem.getProbName();
        addRunRcd(probName);

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
        population.initGgaPopulation(problem,ggaCurParaTbl);
        addGenBestInfo(probName,0,population.getBestIndividual());
        int i=1;
        //for(int i=0;i<maxGenNum;i++){
        while(!stopFlag){
            population.crossPopulation();
            population.mutatePopulation();
            population.updatePoplation();
            population.upDateBestIndividual();
            //注意发布每代最優解
            addGenBestInfo(probName,i,population.getBestIndividual());
            i=i+1;

        }
        Long curTime = System.currentTimeMillis();
        stopRun("GGAlg",probName, curRunID,curTime);
        curRunID++;
    }


    /**
     *
     * @param curGenBestParticle
     * @param probName
     * @param gen
     * @param genNum
     */
    public void addGenBestInfo(String probName, int genNum, Individual curGenBestIndividual){
        String algName="GGAlg";
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
        double objValue= curGenBestIndividual.comObj(codeIntArray);
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

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public DefParaTbl getGgaDefParaTbl() {
        return ggaDefParaTbl;
    }

    public void setGgaDefParaTbl(DefParaTbl ggaDefParaTbl) {
        this.ggaDefParaTbl = ggaDefParaTbl;
    }

    public DefParaTbl getGgaCurParaTbl() {
        return ggaCurParaTbl;
    }

    public void setGgaCurParaTbl(DefParaTbl ggaCurParaTbl) {
        this.ggaCurParaTbl = ggaCurParaTbl;
    }

    public Individual getBestGgaIndividual() {
        return bestGgaIndividual;
    }

    public void setBestGgaIndividual(Individual bestGgaIndividual) {
        this.bestGgaIndividual = bestGgaIndividual;
    }

    public int getCurRunID() {
        return curRunID;
    }

    public void setCurRunID(int curRunID) {
        this.curRunID = curRunID;
    }

    public void finalize() throws Throwable {

    }
}

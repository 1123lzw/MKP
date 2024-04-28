package HHS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HHSAlg extends Alg {
    @Autowired
    public DefParaTbl hhsDefParaTbl;
    private DefParaTbl hhsCurParaTbl;
    public Group hhsGroup;
    private int curRunID;

    private Timer timer;
    private  boolean stopFlag;

    public void init() throws IOException, ClassNotFoundException {
        List<DefPara> defParas=getDefParas("FPGAlg");
        hhsDefParaTbl =DefParaTbl.getHHSDefParaTbl(defParas);
        hhsCurParaTbl=new DefParaTbl(hhsDefParaTbl);//复制参数表
    }

    public void addRunRcd(String probName) throws IOException {
        List<RunParaConf> runParaConfs = hhsCurParaTbl.getRunParaConfs(probName, -1);
        Long curTime = System.currentTimeMillis();
        AlgRunRcd aAlgRunRcd = new AlgRunRcd("HHSAlg", probName, -1, curTime, curTime);
        curRunID=super.addAlgRunRcd(runParaConfs, aAlgRunRcd);
    }



    /**
     *
     * @param problem
     */
    public void run(MKPProb problem ,double time) throws IOException, ClassNotFoundException {
        //如不改参数，即按缺省参数运行算法，可按下面代码
        problem.transconstrains();
        problem.transVector();
        String probName=problem.getProbName();
        addRunRcd(probName);

        int maxGenNum= 5000;

        // 产生随机粒子群
        TimerTask stopTask = new TimerTask() {
            @Override
            public void run() {

                stopFlag = true;  // 设置停止标志
            }
        };

        timer = new Timer();
        double delayInSeconds = time;
        long delayInMillis = (long) (delayInSeconds * 1000);
        timer.schedule(stopTask, delayInMillis);
        stopFlag = false;
        hhsGroup=new HHSGroup();
        hhsGroup.initHM(problem,hhsCurParaTbl);
        hhsGroup.repair(problem);
        int gen=1;
        //for(int gen=1;gen<=maxGenNum;gen++){//算法主体
        while(!stopFlag){
            hhsGroup.genHHSGroup(hhsCurParaTbl,problem,gen,maxGenNum);
            hhsGroup.updateHM();
            hhsGroup.updateHMByLocal(hhsCurParaTbl,problem);
            addGenBestInfo(probName,gen,hhsGroup.getHM().get(0));
            gen=gen+1;
        }
        Long curTime = System.currentTimeMillis();
        stopRun("HHSAlg",probName, curRunID,curTime);
        curRunID++;
    }


    /**
     *
     * @param probName
     * @param genNum
     * @param curGenBestParticle
     */
    public void addGenBestInfo(String probName, int genNum,Harmony harmony){
        String algName="HHSAlg";
        //int runNum,int genNum,
        int[] codeIntArray= harmony.getCode();
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
        double objValue=harmony.getFitness();
        String constrains= "";
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

    public void finalize() throws Throwable {

    }
}

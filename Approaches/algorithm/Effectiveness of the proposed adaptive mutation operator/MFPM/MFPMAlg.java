package MFPM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MFPMAlg {
    private ExternalArch externalArch;
    private Group maPlGroup;
    public DefParaTbl maplDefParaTbl;//缺省参数表
    public DefParaTbl maplCurParaTbl;    //当前参数表
    private int curRunID;

    private Timer timer;
    private  boolean stopFlag;

    public MFPMAlg(){

    }



    public void init() throws IOException, ClassNotFoundException {
        List<DefPara> defParas=getDefParas("MFPMAlg");
        maplDefParaTbl = MAPLDefParaTbl.getMAPLDefParaTbl(defParas);
        maplCurParaTbl=new MAPLDefParaTbl(maplDefParaTbl);//复制参数表
    }

    public void addRunRcd(String probName) throws IOException {
        List<RunParaConf> runParaConfs = maplCurParaTbl.getRunParaConfs(probName, -1);
        Long curTime = System.currentTimeMillis();
        AlgRunRcd aAlgRunRcd = new AlgRunRcd("MFPMAlg", probName, -1, curTime, curTime);
        curRunID=super.addAlgRunRcd(runParaConfs, aAlgRunRcd);
    }

    /**
     *
     * @param solvePro
     */
    public void run(MKPProb solvePro, double time)throws IOException, ClassNotFoundException{
        solvePro.transconstrains();
        solvePro.transVector();
        String probName=solvePro.getProbName();
        addRunRcd(probName);

        //初始化种群
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
        maPlGroup=new Group();
        maPlGroup.setMkpProb(solvePro);
        maPlGroup.initGroup(maplCurParaTbl);  //种群初始化
        maPlGroup.roGroup();

        externalArch.initExternalArch(maPlGroup,maplCurParaTbl);  //需要种群和精英解的个
        addGenBestInfo(probName,0,externalArch.getGbest());

        int gen=1;

        while(!stopFlag){
            externalArch.comLearing(maplCurParaTbl);        //计算MPD
            maPlGroup.genOffspring(externalArch);      //jpd在生成新解中生成
            maPlGroup.fix();
            externalArch.updateExternalArch(maPlGroup);         //更新外部文档
            addGenBestInfo(probName,gen,externalArch.getGbest());
            gen=gen+1;
        }
        Long curTime = System.currentTimeMillis();
        stopRun("MFPMAlg",probName, curRunID,curTime);
        curRunID++;


    }

    private void addGenBestInfo(String probName, int genNum, Individual gbest) {
        String algName="MFPMAlg";
        //int runNum,int genNum,
        int[] codeIntArray= gbest.getCode();
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
        double objValue= gbest.getFitness();
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
        //施文华给出调用DAO中的实现findDefParas(algName)
        defParas=defParaDao.selectList(queryWrapper);
        return defParas;
    }
    public void finalize() throws Throwable {

    }

}

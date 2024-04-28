package HHS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Group {
    private List<Harmony> curHarmonies;
    private List<Harmony> HM;
    private Random random=null;

    public Group(){

    }

    public void finalize() throws Throwable {

    }
    /**
     *
     * @param curParaTbl
     */
    public void genHHSGroup(DefParaTbl curParaTbl,MKPProb solveProb,int gen,int MaxGenNum){  //HM已经被排过序了
        //int gn=curParaTbl.getGn();
        int gn=20;
        double PARMin=0.01;
        double PARMax=0.99;
        double HMCR=0.9;
        double PAR=PARMin+(PARMax-PARMin)*(double)gen/MaxGenNum;
        curHarmonies=new ArrayList<>();  //每一代都清空
        Collections.sort(HM);  //HM排序
        if(random==null){
            random=new Random();
        }
        for(int i=0;i<gn;i++){
            int rand=random.nextInt(HM.size());
            Harmony harmony=new Harmony();
            harmony.genNewHarmony(HMCR,PAR,HM.get(rand),HM.get(0),solveProb);
            curHarmonies.add(harmony);  //加入的是已经修复的个体 并且计算适应度值的个体
        }

    }

    /**
     *
     * @param solveProb
     * @param curParaTbl
     */
    public void initHM(MKPProb solveProb, DefParaTbl curParaTbl){
//		int hms=curParaTbl.getHms();
        int hms=40;
        HM =new ArrayList<>();
        for(int i=0;i<hms;i++){
            Harmony harmony=new Harmony();
            harmony.initHarmony(solveProb);
            HM.add(harmony);
        }
    }

    public void repair(MKPProb solveProb){ //修复HM中的个体
        int size=HM.size();
        for(int i=0;i<size;i++){
            HM.get(i).drop(solveProb,HM.get(i).getCode());
            HM.get(i).add(solveProb,HM.get(i).getCode());
            HM.get(i).comFitness(solveProb);
        }
    }
    public void updateHM(){
        int size=getCurHarmonys().size();
        for (int i=0;i<size;i++){
            if(curHarmonies.get(i).getFitness()>HM.get(HM.size()-1).getFitness()){
                HM.remove(HM.size()-1);
                Harmony harmony=new Harmony(curHarmonies.get(i));
                HM.add(harmony);
                Collections.sort(HM);
            }
        }
    }

    /**
     *
     * @param curParaTbl
     */
    public void updateHMByLocal(DefParaTbl curParaTbl,MKPProb solveProb){
        int size=HM.size();
        for(int i=0;i<size;i++){
            HM.get(i).genNeighbors(curParaTbl,solveProb);
            HM.get(i).updateHarmony();
        }
        Collections.sort(HM);
    }



    public List<Harmony> getCurHarmonys() {
        return curHarmonies;
    }

    public void setCurHarmonys(List<Harmony> curHarmonys) {
        this.curHarmonies = curHarmonys;
    }

    public List<Harmony> getHM() {
        return HM;
    }

    public void setHM(List<Harmony> HM) {
        this.HM = HM;
    }
}

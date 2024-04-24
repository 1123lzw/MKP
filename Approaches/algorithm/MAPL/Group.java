package MAPL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Group {
    private List<Individual> individualList;
    private Individual pBest;
    private Random random;
    private MKPProb mkpProb=null;




    public Group(){

    }

    /**
     *
     * @param solveProb
     * @param exa
     */
    public void genOffspring(ExternalArch exa){
        int popSize=individualList.size();
        ArrayList<Individual> newIndividuals=new ArrayList<>();
        for(int i=0;i<popSize;i++){
            Individual curIndivual=new Individual();
            int[] route=curIndivual.randomSampling(mkpProb);
            int[]  code=curIndivual.genIndividual(mkpProb,exa,route);
            curIndivual.setCode(code);
            newIndividuals.add(curIndivual);
        }
        setIndividualList(newIndividuals);
    }


    /**
     *
     * @param solveProb
     * @param curParaTbl
     */


    public void initGroup( DefParaTbl curParaTbl){  //随机初始化
        int popSize=curParaTbl.getPopSize();
        individualList=new ArrayList<>();
        pBest=new Individual();

        for(int i=0;i<popSize;i++){
            Individual individual=new Individual();
            individual.initIndividual(mkpProb);
            individualList.add(individual);
        }
        setIndividualList(individualList);
    }

    /**
     *
     * @param solveProb
     */
    public void roGroup(){ //修复
        int popSize=individualList.size();
        for(int i=0;i<popSize;i++){
            boolean flag=individualList.get(i).isLegal(mkpProb);
            //System.out.println(flag);
            if(flag==false){

                individualList.get(i).roIndividual(mkpProb);
                individualList.get(i).lsIndividual(mkpProb);

            }else{
                individualList.get(i).comfiness(mkpProb);

            }
        }
    }





    /**
     *
     * @param solveProb
     */
    public void lsGroup(){
        int popSize=individualList.size();
        for(int i=0;i<popSize;i++){
            individualList.get(i).lsIndividual(mkpProb);
        }
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

    public List<Individual> getIndividualList() {
        return individualList;
    }

    public void setIndividualList(List<Individual> individualList) {
        this.individualList = individualList;
    }

    public Individual getpBest() {
        return pBest;
    }

    public void setpBest(Individual pBest) {
        this.pBest = pBest;
    }

    public void finalize() throws Throwable {

    }
}

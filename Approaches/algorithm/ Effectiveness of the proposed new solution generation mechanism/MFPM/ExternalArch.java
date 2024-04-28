package MFPM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExternalArch {
    private List<Individual> individualList;
    private double[][] JPD;
    private double[][] MPD;
    private Individual Gbest;
    private int changeNum;
    private int N;

    public ExternalArch(){

    }

    public double[][] comProMatrix(int[] route){
        int dimension=individualList.get(0).getCode().length;
        double[][]  pro=new double[dimension-1][4];
        for(int i=0;i<dimension-1;i++){
            Arrays.fill(pro[i],0);
            int count0=0;
            int count1=0;
            for(int j=0;j<individualList.size();j++){
                if(individualList.get(j).getCode()[route[i]]==0){
                    count0 +=1;
                    if(individualList.get(j).getCode()[route[i+1]]==0)
                        pro[i][0] +=1;
                    else
                        pro[i][1] +=1;
                }else{
                    count1 +=1;
                    if(individualList.get(j).getCode()[route[i+1]]==0)
                        pro[i][2] +=1;
                    else
                        pro[i][3] +=1;
                }
            }
            if(count0 !=0){
                pro[i][0]=pro[i][0]/(double)count0;
                pro[i][1]=pro[i][1]/(double)count0;
            }
            if(count1 !=0){
                pro[i][2]=pro[i][2]/(double)count1;
                pro[i][3]=pro[i][3]/(double)count1;
            }
        }
        return pro;
    }

    public void binaryMarkov(int route[]){
        double[][] pro=comProMatrix(route);
        JPD=new double[pro.length][4];
        int AL=individualList.size();
        int dimension=individualList.get(0).getCode().length;
        for(int i=0;i<dimension-1;i++){
            int num=0;
            for(int j=0;j<AL;j++){
                num += individualList.get(j).getCode()[route[i]];
            }
            JPD[i][0]=(1/(double)AL)*(AL-num)*pro[i][0];  //00
            JPD[i][1]=(1/(double)AL)*(AL-num)*pro[i][1];  //01
            JPD[i][2]=(num/(double)AL)*pro[i][2];  //10
            JPD[i][3]=(num/(double)AL)*pro[i][3];  //11
        }


    }

    /**
     *
     * @param curParaTbl
     */
    public void comLearing(DefParaTbl curParaTbl){  //MPD
        double lr=curParaTbl.getlR();
        int dimension=individualList.get(0).getCode().length;
        for(int i=0;i<dimension;i++){
            int  num1=0;
            for(int j=0;j<individualList.size();j++){
                num1 += individualList.get(j).getCode()[i];
            }
            MPD[i][1] =(1-lr)*MPD[i][1]+lr*(num1/(double)individualList.size());
            MPD[i][0]=1-MPD[i][1];
        }
    }

    public  void initMPD(){  //��ʼ��MPD
        int dimension= individualList.get(0).getCode().length;  //MPD��ά�ȴ�С
        MPD=new double[dimension][2]; //��һ����ȡ0�ĸ��ʣ��ڶ�����ȡ1�ĸ���
        for(int i=0;i<dimension;i++){
            Arrays.fill(MPD[i],0.5);
        }
    }

    /**
     *
     * @param maPlGroup
     */
    public void initExternalArch(Group maPlGroup, DefParaTbl curParaTbl){
        Collections.sort(maPlGroup.getIndividualList());
        List<Individual>  List=new ArrayList<>();
        int size=20;
        changeNum=1;
        N=0;
        for(int i=0;i<size;i++){
            Individual individual=new Individual(maPlGroup.getIndividualList().get(i));
            List.add(individual);
        }
        setIndividualList(List);
        initMPD();
        Individual individual=new Individual(individualList.get(0));
        setGbest(individual);

    }

    /**
     *
     * @param maPlGroup
     */

    public void updateExternalArch(Group maPlGroup){
        Collections.sort(maPlGroup.getIndividualList());
        int archSize=individualList.size();
        if(maPlGroup.getIndividualList().get(0).getFitness()>individualList.get(archSize-1).getFitness()){
            Individual pbest=new Individual(maPlGroup.getIndividualList().get(0));
            individualList.remove(archSize-1);
            individualList.add(pbest);
            double bestVal=Gbest.getFitness();
            Collections.sort(individualList);
            if(bestVal<individualList.get(0).getFitness()) {
                Individual bestIndividual = new Individual(individualList.get(0));
                setGbest(bestIndividual);
            }
        }

    }



    public List<Individual> getIndividualList() {
        return individualList;
    }

    public void setIndividualList(List<Individual> individualList) {
        this.individualList = individualList;
    }

    public double[][] getJPD() {
        return JPD;
    }

    public void setJPD(double[][] JPD) {
        this.JPD = JPD;
    }

    public double[][] getMPD() {
        return MPD;
    }

    public void setMPD(double[][] MPD) {
        this.MPD = MPD;
    }

    public Individual getGbest() {
        return Gbest;
    }

    public void setGbest(Individual gbest) {
        Gbest = gbest;
    }
    public void finalize() throws Throwable {

    }
}

package HHS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Harmony implements Comparable<Harmony>{

    private int[] code;
    private double fitness;
    List<Neighbor> hhsNeighbors;
    private Random random=null;

    public Harmony (){

    }
    public Harmony(Harmony newHarmony){
        this.fitness=newHarmony.fitness;
        int[] curCode=new int[newHarmony.getCode().length];
        for(int i=0;i<curCode.length;i++){
            curCode[i]=newHarmony.getCode()[i];
        }
        this.code=curCode;
    }
    public void comFitness(MKPProb solveProb){
        double[] value=solveProb.getValueVectortran();
        int dimension=value.length;
        fitness=0;
        for(int i=0;i<dimension;i++){
            fitness +=code[i]*value[i];
        }
    }

    public Boolean isLegal(MKPProb solveProb,int[] newCode){
        boolean flag=true;
        double[][] conMaxtri= solveProb.getContraintMatrixtran();
        int dimension=newCode.length;
        for(int j=0;j<conMaxtri[0].length;j++){
            double consum=0;
            for(int i=0;i<dimension;i++){
                consum +=conMaxtri[i][j]*newCode[i];
            }
            if(consum>conMaxtri[dimension][j]){
                flag=false;
                break;
            }
        }
        return flag;
    }
    public boolean singleIsLeagal(double[] conMaxtri,int[] newCode){
        boolean flag=true;
        int dimension=newCode.length;
        double sum=0;
        for(int i=0;i<dimension;i++){
            sum +=conMaxtri[i]*newCode[i];
        }
        if(sum>conMaxtri[dimension]){
            flag=false;
        }
        return flag;
    }

    public double[][] traCon(MKPProb solveProb){
        double[][] conMaxtri=solveProb.getContraintMatrixtran();
        double[][] tra=new double[conMaxtri[0].length][conMaxtri.length];
        for(int i=0;i<conMaxtri.length;i++){
            for(int j=0;j<conMaxtri[0].length;j++){
                tra[j][i]=conMaxtri[i][j];
            }
        }
        return tra;
    }

    public int[][] weightMax(MKPProb solveProb){
        double[][]  traCon=traCon(solveProb);
        double[] value=solveProb.getValueVectortran();
        double[][] weight=new double[traCon.length][value.length];
        for(int i=0;i<traCon.length;i++){
            for(int j=0;j<value.length;j++){
                weight[i][j]=value[j]/traCon[i][j];
            }
        }
        int[][] itemSort=new int[traCon.length][value.length];
        for(int i=0;i<itemSort.length;i++){
            for(int j=0;j<value.length;j++){
                itemSort[i][j]=j;
            }
        }
        for(int i=0;i<traCon.length;i++){
            for(int m=0;m<value.length-1;m++){
                for(int n=0;n<value.length-1-m;n++){
                    if(weight[i][n]<weight[i][n+1]){
                        double temp=weight[i][n];
                        weight[i][n]=weight[i][n+1];
                        weight[i][n+1]=temp;

                        int indexTemp=itemSort[i][n];
                        itemSort[i][n]=itemSort[i][n+1];
                        itemSort[i][n+1]=indexTemp;
                    }
                }
            }
        }
        return  itemSort;
    }
    public void genNewHarmony(double HMCR,double PAR,Harmony randHarmony,Harmony gBest,MKPProb solveProb){  //生成种群新个体
        if(random==null){
            random=new Random();
        }
        int[] curCode=randHarmony.getCode();
        int[] gbestCode=gBest.getCode();
        int dimesion=curCode.length;
        code=new int[dimesion];
        if(Double.compare(random.nextDouble(),HMCR)<0){
            for(int i=0;i<dimesion;i++){
                code[i]=curCode[i];
                if(Double.compare(random.nextDouble(),PAR)<0){
                    code[i]=gbestCode[i];
                }
            }
        }else{
            for(int i=0;i<dimesion;i++){
                if(Double.compare(random.nextDouble(),0.5)<0){
                    code[i]=1;
                }else
                    code[i]=0;
            }
        }
        drop(solveProb,code);
        add(solveProb,code);
        comFitness(solveProb);
    }
    /**
     *
     * @param curParaTbl
     */
    public void genNeighbors(DefParaTbl curParaTbl,MKPProb solveProb){
        if(random==null){
            random=new Random();
        }
//        int neighbors=curParaTbl.getS();
        int neighbors=3;
        int mutateLength=3;
//        int mutateLength=curParaTbl.getL();
        hhsNeighbors=new ArrayList<>();
        for(int i=0;i<neighbors;i++){
           Neighbor hhsNeighbor=new Neighbor();
            int[] change=new int[mutateLength];
            List<Integer> index=new ArrayList<>();
            int[] neiCode=new int[code.length];
            for(int j=0;j<code.length;j++){
                index.add(j);
                neiCode[j]=code[j];
            }
            for(int j=0;j<change.length;j++){
                int r=random.nextInt(index.size());
                change[j]=index.get(r);
                index.remove(r);
            }
            for(int j=0;j<change.length;j++){
                if(code[change[j]]==0){
                    neiCode[change[j]]=1;
                }else
                    neiCode[change[j]]=0;
            }
            drop(solveProb,neiCode);
            add(solveProb,neiCode);
            hhsNeighbor.setCode(neiCode);
            hhsNeighbor.comFitness(solveProb);   //每个邻居都修复了并且计算了适应度值
            hhsNeighbors.add(hhsNeighbor);
        }
    }

    public void updateHarmony(){
        Collections.sort(hhsNeighbors);
        if(hhsNeighbors.get(0).getFitness()>fitness){
            int[] curCode=new int[hhsNeighbors.get(0).getCode().length];
            for(int i=0;i<curCode.length;i++){
                curCode[i]=hhsNeighbors.get(0).getCode()[i];
            }
            setCode(curCode);
            setFitness(hhsNeighbors.get(0).getFitness());
        }

    }

    /**
     *
     * @param solveProb
     */
    public void initHarmony(MKPProb solveProb){
        int dimension=solveProb.getValueVectortran().length;
        if(random==null){
            random=new Random();
        }
        code=new int[dimension];
        for(int i=0;i<dimension;i++){
            if(Double.compare(random.nextDouble(),0.5)<0){
                code[i]=1;
            }else
                code[i]=0;
        }
    }

    /**
     *
     * @param solveProb
     */
    public void drop(MKPProb solveProb,int[] newCode){
        int[][] itemSort=weightMax(solveProb);
        double[][] traCon=traCon(solveProb);
        int dimension=newCode.length;
        boolean flagAll=isLegal(solveProb,newCode);
        if(flagAll==false) {
            for (int i = 0; i < traCon.length; i++) {
                boolean flag = singleIsLeagal(traCon[i],newCode);
                if (flag == false) {
                    for (int j = dimension - 1; j >= 0; j--) {
                        if (newCode[itemSort[i][j]] == 1) {
                            newCode[itemSort[i][j]] = 0;
                        }
                        flag = singleIsLeagal(traCon[i],newCode);
                        if (flag == true)
                            break;
                    }
                }
            }
        }
    }


    public void add(MKPProb solveProb,int[] newCode){
        int[][] itemSort=weightMax(solveProb);
        double[][] traCon=traCon(solveProb);
        int dimension=newCode.length;
        boolean flag=isLegal(solveProb,newCode);
        if(flag==true){
            double[] left=new double[traCon.length];
            for(int i=0;i<traCon.length;i++){
                double conSum=0;
                for(int j=0;j<dimension;j++){
                    conSum += traCon[i][j]*newCode[j];
                }
                left[i]=traCon[i][dimension]-conSum;
            }
            double max=0;
            int index=-1;
            for(int i=0;i<left.length;i++){
                if(max<left[i]){
                    max=left[i];
                    index=i;
                }
            }
            if(index !=-1){
                for(int i=0;i<dimension;i++){
                    if(newCode[itemSort[index][i]]==0){
                        newCode[itemSort[index][i]]=1;
                        flag=isLegal(solveProb,newCode);
                        if(flag==false){
                            newCode[itemSort[index][i]]=0;
                        }
                    }
                }
            }
        }
    }
    @Override  //逆序排序
    public int compareTo(Harmony o) {
        if(this.fitness<o.fitness){
            return 1;
        }else if(this.fitness>o.fitness){
            return -1;
        }
        return 0;
    }

    public List<Neighbor> getHhsNeighbors() {
        return hhsNeighbors;
    }

    public void setHhsNeighbors(List<Neighbor> hhsNeighbors) {
        this.hhsNeighbors = hhsNeighbors;
    }

    public void finalize() throws Throwable {

    }
    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}

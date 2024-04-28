package GGA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual> {
    private int[] code;
    MKPProb mkpProb;
    private DefParaTbl curParaTbl = null;//当前的参数表
    protected double fitness;//适度度值
    private Random random = null;


    public Individual() {

    }

    public Individual(Individual newGaIndividual) {//对象复制
        this.mkpProb = newGaIndividual.mkpProb;
        this.curParaTbl = newGaIndividual.curParaTbl;//
        this.fitness = newGaIndividual.fitness;//
        this.random = newGaIndividual.random;//
        int[] curCode = new int[newGaIndividual.getCode().length];
        for (int i = 0; i < newGaIndividual.getCode().length; i++) {
            curCode[i] = newGaIndividual.getCode()[i];
        }
        this.code = curCode;
    }

    public double comObj(int[] curCode) { //计算目标值
        double[] value = mkpProb.getValueVectortran();
        int dimension = value.length;
        double curFitness = 0;
        for (int i = 0; i < dimension; i++) {
            curFitness += value[i] * curCode[i];
        }
        return curFitness;
    }

    public double comFitness(int[] curCode) {  //计算适应度值
        int dimension = mkpProb.getValueVectortran().length;
        double[] value = mkpProb.getValueVectortran();
        double[][] contra = mkpProb.getContraintMatrixtran();
        double[] e = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            double sum = 0;
            for (int j = 0; j < contra[0].length; j++) {
                sum += contra[i][j];
            }
            e[i] = value[i] / sum;
        }
        //System.out.println(Arrays.toString(e));
        double curFitness = 0;
        for (int i = 0; i < dimension; i++) {
            curFitness += curCode[i] * e[i] * value[i];
        }
        return curFitness;
    }

    public void initGaIndividual(MKPProb prob, DefParaTbl curParaTbl, List<Integer> x1) throws IOException, ClassNotFoundException {  //初始化
        if (random == null) {
            random = new Random();
        }
        this.mkpProb = prob;
        this.curParaTbl = curParaTbl;
        int dimension = mkpProb.getValueVectortran().length;//维度
//        double a=curParaTbl.getA();
        double a = 0.9;
        code = new int[dimension];
        int randSize = (int) a * x1.size();
        List<Integer> newX1 = new ArrayList<>();
        for (int i = 0; i < x1.size(); i++) {
            newX1.add(x1.get(i));
        }
        for (int i = 0; i < randSize; i++) {
            int num = random.nextInt(newX1.size());
            code[newX1.get(num)] = 1;
            newX1.remove(num);
        }
        for (int i = 0; i < dimension; i++) {
            if (code[i] == 0) {
                double r = random.nextDouble();
                if (Double.compare(r, 0.5) >= 0) {
                    code[i] = 1;
                } else
                    code[i] = 0;
            }
        }
        setCode(code);

    }

    public void repair(int[] code) {  //修复
        double[] value = mkpProb.getValueVectortran();
        GGAHelper particleHelper = new GGAHelper();
        double[][] constraint = particleHelper.transform(mkpProb.getContraintMatrixtran());
        int[] p = particleHelper.Sort3(value, constraint);
        boolean flag = particleHelper.isValid(code, constraint);
        if (flag == false) {
            drop(p, constraint, code);
            add(p, constraint, code);
        } else {
            add(p, constraint, code);
        }
    }

    //修复不满足约束的解
    public void drop(int[] p, double[][] constraint, int[] code) {
        GGAHelper particleHelper = new GGAHelper();
        boolean flag;
        int dimension = p.length;
        for (int i = dimension - 1; i > -1; i--) {
            if (code[p[i]] == 1) {
                code[p[i]] = 0;
            }
            flag = particleHelper.isValid(code, constraint);
            if (flag == true) {
                break;
            }

        }
    }

    //add操作: 按照物品顺序对物品进行增加
    public void add(int[] p, double[][] constraint, int[] code) {
        GGAHelper particleHelper = new GGAHelper();
        boolean flag;
        int dimension = p.length;
        //System.out.println("repair"+Arrays.toString(p));
        for (int i = 0; i < dimension; i++) {
            if (code[p[i]] == 0) {
                code[p[i]] = 1;
                flag = particleHelper.isValid(code, constraint);
                if (flag == false) {
                    code[p[i]] = 0;
                }
            }
        }
    }

    public void cross(int[] code1, int[] code2) {
        if (random == null) {
            random = new Random();
        }
        int[] curCode = new int[code1.length];
        int randPonit = random.nextInt(curCode.length);
        for (int i = 0; i < curCode.length; i++) {
            if (i < randPonit) {
                curCode[i] = code1[i];
            } else {
                curCode[i] = code2[i];
            }
        }
        setCode(curCode);
    }

    public void mutate() {
        int dimension = code.length;
        if (random == null) {
            random = new Random();
        }
        for (int i = 0; i < 3; i++) {
            int rand = random.nextInt(dimension);
            code[rand] = 1 - code[rand];
        }
    }


    @Override  //逆序排序
    public int compareTo(Individual o) {
        if (this.fitness < o.fitness) {
            return 1;
        } else if (this.fitness > o.fitness) {
            return -1;
        }
        return 0;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public MKPProb getMkpProb() {
        return mkpProb;
    }

    public void setMkpProb(MKPProb mkpProb) {
        this.mkpProb = mkpProb;
    }

    public DefParaTbl getCurParaTbl() {
        return curParaTbl;
    }

    public void setCurParaTbl(DefParaTbl curParaTbl) {
        this.curParaTbl = curParaTbl;
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

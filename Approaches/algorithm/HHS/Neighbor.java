package HHS;

public class Neighbor implements Comparable<Neighbor>{
    private int[] code;
    private double fitness;

    public void comFitness(MKPProb solveProb){
        double[] valueVector =solveProb.getValueVectortran();
        int dimension=code.length;
        fitness=0;
        for (int i = 0; i <dimension; i++) {
            fitness += code[i]*valueVector[i];
        }
    }

    @Override  //逆序排序
    public int compareTo(Neighbor o) {
        if(this.fitness<o.fitness){
            return 1;
        }else if(this.fitness>o.fitness){
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

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}

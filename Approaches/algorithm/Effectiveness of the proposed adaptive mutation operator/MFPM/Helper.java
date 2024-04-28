package MFPM;

import EAHMP.Individual;

public class Helper extends Individual {
    //权重表转换
    public double[][] transform(double[][] constrains) {
        double[][] constrain = new double[constrains[0].length][constrains.length];
        for (int i = 0; i < constrains[0].length; i++) {
            for (int j = 0; j < constrains.length; j++) {
                constrain[i][j] = constrains[j][i];
            }
        }
        return constrain;
    }

    //计算某个属性的权重
    public double count(int[] code, double[] constrain) {
        double cur_val = 0;
        for (int i = 0; i < code.length; i++) {
            cur_val += constrain[i] * code[i];
        }
        return cur_val;
    }

    //判断是否违反约束，输入的是转换后的矩阵
    public boolean isValid(int[] code, double[][] constrain) {
        boolean flag = true;
        double[] limit = new double[constrain.length]; //限制个数
        for (int i = 0; i < constrain.length; i++) {
            limit[i] = constrain[i][constrain[i].length - 1];  //
            double cur_val = count(code, constrain[i]);
            if (cur_val > limit[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    public int[] Sort3(double[] value, double[][] constrain) {  //Profit density
        double[] r = new double[value.length];
        int[] q = new int[value.length];
        for (int j = 0; j < value.length; j++) {
            q[j] = j;
            double c =0;
            for (int i = 0; i < constrain.length; i++) {
                c += constrain[i][j]/constrain[i][constrain[i].length-1];
            }
            r[j] = (c/constrain.length)/value[j];
        }
        double temp;
        int index;
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r.length - i - 1; j++) {
                if (r[j] > r[j + 1]) {  //升序排序
                    temp = r[j];
                    r[j] = r[j + 1];
                    r[j + 1] = temp;

                    index = q[j];
                    q[j] = q[j + 1];
                    q[j + 1] = index;
                }
            }
        }
        return q;
    }
}

package com.example.demo.absAlgRun.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author Youcongni
 * @version 1.0
 * @created 01-10��-2021 21:37:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("mkp_prob")
public class

MKPProb {
	private String probName;//问题名
	@TableField(value = "weight_matrix")
	private Object contraintMatrix;//存储多维背包问题的约束矩阵
	private Object valueVector;//存储多维背包问题的价值向量
	@TableField(exist = false)
    private double [][] contraintMatrixtran;
	@TableField(exist = false)
    private double [] valueVectortran;

	public double[][] getContraintMatrixtran() {
		return contraintMatrixtran;
	}

	public void setContraintMatrixtran(double[][] contraintMatrixtran) {
		this.contraintMatrixtran = contraintMatrixtran;
	}

	public double[] getValueVectortran() {
		return valueVectortran;
	}

	public void setValueVectortran(double[] valueVectortran) {
		this.valueVectortran = valueVectortran;
	}

	public String getProbName() {
		return probName;
	}

	public void setProbName(String probName) {
		this.probName = probName;
	}

	public Object getContraintMatrix(){
		return contraintMatrix;
	}

	public Object getValueVector(){
		return valueVector;
	}

	public void setContraintMatrix(Object contraintMatrix) {
		this.contraintMatrix = contraintMatrix;
	}

	public void setValueVector(Object valueVector) {
		this.valueVector = valueVector;
	}
	public void transconstrains() throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteIntconstrains = new ByteArrayInputStream((byte[]) this.contraintMatrix);
		ObjectInputStream objIntconstrains = new ObjectInputStream(byteIntconstrains);
		List<List<Double>> mkpconstrains=(List<List<Double>>)objIntconstrains.readObject();
		this.contraintMatrixtran= new double[mkpconstrains.size()][mkpconstrains.get(0).size()];
		for(int i=0;i<mkpconstrains.size();i++){

			for(int j=0;j<mkpconstrains.get(i).size();j++){
				this.contraintMatrixtran[i][j]=mkpconstrains.get(i).get(j);
			}
		}
	}
	public void transVector() throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteInvalueVectort = new ByteArrayInputStream((byte[]) this.valueVector);
		ObjectInputStream objIntvalueVector = new ObjectInputStream(byteInvalueVectort);
		List<Double> mkpvalueVector=(List<Double>)objIntvalueVector.readObject();
		this.valueVectortran = new double[mkpvalueVector.size()];
		for(int i=0;i<mkpvalueVector.size();i++){
			this.valueVectortran[i]=mkpvalueVector.get(i);
		}
	}
}//end MKPProb
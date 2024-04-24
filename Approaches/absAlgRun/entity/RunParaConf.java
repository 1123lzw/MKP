package com.example.demo.absAlgRun.entity;


import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author Youcongni
 * @version 1.0
 * @created 01-10��-2021 21:37:58
 */
public class RunParaConf {

	private String algName;//算法名
	private String probName;//问题名
	@TableField(value = "run_id")
	private int runID;//第几次运行
	private String paraName;//参数名
	private Object paraValue;//值

	public RunParaConf(String algName,String probName,int runID, String paraName,Object paraValue){
		this.algName=algName;
		this.probName=probName;
		this.runID =runID;
		this.paraName=paraName;
		this.paraValue=paraValue;
	}

	public RunParaConf() {

	}

	public String getAlgName() {
		return algName;
	}

	public void setAlgName(String algName) {
		this.algName = algName;
	}

	public String getProbName() {
		return probName;
	}

	public void setProbName(String probName) {
		this.probName = probName;
	}

	public int getRunID() {
		return runID;
	}

	public void setRunID(int runID) {
		this.runID = runID;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public Object getParaValue() {
		return paraValue;
	}

	public void setParaValue(Object paraValue) {
		this.paraValue = paraValue;
	}
}//end RunParaConf
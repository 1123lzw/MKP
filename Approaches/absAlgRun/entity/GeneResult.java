package com.example.demo.absAlgRun.entity;


//import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Youcongni
 * @version 1.0
 * @created 01-10��-2021 21:37:58
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class GeneResult implements Serializable {

	private String algName;//算法名
	private String probName;//问题名
	@TableField(value = "run_id")
	private int runID;//第几次运行
	private int genNum;//第几代
	private String code;//最优解编码，形如:[1,0,....,1]的字符串
	private double objValue;//适应度值或优化目标的值
	private String contraints;//惩罚函数值对应的字符串，形如[v1,v2,...,vm]
	private long outputTime;//输出最优解的时间



	public GeneResult(String algName,String probName,int runID,int genNum, String code,double objValue,String contraints,long outputTime){
   	  this.algName=algName;
   	  this.probName=probName;
   	  this.runID=runID;
   	  this.genNum=genNum;
   	  this.code=code;
   	  this.objValue=objValue;
   	  this.contraints=contraints;
   	  this.outputTime=outputTime;
   }


}//end GeneResult
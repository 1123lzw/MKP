package com.example.demo.absAlgRun.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Youcongni
 * @version 1.0
 * @created 01-10��-2021 21:37:58
 */
@Data
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("alg_run_record")
public class AlgRunRcd {

	private String algName;//算法名
	private String probName;//问题名
	@TableField(value = "run_id")
	private int runID=-1;//第几次运行
	private long startTime;//问题一次求解的开始时间
	private long endTime;//问题一次求解的结束时间


	public AlgRunRcd(String algName,String probName,int runID,long startTime,long endTime){
		this.algName=algName;
		this.probName=probName;
		this.runID=runID;
		this.startTime=startTime;
		this.endTime=endTime;
	}

	public void setRunID(int runID) {
		this.runID = runID;
	}
}//end AlgRunRcd
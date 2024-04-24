package com.example.demo.absAlgRun.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.algorithm.HPSOGO.service.HPSOGDefParaTbl;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("def_para")
public class DefPara {
    private String algName;//算法名
	private String paraName;//参数名
	private String dataType;//参数数据类型
	private Object value;//参数值

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}//end DefPara
package com.example.demo.absAlgRun.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.example.demo.absAlgRun.dao.*;
import com.example.demo.absAlgRun.entity.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Youcongni
 * @version 1.0
 * @created 01-10��-2021 21:37:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Component
public class Alg {
	private  String algName;//算法名
	@Autowired
	private AlgDao algDao;
	@Autowired
	protected DefParaDao defParaDao;
	@Autowired
	private RunParaConfDao runParaConfDao;
	@Autowired
	private AlgRunRcdDao algRunRcdDao;
	@Autowired
	private MKPProbDao mkpProbDao;
	@Autowired
	protected GeneResultFunctionDao geneResultFunctionDao;
	@Autowired
	private IndInformationDao indInformationDao;
	@Autowired
	RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法
	private Object SerializeUtil;
	public Alg(String algName){
		this.algName=algName;
	}

	public Alg() {

	}

	public List<MKPProb> getAllProbs(){
		List<MKPProb> probs=new ArrayList<MKPProb>();
		//施文华给出调用DAO中的实现findAllProbs
		probs=mkpProbDao.selectList(null);
		return probs;

	};

	/**
	 * 
	 * @param algName
	 */
	public List<DefPara> getDefParas(String algName){
		QueryWrapper<DefPara> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("alg_name",algName);
		List<DefPara> defParas=new ArrayList<>();
		//施文华给出调用DAO中的实现findDefParas(algName)
        defParas=defParaDao.selectList(queryWrapper);
		return defParas;
	};

	/**
	 * 
	 * @param runParaConfs
	 * @param aAlgRunRcd
	 */
	public int addAlgRunRcd(List<RunParaConf> runParaConfs, AlgRunRcd aAlgRunRcd) {
		//施文华给出调用DAO中的实现addAlgRunRcd和addRunParaConfs,并返回当前ID
		//1.先入库AlgRunRcd，获取返回的当前最大运行次数curMax
		int curMax = algRunRcdDao.getcurMax();
		aAlgRunRcd.setRunID(curMax+1);
		algRunRcdDao.insert(aAlgRunRcd);
		curMax = algRunRcdDao.getcurMax();
		//2.用curMax,赋给runParaConfs中的每个元素
		for (RunParaConf runParaConf : runParaConfs) {
			runParaConf.setRunID(curMax);
			//3.入库runParaConfs
			runParaConfDao.insert(runParaConf);
		}
		return curMax;
	};



	/**
	 * 
	 * @param problem
	 * @param runNum
	 */
//	public void run(MKPProb problem, int runNum){
//          //无需实现
//	};

	/**
	 * 
	 * @param algName
	 * @param probName
	 * @param runNum
	 * @param endTime
	 */
	public void stopRun(String algName, String probName, int runNum, long endTime){
		//施文华将算法一次运行停机消息发布至foundation.common.rabbitmq中
//		String messageId = String.valueOf(UUID.randomUUID());
//		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String,Object> map=new HashMap<>();
		map.put("algName",algName);
		map.put("probName",probName);
		map.put("runNum",runNum);
		map.put("endTime",endTime);
		//将消息携带绑定键值：StopRunRouting 发送到交换机StopRunExchange
		rabbitTemplate.convertAndSend("StopRunExchange", "StopRunRouting", map);

	};

	/**
	 * 
	 * @param geneBestResult
	 */
	public void addGenBestInfo(GeneResult geneBestResult){
        //施文华将该消息发布至foundation.common.rabbitmq中
		String string = JSON.toJSONString(geneBestResult);
		//将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
		rabbitTemplate.convertAndSend("BestParticlesExchange", "BestParticlesRouting", string);
	}

//	public void addFunInfo(GeneResultFunction geneBestResultFuction){
//		String string = JSON.toJSONString(geneBestResultFuction);
//		rabbitTemplate.convertAndSend("BestParticlesExchange", "BestParticlesRouting", string);
//	}
	public void addIndInfo(IndInformation indInformation){
		indInformationDao.insert(indInformation);
	}

	public void addAlgEntity(String algName,String descrtiption,List<DefPara> defParas) {
		//施文华做AlgEntity入库
		AlgEntity algEntity = new AlgEntity();
		algEntity.setAlgName(algName);
		algEntity.setDescription(descrtiption);
		algDao.insert(algEntity);
		//施文华做DefPara入库
		for (DefPara defPara:defParas) {
			defParaDao.insert(defPara);
		}
	}
	public void addMKPProb(String name,List<Double> valueVector,List<List<Double>> weightMatrix) throws IOException {
		MKPProb mkpProb=new MKPProb();
		mkpProb.setProbName(name);//


		ByteArrayOutputStream bytweightMatrix=new ByteArrayOutputStream();
		ObjectOutputStream objweightMatrix=new ObjectOutputStream(bytweightMatrix);
		objweightMatrix.writeObject(weightMatrix);
		byte[] bytesweightMatrix=bytweightMatrix.toByteArray();
		mkpProb.setContraintMatrix(bytesweightMatrix);

		ByteArrayOutputStream bytvalueVector=new ByteArrayOutputStream();
		ObjectOutputStream objvalueVector=new ObjectOutputStream(bytvalueVector);
		objvalueVector.writeObject(valueVector);
		byte[] bytesvalueVector=bytvalueVector.toByteArray();
		mkpProb.setValueVector(bytesvalueVector);
		mkpProbDao.insert(mkpProb);
	}

}
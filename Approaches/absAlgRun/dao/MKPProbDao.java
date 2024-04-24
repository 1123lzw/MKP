package com.example.demo.absAlgRun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.absAlgRun.entity.GeneResult;
import com.example.demo.absAlgRun.entity.MKPProb;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MKPProbDao extends BaseMapper<MKPProb> {
}

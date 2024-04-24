package com.example.demo.absAlgRun.dao;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.absAlgRun.entity.AlgEntity;
import com.example.demo.absAlgRun.entity.MKPProb;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface AlgDao extends BaseMapper<AlgEntity> {

}

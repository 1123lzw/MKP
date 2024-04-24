package com.example.demo.absAlgRun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.absAlgRun.entity.RunParaConf;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RunParaConfDao extends BaseMapper<RunParaConf> {
}

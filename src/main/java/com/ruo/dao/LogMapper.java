package com.ruo.dao;

import com.ruo.model.Log;
import com.ruo.model.LogWithBLOBs;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogWithBLOBs record);

    int insertSelective(LogWithBLOBs record);

    LogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LogWithBLOBs record);

    int updateByPrimaryKey(Log record);
}
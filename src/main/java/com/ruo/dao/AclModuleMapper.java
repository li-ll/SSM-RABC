package com.ruo.dao;

import com.ruo.model.AclModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AclModule record);

    int insertSelective(AclModule record);

    AclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AclModule record);

    int updateByPrimaryKey(AclModule record);
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    List<AclModule> getChildAclModuleListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("sysAclModuleList") List<AclModule> sysAclModuleList);

    List<AclModule> getAllAclModule();

    int countByParentId(@Param("aclModuleId") int aclModuleId);
}
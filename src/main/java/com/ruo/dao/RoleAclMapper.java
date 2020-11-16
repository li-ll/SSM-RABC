package com.ruo.dao;

import com.ruo.model.RoleAcl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleAcl record);

    int insertSelective(RoleAcl record);

    RoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAcl record);

    int updateByPrimaryKey(RoleAcl record);
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("roleAclList") List<RoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}
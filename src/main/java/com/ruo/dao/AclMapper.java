package com.ruo.dao;

import com.ruo.beans.PageQuery;
import com.ruo.model.Acl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface AclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Acl record);

    int insertSelective(Acl record);

    Acl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Acl record);

    int updateByPrimaryKey(Acl record);
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    List<Acl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page") PageQuery page);

    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("name") String name, @Param("id") Integer id);

    List<Acl> getAll();

    List<Acl> getByIdList(@Param("idList") List<Integer> idList);

    List<Acl> getByUrl(@Param("url") String url);

}
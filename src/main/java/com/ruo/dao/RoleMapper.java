package com.ruo.dao;

import com.ruo.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
    List<Role> getAll();

    int countByName(@Param("name") String name, @Param("id") Integer id);

    List<Role> getByIdList(@Param("idList") List<Integer> idList);
}
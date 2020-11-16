package com.ruo.dao;

import com.ruo.model.Dept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeptMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(@Param("id")Integer id);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);
    List<Dept>getAllDept();
    List<Dept>getChildDeptListByLevel(@Param("level")String level);
    void batchUpdateLevel(@Param("DeptList") List<Dept> DeptList);
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);
  int countByParentId(@Param("deptId") int deptId);
}
package com.ruo.dao;

import com.ruo.beans.PageQuery;
import com.ruo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    User findByKeyword(@Param("keyword") String keyeord);
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    int countByDeptId(@Param("deptId") int deptId);
    List<User> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    List<User> getByIdList(@Param("idList") List<Integer> idList);

    List<User> getAll();
}
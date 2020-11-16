package com.ruo.service;

import com.google.common.base.Preconditions;
import com.ruo.common.RequestHolder;
import com.ruo.dao.DeptMapper;
import com.ruo.exception.ParamException;
import com.ruo.model.Dept;
import com.ruo.param.DeptParam;
import com.ruo.util.BeanValidator;
import com.ruo.util.IpUtil;
import com.ruo.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DeptService {
    @Resource
    private DeptMapper deptMapper;


    public void save(DeptParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        Dept dept=Dept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
      dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
      dept.setOperator(RequestHolder.getCurrentUser().getUsername());
      dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
      dept.getOperateTime(new Date());
      deptMapper.insertSelective(dept);
    }
    public void update(DeptParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        Dept before = deptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        Dept after = Dept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));

    }
    @Transactional
   public void updateWithChild(Dept before, Dept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<Dept> deptList = deptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (Dept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                deptMapper.batchUpdateLevel(deptList);
            }
        }
        deptMapper.updateByPrimaryKey(after);
    }
    private boolean checkExist(Integer parentId,String deptName,Integer deptId){
        return deptMapper.countByNameAndParentId(parentId,deptName,deptId)>0;
    }
    private String getLevel(Integer deptId){
       Dept dept=deptMapper.selectByPrimaryKey(deptId);
       if (dept==null){
           return null;
       }
       return dept.getLevel();
    }
    public void delete(int deptId) {
        Dept dept = deptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (deptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
       /* if(userMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }*/
        deptMapper.deleteByPrimaryKey(deptId);
    }
}

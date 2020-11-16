package com.ruo.service;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ruo.common.RequestHolder;
import com.ruo.dao.RoleAclMapper;
import com.ruo.dao.RoleMapper;
import com.ruo.dao.RoleUserMapper;
import com.ruo.dao.UserMapper;
import com.ruo.exception.ParamException;
import com.ruo.model.Role;
import com.ruo.model.User;
import com.ruo.param.RoleParam;
import com.ruo.util.BeanValidator;
import com.ruo.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;
   @Resource
    private RoleUserMapper roleUserMapper;
    @Resource
    private RoleAclMapper roleAclMapper;
    @Resource
    private UserMapper userMapper;
  /*  @Resource
    private SysLogService sysLogService;*/

    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        Role role = Role.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        roleMapper.insertSelective(role);
        /* sysLogService.saveRoleLog(null, role);*/
    }

    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        Role before = roleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        Role after = Role.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        roleMapper.updateByPrimaryKeySelective(after);
        /* sysLogService.saveRoleLog(before, after);*/
    }

    public List<Role> getAll() {
        return roleMapper.getAll();
    }

    private boolean checkExist(String name, Integer id) {
        return roleMapper.countByName(name, id) > 0;
    }

   public List<Role> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return roleMapper.getByIdList(roleIdList);
    }

    public List<Role> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = roleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return roleMapper.getByIdList(roleIdList);
    }

    public List<User> getUserListByRoleList(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return userMapper.getByIdList(userIdList);
    }
}
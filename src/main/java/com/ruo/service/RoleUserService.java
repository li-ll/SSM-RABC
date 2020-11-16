package com.ruo.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruo.common.RequestHolder;
import com.ruo.dao.RoleUserMapper;
import com.ruo.dao.UserMapper;
import com.ruo.model.RoleUser;
import com.ruo.model.User;
import com.ruo.util.IpUtil;
import com.ruo.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RoleUserService {

    @Resource
    private RoleUserMapper  roleUserMapper;
    @Resource
    private UserMapper userMapper;
   /* @Resource
    private SysLogMapper sysLogMapper;
*/
    public List<User> getListByRoleId(int roleId) {
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return userMapper.getByIdList(userIdList);
    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
      /*  saveRoleUserLog(roleId, originUserIdList, userIdList);*/
    }

    @Transactional
    void updateRoleUsers(int roleId, List<Integer> userIdList) {
        roleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<RoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            RoleUser roleUser = RoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        roleUserMapper.batchInsert(roleUserList);
    }
   /* private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }*/
}
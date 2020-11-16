package com.ruo.service;

import com.google.common.collect.Lists;
import com.ruo.common.RequestHolder;
import com.ruo.dao.AclMapper;
import com.ruo.dao.RoleAclMapper;
import com.ruo.dao.RoleUserMapper;
import com.ruo.model.Acl;
import com.ruo.model.User;
import com.ruo.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CoreService {

    @Resource
    private AclMapper aclMapper;
    @Resource
    private RoleUserMapper roleUserMapper;
    @Resource
    private RoleAclMapper roleAclMapper;
   /* @Resource
    private SysCacheService sysCacheService;*/

   public List<Acl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    public List<Acl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = roleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return aclMapper.getByIdList(aclIdList);
    }

    public List<Acl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            return aclMapper.getAll();
        }
        List<Integer> userRoleIdList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = roleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return aclMapper.getByIdList(userAclIdList);
    }

    public boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        User user = RequestHolder.getCurrentUser();
        if (user.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

     /* public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<Acl> aclList = aclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<Acl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (Acl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }

  public List<Acl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }*/
}

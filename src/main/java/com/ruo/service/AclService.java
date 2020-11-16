package com.ruo.service;


import com.google.common.base.Preconditions;
import com.ruo.beans.PageQuery;
import com.ruo.beans.PageResult;
import com.ruo.common.RequestHolder;
import com.ruo.dao.AclMapper;
import com.ruo.exception.ParamException;
import com.ruo.model.Acl;
import com.ruo.param.AclParam;
import com.ruo.util.BeanValidator;
import com.ruo.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AclService {

    @Resource
    private AclMapper aclMapper;
   /* @Resource
    private SysLogService sysLogService;
*/
    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl acl = Acl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclMapper.insertSelective(acl);
        /*sysLogService.saveAclLog(null, acl);*/
    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        Acl before = aclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        Acl after = Acl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        aclMapper.updateByPrimaryKeySelective(after);
     /*   sysLogService.saveAclLog(before, after);*/
    }

    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return aclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    public PageResult<Acl> getPageByAclModuleId(int aclModuleId, PageQuery page) {
        BeanValidator.check(page);
        int count = aclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<Acl> aclList = aclMapper.getPageByAclModuleId(aclModuleId, page);
            return PageResult.<Acl>builder().data(aclList).total(count).build();
        }
        return PageResult.<Acl>builder().build();
    }
}

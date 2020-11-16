package com.ruo.service;

import com.google.common.base.Preconditions;
import com.ruo.beans.PageQuery;
import com.ruo.beans.PageResult;
import com.ruo.common.RequestHolder;
import com.ruo.dao.UserMapper;
import com.ruo.exception.ParamException;
import com.ruo.model.User;
import com.ruo.param.UserParam;
import com.ruo.util.BeanValidator;
import com.ruo.util.IpUtil;
import com.ruo.util.MD5Util;
import com.ruo.util.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public void save(UserParam param){
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        String password = PasswordUtil.randomPassword();
        password = "12345678";
        String encryptedPassword = MD5Util.encrypt(password);
        User user = User.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());
        //ToDO:sendMail
        userMapper.insertSelective(user);
    }
    public void update(UserParam param) {
        BeanValidator.check(param);
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        User before = userMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        User after = User.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
    userMapper.updateByPrimaryKeySelective(after);
    }
    public boolean checkEmailExist(String mail, Integer userId) {
        return userMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return userMapper.countByTelephone(telephone, userId) > 0;
    }

    public User findByKeyword(String keyword) {
        return userMapper.findByKeyword(keyword);
    }
    public PageResult<User> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        int count = userMapper.countByDeptId(deptId);
        if (count > 0) {
            List<User> list = userMapper.getPageByDeptId(deptId, page);
            return PageResult.<User>builder().total(count).data(list).build();
        }
        return PageResult.<User>builder().build();
    }

    public List<User> getAll() {
        return userMapper.getAll();
    }
}

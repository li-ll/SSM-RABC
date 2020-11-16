package com.ruo.controller;


import com.ruo.beans.PageQuery;
import com.ruo.beans.PageResult;
import com.ruo.common.JsonData;
import com.ruo.model.User;
import com.ruo.param.UserParam;
import com.ruo.service.TreeService;
import com.ruo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam param) {
        userService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param) {
        userService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery) {
        PageResult<User> result = userService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }
}

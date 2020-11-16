package com.ruo.controller;

import com.ruo.common.JsonData;
import com.ruo.param.AclModuleParam;
import com.ruo.service.AclModuleService;
import com.ruo.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class AclModuleController {
        @Resource
        private AclModuleService aclModuleService;
        @Resource
        private TreeService treeService;

        @RequestMapping("/acl.page")
        public ModelAndView page() {
            return new ModelAndView("acl");
        }

        @RequestMapping("/save.json")
        @ResponseBody
        public JsonData saveAclModule(AclModuleParam param) {
            aclModuleService.save(param);
            return JsonData.success();
        }

        @RequestMapping("/update.json")
        @ResponseBody
        public JsonData updateAclModule(AclModuleParam param) {
            aclModuleService.update(param);
            return JsonData.success();
        }

       @RequestMapping("/tree.json")
        @ResponseBody
        public JsonData tree() {
            return JsonData.success(treeService.aclModuleTree());
        }

        @RequestMapping("/delete.json")
        @ResponseBody
        public JsonData delete(@RequestParam("id") int id) {
            aclModuleService.delete(id);
            return JsonData.success();
        }
    }


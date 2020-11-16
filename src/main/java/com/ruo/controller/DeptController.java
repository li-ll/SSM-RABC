package com.ruo.controller;

import com.ruo.common.JsonData;
import com.ruo.dto.DeptLevelDto;
import com.ruo.param.DeptParam;
import com.ruo.service.DeptService;
import com.ruo.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class DeptController {
    @Resource
    private DeptService deptService;
    @Resource
    private TreeService treeService;
    @RequestMapping("/dept.page")
    public ModelAndView page(){
        return new ModelAndView("dept");
    }
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param){
        deptService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        List<DeptLevelDto>dtoList=treeService.deptTree();
        return JsonData.success(dtoList);
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param){
        deptService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id) {
       deptService.delete(id);
        return JsonData.success();
    }
}

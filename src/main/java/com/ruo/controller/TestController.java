package com.ruo.controller;

import com.ruo.common.ApplicationContextHelper;
import com.ruo.common.JsonData;
import com.ruo.exception.ParamException;
import com.ruo.param.TestVo;
import com.ruo.util.BeanValidator;
import com.ruo.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        throw new ParamException("test exception");
       // return JsonData.success("hello");
    }
    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo)  {
        log.info("validate");
        Map<String,String>map=BeanValidator.validateObject(vo);
        try {
            if (map!=null&&map.entrySet().size()>0)
                for (Map.Entry<String,String> entry:map.entrySet()){
                    log.info("{}->{}",entry.getKey(),entry.getValue());
                }
        }catch (Exception e){}
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }
}

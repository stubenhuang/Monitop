package com.stuben.monitop.test.action;

import com.stuben.monitop.test.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("demo")
public class DemoAction {
    @Autowired
    private IDemoService demoService;

    @RequestMapping("hello/{num}")
    public String hello(@PathVariable Integer num) {
        return demoService.hello(num);
    }
}

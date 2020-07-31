package com.stuben.monitop.server.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stuben.monitop.server.service.MonitorProxyService;


@Controller
@RequestMapping("/")
public class MonitorAction {

    @Autowired
    private MonitorProxyService monitorProxyService;

    @RequestMapping("refresh/{id}")
    public String hello(@PathVariable Integer id) {
        return monitorProxyService.refresh(id) ? "刷新成功" : "刷新失败";
    }
}

package com.stuben.monitop.test.service.impl;

import com.stuben.monitop.test.service.IDemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements IDemoService {
    public String hello(int num) {
        return "Hello , " + num;
    }
}

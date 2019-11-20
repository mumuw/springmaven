package com.lin.demo.mvc.service;

import com.lin.framework.annotation.LinService;

@LinService
public class DemoServiceImpl implements DemoService {

    @Override
    public String getName(String name) {
        return name;
    }
}

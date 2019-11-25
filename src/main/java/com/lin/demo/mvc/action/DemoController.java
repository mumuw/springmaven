package com.lin.demo.mvc.action;

import com.lin.demo.mvc.bean.UserInfoRequest;
import com.lin.demo.mvc.service.DemoService;
import com.lin.framework.annotation.LinAutowired;
import com.lin.framework.annotation.LinController;
import com.lin.framework.annotation.LinRequestMapping;
import com.lin.framework.annotation.LinRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LinController
@LinRequestMapping("/demo")
public class DemoController {
    @LinAutowired
    private DemoService demoService;
    @LinRequestMapping("/name")
    public void showName(HttpServletRequest req, HttpServletResponse resp,
                           @LinRequestParam("name") String name){
        String result = demoService.getName(name);
        try {
            resp.getWriter().write(result);
        }catch (IOException i){
            i.printStackTrace();
        }
    }

}

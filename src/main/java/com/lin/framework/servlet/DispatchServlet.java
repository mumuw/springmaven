package com.lin.framework.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class DispatchServlet extends HttpServlet {
    //存储aplication.properties的配置内容
    private Properties contextConfig = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        doLoadConfig(config.getInitParameter("contextConfigLocation"));


    }

    private void doLoadConfig(String configLocation) {
        InputStream inputStream = null;
        try {
            inputStream  = this.getClass().getClassLoader().getResourceAsStream(configLocation);
            contextConfig.load(inputStream);
        }catch (IOException i){
            i.printStackTrace();
        }finally {
            try{
                if (inputStream != null) inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}

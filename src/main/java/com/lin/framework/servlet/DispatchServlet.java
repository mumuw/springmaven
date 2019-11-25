package com.lin.framework.servlet;

import com.lin.framework.annotation.LinAutowired;
import com.lin.framework.annotation.LinController;
import com.lin.framework.annotation.LinRequestMapping;
import com.lin.framework.annotation.LinService;
import org.jsoup.helper.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DispatchServlet extends HttpServlet {
    //存储aplication.properties的配置内容
    private Properties contextConfig = new Properties();
    //存储所有扫描到的类
    private List<String> classNames = new ArrayList<String>();
    //IOC容器，保存所有实例化对象
    //注册式单例模式
    private Map<String,Object> ioc = new HashMap<String,Object>();
    //保存Contrller中所有Mapping的对应关系
    private Map<String,Method> handlerMapping = new HashMap<String,Method>();

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
        doScanner(contextConfig.getProperty("scanPackage"));
        //3、初始化所有相关的类的实例，并且放入到IOC容器之中
        doInstance();
        //4、完成依赖注入
        doAutowired();
        //5、初始化HandlerMapping
        initHandlerMapping();

        System.out.println("lin Spring framework is init.");

    }

    private void initHandlerMapping() {
        System.out.println("------------------initHandlerMapping-----------------");
        if(ioc.isEmpty()){return;}
        for (Map.Entry<String,Object> entry:ioc.entrySet()){
            try {
                System.out.println("initHandlerMapping entry name:" + entry.getValue().getClass().getName());
                if (!entry.getValue().getClass().isAnnotationPresent(LinController.class)){
                    continue;
                }
                LinRequestMapping linRequestMapping = entry.getValue().getClass().getAnnotation(LinRequestMapping.class);
                System.out.println("initHandlerMapping linController name:" +linRequestMapping.value());
                String baseUrl = linRequestMapping.value().replace("/","");

                Method[] methods = entry.getValue().getClass().getMethods();
                for (Method method : methods){
                    if (!method.isAnnotationPresent(LinRequestMapping.class)){
                        continue;
                    }
                    LinRequestMapping pathMapping = method.getAnnotation(LinRequestMapping.class);
                    String path = pathMapping.value();
                    String url = ("/" + baseUrl + "/" + path)
                            .replaceAll("/+", "/");
                    handlerMapping.put(url,method);
                    System.out.println("Mapped " + url + "," + method);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void doAutowired() {
        System.out.println("------------------doAutowired-----------------");
        if(ioc.isEmpty()){return;}
        for (Map.Entry<String,Object> entry:ioc.entrySet()){
            try {
            System.out.println("doAutowired entry: bean name:" + entry.getKey() + " bean value: " + entry.getValue());
            Field[] files = entry.getValue().getClass().getDeclaredFields();
            for (Field field: files){
                System.out.println("doAutowired filed:" + field.getName());
                if (!field.isAnnotationPresent(LinAutowired.class)){continue;}
                LinAutowired autowired = field.getAnnotation(LinAutowired.class);
                String beanName = autowired.value().trim();
                System.out.println("doAutowired annotation name:" + beanName);

                if (StringUtil.isBlank(beanName)){
                    beanName = field.getType().getName();
                    System.out.println("doAutowired field.getType name:" + beanName);
                }
                beanName = toLowerFirstCase(beanName);
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void doInstance() {
        System.out.println("------------------doInstance-----------------");
        if (classNames.isEmpty()){return;}
        try {
            for (String className:classNames){
                System.out.println("init class name :"+className);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(LinController.class)) {
                    Object instance = clazz.newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    System.out.println("init controller add to ioc name:" + beanName);
                    ioc.put(beanName,instance);
                }else if (clazz.isAnnotationPresent(LinService.class)){
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    LinService linService = clazz.getAnnotation(LinService.class);
                    if (!"".equals(linService.value())){
                        beanName = linService.value();
                    }
                    Object instance = clazz.newInstance();
                    System.out.println("init service add to ioc name:" + beanName);
                    ioc.put(beanName,instance);
                    //clazz.getInterfaces() 该类实现的接口
                    for (Class<?> i : clazz.getInterfaces()){
                        System.out.println("init service interface:" + beanName);
                        if (ioc.containsKey(i.getName())){
                            throw new Exception("the bean name is exit");
                        }
                        ioc.put(i.getName(),instance);
                    }
                }
            }
        }catch (Exception e){
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return  String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {
        System.out.println("------------------doScanner-----------------");
        URL url = this.getClass().getClassLoader()
                .getResource("/" + scanPackage.replaceAll("\\.","/"));
        System.out.println("scan:" +  url);
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()){
            if (file.isDirectory()){
//                System.out.println(scanPackage + "." + file.getName());
                doScanner(scanPackage + "." + file.getName());
            }else {
                System.out.println("doScan file name:" + file.getName());
                if (!file.getName().endsWith(".class")){continue;}
                String className = (scanPackage + "." + file.getName()).replace(".class","");
                System.out.println("doScan class name added:" +className);
                classNames.add(className);
            }
        }
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

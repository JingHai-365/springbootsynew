package com.jcooling.mall.config;

import com.jcooling.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @date: 2022/04/11
* @time: 10:30:29
* @version: 1.0
* @description: nothing.
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //set mapping
        registry.addResourceHandler("/Code/Gallery/image/imagessy05/**").addResourceLocations("file:"+Constant.FILE_UPLOAD_DIR);//必须要"file:"，否则失败
    }

    /*
    *全局跨域请求
    *
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/login").allowedMethods("GET","POST","DELETE","PUT");
        registry.addMapping("/register").allowedMethods("GET","POST","DELETE","PUT");
        registry.addMapping("/user/*").allowedMethods("GET","POST","DELETE","PUT");
    }
}
package com.jcooling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.jcooling.mall.model.dao")
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc//加了这个会使Springmvc自动配置失效
@EnableCaching
public class SpringbootsyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootsyApplication.class, args);
    }

}

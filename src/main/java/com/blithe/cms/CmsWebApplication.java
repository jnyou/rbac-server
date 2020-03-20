package com.blithe.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName CmsWebApplication
 * @Description: TODO
 * @Author: 夏小颜
 * @Date: 13:53
 * @Version: 1.0
 **/
@MapperScan(basePackages = {"com.blithe.cms.mapper"})
@SpringBootApplication
public class CmsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsWebApplication.class);
    }
}
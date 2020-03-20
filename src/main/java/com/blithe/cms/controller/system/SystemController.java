package com.blithe.cms.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName LoginController
 * @Description: 跳转页面
 * @Author: 夏小颜
 * @Date: 9:59
 * @Version: 1.0
 **/
@Controller
public class SystemController {


    /**
     * 系统登陆页
     */
    @RequestMapping("/login")
    public String toLogin(){
        return "login";
    }


    /**
     * 系统主页
     */
    @RequestMapping("/index")
    public String toIndex(){
        return "index";
    }

}
package com.blithe.cms.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: SystemController
 * @Description: 跳转Controller
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


    /**
     * main 工作空间页面
     */
    @RequestMapping("/main")
    public String toMain(){
        return "main";
    }

    /**
     * 登陆日志信息页
     */
    @RequestMapping("/sys/toLogInfoManager")
    public String toLogInfoManager(){
        return "system/logInfo";
    }

    /**
     * 系统公告页
     */
    @RequestMapping("/sys/toNoticeManager")
    public String toNoticeManager(){
        return "system/notice";
    }
    /**
     * ICON 页面
     */
    @RequestMapping("/sys/toIconManager")
    public String toIconManager(){
        return "icon";
    }

    /**
     * 部门管理页面
     * @return
     */
    @RequestMapping("/sys/toDeptManager")
    public String toDeptManager(){
        return "system/dept/deptManager";
    }

    /**
     * 部门管理left页面
     * @return
     */
    @RequestMapping("/sys/toDeptLeft")
    public String toDeptLeft(){
        return "system/dept/deptLeft";
    }

    /**
     * 部门管理right页面
     * @return
     */
    @RequestMapping("/sys/toDeptRight")
    public String toDeptRight(){
        return "system/dept/deptRight";
    }

}
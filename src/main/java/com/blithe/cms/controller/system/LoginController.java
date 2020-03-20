package com.blithe.cms.controller.system;

import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.HttpContextUtils;
import com.blithe.cms.realm.ActiverUser;
import com.blithe.cms.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LoginController
 * @Description: 登陆
 * @Author: 夏小颜
 * @Date: 14:55
 * @Version: 1.0
 **/

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * check code and pwd
     */
    @RequestMapping("/login")
    public R checkAccount(@RequestParam("username") String name, @RequestParam("password") String pwd, @RequestParam("code") String code){
        // 调用shiro提供得外部接口subject
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(name,pwd);

        try {
            // 登陆成功 调用login方法
            subject.login(token);
            // 从subject中获取用户身份
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            // 调用工具栏获得session并将用户信息存入session中
            HttpContextUtils.getHttpServletRequest().getSession().setAttribute("user",activerUser.getSysUser());
            return R.ok();
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }


}
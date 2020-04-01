package com.blithe.cms.controller.system;

import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.HttpContextUtils;
import com.blithe.cms.common.tools.IpUtil;
import com.blithe.cms.pojo.system.Loginfo;
import com.blithe.cms.pojo.system.Role;
import com.blithe.cms.realm.ActiverUser;
import com.blithe.cms.service.LoginfoService;
import com.blithe.cms.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    @Autowired
    private LoginfoService loginfoService;

    /**
     * check code and pwd
     */
    @PostMapping("/login")
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
            // 登陆成功后将登陆信息记录到登陆日志中
            Loginfo loginfo = new Loginfo();
            loginfo.setLoginname(activerUser.getSysUser().getName()+"-"+activerUser.getSysUser().getLoginname());
            loginfo.setLoginip(IpUtil.getIpAddr(HttpContextUtils.getHttpServletRequest()));
            loginfo.setLogintime(new Date());
            loginfoService.insert(loginfo);
//        }catch (IncorrectCredentialsException e){
//            System.out.println("密码不正确");
//        }catch (UnknownAccountException e){
//            System.out.println("账号不存在");
        }catch (AuthenticationException e){
            System.out.println("用户名或者密码不正确");
        }


        return R.ok();
    }
}
package com.blithe.cms.realm;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @ClassName UserRealm
 * @Description: 认证 授权
 * @Author: 夏小颜
 * @Date: 12:06
 * @Version: 1.0
 **/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        EntityWrapper<SysUser> entityWrapper = new EntityWrapper<SysUser>();
        // token.getPrincipal().toString() 获取身份
        entityWrapper.eq("loginname", token.getPrincipal().toString());
        SysUser sysUser = sysUserService.selectOne(entityWrapper);

        if (null != sysUser) {
            ActiverUser activerUser = new ActiverUser();
            activerUser.setSysUser(sysUser);
            // 加盐
            ByteSource salt = ByteSource.Util.bytes(sysUser.getSalt());
            AuthenticationInfo info = new SimpleAuthenticationInfo(activerUser, sysUser.getPwd(), salt, this.getName());
            return info;
        }
        return null;
    }


    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


}
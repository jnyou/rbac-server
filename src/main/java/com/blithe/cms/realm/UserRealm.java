package com.blithe.cms.realm;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blithe.cms.common.tools.Constast;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.system.PermissionService;
import com.blithe.cms.service.system.RoleService;
import com.blithe.cms.service.system.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;


/**
 * @ClassName UserRealm
 * @Description: 认证 授权
 * @Author: 夏小颜
 * @Date: 12:06
 * @Version: 1.0
 **/
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

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
//        System.out.println("代理对象:" + sysUserService.getClass().getSimpleName()); // 通过懒加载交给代理对象 代理对象:$Proxy85
        EntityWrapper entityWrapper = new EntityWrapper<Object>();
        // token.getPrincipal().toString() 获取身份
        entityWrapper.eq("loginname", token.getPrincipal().toString());
        SysUser sysUser = sysUserService.selectOne(entityWrapper);
        List<String> roles = null;
        List<String> permissions = null;
        List<Integer> pids = null;
        if (null != sysUser) {
            EntityWrapper wrapper = new EntityWrapper<Object>();
            EntityWrapper wrapper1 = new EntityWrapper<Object>();
            // 通过用户名称查询用户角色
            List<Integer> rids = sysUserService.selectRidByUid(sysUser.getId());
            if(CollectionUtil.isNotEmpty(rids)){
                wrapper.in("id",rids);
                roles = roleService.selectList(wrapper);
                // 根据rids查询pids
                for (Integer rid : rids) {
                    pids = roleService.queryRolePermissionIdsByRid(rid);
                }
                if(CollectionUtil.isNotEmpty(pids)){
                    wrapper1.in("id",pids);
                    permissions = permissionService.selectList(wrapper1);
                }
            }
            // 通过用户名称查询用户权限
            ActiverUser activerUser = new ActiverUser(sysUser,roles,permissions);
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
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        log.info("========================================用户授权========================================");
        ActiverUser activerUser = (ActiverUser) principal.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 添加角色
        List<String> roles = activerUser.getRoles();
        if(CollectionUtil.isNotEmpty(roles)){
            info.addRoles(roles);
        }
        // 添加权限
        List<String> permissions = activerUser.getPermissions();
        if(CollectionUtil.isNotEmpty(permissions)){
            info.addRoles(permissions);
        }
        // 超级管理员
        if(Constast.USER_TYPE_SUPER.equals(activerUser.getSysUser().getType())){
            info.addStringPermission("*:*");
        }
        System.out.println("该用户拥有的角色" + roles);
        System.out.println("该用户拥有的权限" + permissions);
        System.out.println("该用户拥有的角色和权限信息" + info);
        return info;
    }


}
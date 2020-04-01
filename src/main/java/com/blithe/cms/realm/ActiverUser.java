package com.blithe.cms.realm;

import com.blithe.cms.pojo.system.Permission;
import com.blithe.cms.pojo.system.Role;
import com.blithe.cms.pojo.system.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ActiverUser
 * @Description: 需要shiro管理
 * @Author: 夏小颜
 * @Date: 12:07
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

    private SysUser sysUser;

    private List<String> roles;

    private List<String> permissions;
}
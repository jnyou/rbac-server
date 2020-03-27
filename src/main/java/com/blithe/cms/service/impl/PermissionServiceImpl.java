package com.blithe.cms.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blithe.cms.mapper.system.PermissionMapper;
import com.blithe.cms.pojo.system.Permission;
import com.blithe.cms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author: youjiannan
 * @Description:
 * @Date: 2020/3/19
 * @Param:
 * @Return:
 **/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 删除了菜单，权限就没了，所以应该也删除对应的角色id
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Serializable id) {
//        PermissionMapper baseMapper = this.baseMapper;
        // 删除与角色关联的数据
        permissionMapper.deleteRolemissionByPid(id);
        return super.deleteById(id); // 删除权限表的数据
    }
}

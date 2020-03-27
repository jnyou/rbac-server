package com.blithe.cms.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blithe.cms.mapper.system.PermissionMapper;
import com.blithe.cms.mapper.system.RoleMapper;
import com.blithe.cms.pojo.system.Role;
import com.blithe.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: youjiannan
 * @Description: role
 * @Date: 2020/3/27
 * @Param:
 * @Return:
 **/
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public boolean deleteById(Serializable id) {
		//根据角色ID删除sys_role_permission
		roleMapper.deleteRolePermissionByRid(id);
		//根据角色ID删除sys_role_user
		roleMapper.deleteRoleUserByRid(id);
		return super.deleteById(id);
	}

	/**
	 * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
	 */
	@Override
	public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
		return roleMapper.queryRolePermissionIdsByRid(roleId);
	}

	/**
	 * 保存角色和菜单权限之间的关系
	 */
	@Override
	public void saveRolePermission(Integer rid, Integer[] ids) {
		//根据rid删除sys_role_permission
		roleMapper.deleteRolePermissionByRid(rid);
		if(ids!=null&&ids.length>0) {
			for (Integer pid : ids) {
				roleMapper.saveRolePermission(rid,pid);
			}
		}
	}
}

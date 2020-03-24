package com.blithe.cms.controller.system;


import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blithe.cms.common.tools.*;
import com.blithe.cms.pojo.system.Permission;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: youjiannan
 * @Description: 菜单
 * @Date: 2020/3/19
 * @Param:
 * @Return:
 **/
@RestController
@RequestMapping("/menu")
public class MenuController {


	@Autowired
	private PermissionService permissionService;
	
	
	@GetMapping("/loadIndexLeftMenuJson")
	public DataGridView loadIndexLeftMenuJson(Permission permission) {
		//查询所有菜单
		EntityWrapper<Permission> queryWrapper=new EntityWrapper<>();
		//设置只能查询菜单
		queryWrapper.eq("type", Constast.TYPE_MNEU);
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		
		SysUser user = (SysUser) HttpContextUtils.getHttpServletRequest().getSession().getAttribute("user");
		List<Permission> list=null;
		// 超级管理员登陆
		if(user.getType().equals(Constast.USER_TYPE_SUPER)) {
			list = permissionService.selectList(queryWrapper);
		}else {
			// 普通用户登陆 TODO
			//根据用户ID+角色+权限去查询 
			list = permissionService.selectList(queryWrapper);
		}
		
		List<TreeNode> treeNodes=new ArrayList<>();
		for (Permission p : list) {
			Integer id=p.getId();
			Integer pid=p.getPid();
			String title=p.getTitle();
			String icon=p.getIcon();
			String href=p.getHref();
			Boolean spread=p.getOpen().equals(Constast.OPEN_TRUE)?true:false;
			treeNodes.add(new TreeNode(id, pid, title, icon, href, spread));
		}
		//构造层级关系
		List<TreeNode> list2 = TreeNodeBuilder.build(treeNodes, 1);
		return new DataGridView(list2);
	}
	
}


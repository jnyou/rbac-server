package com.blithe.cms.controller.system;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.*;
import com.blithe.cms.pojo.system.Permission;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.PermissionService;
import com.blithe.cms.service.RoleService;
import com.blithe.cms.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: youjiannan
 * @Description: menu
 * @Date: 2020/3/19
 * @Param:
 * @Return:
 **/
@RestController
@RequestMapping("/menu")
public class MenuController {


	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SysUserService userService;

    /**
     * load index left dtree meun
     * @param permission
     * @return
     */
	@GetMapping("/loadIndexLeftMenuJson")
	public DataGridView loadIndexLeftMenuJson(Permission permission) {
		//查询所有菜单
		EntityWrapper<Permission> queryWrapper=new EntityWrapper<>();
		//设置只能查询菜单
		queryWrapper.eq("type", Constast.TYPE_MNEU);
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		
		SysUser user = (SysUser) HttpContextUtils.getHttpServletRequest().getSession().getAttribute("user");
		List<Permission> list=null;
		// 判断用户类型
		if (Constast.USER_TYPE_SUPER.equals(user.getType())) {
			// 超级管理员登陆
			list = permissionService.selectList(queryWrapper);
		} else {
			// 普通用户登陆
			//根据用户ID+角色+权限去查询
			Integer uid = user.getId();
			List<Integer> rids = userService.selectRidByUid(uid);
			//去重查询权限和菜单id
			Set<Object> dinSet = new HashSet<>();
			for (Integer rid : rids) {
				dinSet.addAll(roleService.queryRolePermissionIdsByRid(rid));
			}
			if(CollectionUtils.isNotEmpty(dinSet)){
				queryWrapper.in("id",dinSet);
				list = permissionService.selectList(queryWrapper);
			}
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

/*****************************************menu manager start***********************************************/
	/**
     * load menu manager left dtree  JSON data
	 * @return
	 */
	@PostMapping("/loadMenuLeftDtreeJson")
	public DataGridView loadMenuLeftDtreeJson() {
		//设置只能查询菜单
		List<Permission> permissionList = this.permissionService.selectList(new EntityWrapper().eq("type", Constast.TYPE_MNEU));
		List<TreeNode> treeNodes = new ArrayList<>();
		for (Permission Permission : permissionList) {
			Boolean openFlag = Permission.getOpen() == 1 ? true: false;
			treeNodes.add(new TreeNode(Permission.getId(),Permission.getPid(),Permission.getTitle(),openFlag));
		}
		return new DataGridView(treeNodes);
	}


	/**
	 * query menu
	 * @param Permission
	 * @param params
	 * @return
	 */
	@GetMapping("/list")
	public R queryMenuList(Permission permission, Map<String,Object> params){

		Permission permissionNew = (Permission)params.get("permission");
		Page page = new Page<>(permissionNew.getPage(),permissionNew.getLimit(),"ordernum",true);
		Wrapper wrapper = new EntityWrapper();
		wrapper.like(StringUtils.isNotBlank(permission.getTitle()),"title",permission.getTitle());
        wrapper.eq("type", Constast.TYPE_MNEU);
		// 接受前台left参数进行渲染数据表格
        if(permission.getId()!=null){
            wrapper.eq("id",permission.getId()).or().eq("pid",permission.getId());
        }
		Page pages = this.permissionService.selectPage(page, wrapper);

		return R.ok().put("count",pages.getTotal()).put("data",pages.getRecords());

	}


	@PostMapping("/checkMenuChildrenDel")
	public R checkMenuChildrenDel(@RequestBody String id){
		try {
			EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("type", Constast.TYPE_MNEU);
			wrapper.eq("pid",id);
			List list = this.permissionService.selectList(wrapper);
			if(CollectionUtils.isNotEmpty(list)){
				return R.ok().put("value",true);
			}else {
                permissionService.deleteById(Integer.parseInt(id));
			}
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }

	/**
	 * save
	 * @param Permission
	 * @return
	 */
	@PostMapping("/menuSaveOrUpdate")
	public R menuSaveOrUpdate(Permission Permission){
		try {
			if(Permission.getId() == null){
				Permission.setType(Constast.TYPE_MNEU);
				this.permissionService.insert(Permission);
			}else{
				this.permissionService.updateById(Permission);
			}
		}catch (Exception e){
			return R.error(e.getMessage());
		}
		return R.ok();
	}

	/**
     * query max ordernum
	 * @return
	 */
	@GetMapping("/loadMenuMaxOrderNum")
	public R loadMenuMaxOrderNum(){
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.eq("type", Constast.TYPE_MNEU);
		wrapper.orderBy("ordernum",false).last("LIMIT 1");
		Permission permission = this.permissionService.selectOne(wrapper);
		if(permission != null && permission.getOrdernum()>0){
			return R.ok().put("error",true).put("value",permission.getOrdernum() + 1);
		}else{
			return R.ok().put("error",false);
		}
	}

/*****************************************menu manager end***********************************************/

}


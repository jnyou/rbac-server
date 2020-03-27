package com.blithe.cms.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.StringUtil;
import com.blithe.cms.pojo.system.Role;
import com.blithe.cms.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description: role
 * @Author: 夏小颜
 * @Date: 16:08
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/role")
public class RoleController {
    
    
    @Autowired
    private RoleService roleService;


    /*****************************************role manager start***********************************************/


    /**
     * query role
     * @param Role
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R queryRoleList(Role role, Map<String,Object> params){
        Page page = null;
        Role roleNew = (Role)params.get("role");

        Wrapper wrapper = new EntityWrapper();
        wrapper.like(StringUtils.isNotBlank(role.getName()),"name",role.getName());
        wrapper.ge(role.getStartTime()!=null,"createtime",role.getStartTime());
        wrapper.le(role.getEndTime()!=null,"createtime",role.getEndTime());

        if(!StringUtil.isNull(roleNew.getPage()) && !StringUtil.isNull(roleNew.getLimit())){
            page = new Page<>(roleNew.getPage(),roleNew.getLimit(),"createtime",false);
            Page pages = roleService.selectPage(page, wrapper);
            return R.ok().put("count",pages.getTotal()).put("data",pages.getRecords());
        }else {
            wrapper.orderBy("createtime",false);
            List list = roleService.selectList(wrapper);
            return R.ok().put("data",list);
        }
    }


    @PostMapping("/delete")
    public R delete(@RequestBody String id){
        try {
            roleService.deleteById(Integer.parseInt(id));
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * save
     * @param Role
     * @return
     */
    @PostMapping("/roleSaveOrUpdate")
    public R roleSaveOrUpdate(Role Role){
        try {
            if(Role.getId() == null){
                Role.setCreatetime(new Date());
                this.roleService.insert(Role);
            }else{
                this.roleService.updateById(Role);
            }
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }


/*****************************************role manager end***********************************************/


}
package com.blithe.cms.controller.system;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.Constast;
import com.blithe.cms.common.tools.DataGridView;
import com.blithe.cms.common.tools.PinyinUtils;
import com.blithe.cms.pojo.system.Dept;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.DeptService;
import com.blithe.cms.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (SysUser)表控制层
 *
 * @author makejava
 * @since 2020-03-19 09:55:08
 */
@RestController
@RequestMapping("sysUser")
public class UserController {
    /**
     * 服务对象
     */
    @Autowired
    private SysUserService userService;

    @Autowired
    private DeptService deptService;

    /**
     *  查询用户
     * @param user
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R querySysUserList(SysUser sysUser, Map<String,Object> params){

        SysUser userNew = (SysUser)params.get("sysUser");
        Page page = new Page<>(userNew.getPage(),userNew.getLimit(),"ordernum",true);
        EntityWrapper wrapper = new EntityWrapper();
        // 用户名或者登录名
        wrapper.like(StringUtils.isNotBlank(sysUser.getName()), "loginname", sysUser.getName()).or().like(StringUtils.isNotBlank(sysUser.getName()), "name", sysUser.getName());

        wrapper.eq(StringUtils.isNotBlank(sysUser.getAddress()), "address", sysUser.getAddress());
        // 查询系统用户
        wrapper.eq("type", Constast.USER_TYPE_NORMAL);
        // 部门id
        wrapper.eq(sysUser.getDeptid()!=null,"deptid",sysUser.getDeptid());
        Page pages = this.userService.selectPage(page, wrapper);

        List<SysUser> sysUsers = pages.getRecords();
        // 处理部门名称和上级名称
        if(CollectionUtils.isNotEmpty(sysUsers)){
            for (SysUser user : sysUsers) {
                Integer deptid = user.getDeptid();
                if(deptid != null){
                    Dept dept = deptService.selectById(deptid);
                    user.setDepeName(dept.getTitle());
                }
                Integer mgr = user.getMgr();
                if(mgr != null){
                    user.setMgrName(this.userService.selectById(mgr).getName());
                }
            }
        }
        return R.ok().put("count",pages.getTotal()).put("data",sysUsers);

    }


    @PostMapping("/deleteUser")
    public R deleteUser(Integer id){
        try {
            this.userService.deleteById(id);
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 保存
     * @param user
     * @return
     */
    @PostMapping("/userSaveOrUpdate")
    public R userSaveOrUpdate(SysUser user){
        try {
            if(user.getId() == null){
                //设置类型
                user.setType(Constast.USER_TYPE_NORMAL);
                user.setHiredate(new Date());
                String salt= IdUtil.simpleUUID().toUpperCase();
                //设置盐
                user.setSalt(salt);
                //设置密码
                user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD, salt, 2).toString());
                this.userService.insert(user);
            }else{
                this.userService.updateById(user);
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
    @GetMapping("/loadUserMaxOrderNum")
    public R loadUserMaxOrderNum(){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.orderBy("ordernum",false).last("LIMIT 1");
        SysUser sysUser = this.userService.selectOne(wrapper);
        if(sysUser != null && sysUser.getOrdernum()>0){
            return R.ok().put("error",true).put("value",sysUser.getOrdernum() + 1);
        }else{
            return R.ok().put("error",false);
        }
    }

    /**
     * 根据部门ID查询用户
     */
    @RequestMapping("/loadUsersByDeptId")
    public R loadUsersByDeptId(Integer deptid) {
        EntityWrapper<SysUser> wrapper=new EntityWrapper<>();
        wrapper.eq(deptid!=null, "deptid", deptid);
        wrapper.eq("available", Constast.AVAILABLE_TRUE);
        wrapper.eq("type", Constast.USER_TYPE_NORMAL);
        List<SysUser> list = this.userService.selectList(wrapper);
        return R.ok().put("data",list);
    }

    /**
     * 根据用户ID查询一个用户
     */
    @RequestMapping("/loadUserById")
    public DataGridView loadUserById(Integer id) {
        return new DataGridView(this.userService.selectById(id));
    }

    /**
     * 把用户中文名转成拼音
     */
    @RequestMapping("/changeChineseToPinyin")
    public R changeChineseToPinyin(String username){
        if(null!=username) {
            return R.ok().put("value", PinyinUtils.getPingYin(username));
        }else {
            return R.ok().put("value", "");
        }
    }

}
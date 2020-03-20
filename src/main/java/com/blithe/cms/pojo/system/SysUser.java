package com.blithe.cms.pojo.system;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_user
 * @author 
 */
@Data
public class SysUser implements Serializable {
    private Integer id;

    private String name;

    private String loginname;

    private String address;

    private Integer sex;

    private String remark;

    private String pwd;

    private Integer deptid;

    private Date hiredate;

    private Integer mgr;

    private Integer available;

    private Integer ordernum;

    /**
     * 用户类型[0超级管理员1，管理员，2普通用户]
     */
    private Integer type;

    /**
     * 头像地址
     */
    private String imgpath;

    private String salt;

    private static final long serialVersionUID = 1L;
}
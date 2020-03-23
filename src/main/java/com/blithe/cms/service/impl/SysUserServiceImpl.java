package com.blithe.cms.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blithe.cms.common.tools.MD5;
import com.blithe.cms.mapper.system.SysUserMapper;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (SysUser)表服务实现类
 *
 * @author makejava
 * @since 2020-03-19 09:55:47
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
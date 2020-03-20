package com.blithe.cms.mapper.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.blithe.cms.pojo.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {
    int deleteByPrimaryKey(Integer id);

    Integer insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser queryNameAndcode(@Param("loginname")String loginname, @Param("pwd")String pwd);
}
package com.blithe.cms.service.impl;

import java.io.Serializable;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blithe.cms.mapper.system.DeptMapper;
import com.blithe.cms.pojo.system.Dept;
import com.blithe.cms.service.DeptService;
import org.springframework.stereotype.Service;



/**
 * 部门管理
 * @author 夏小颜
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

	@Override
	public Dept selectOne(Wrapper<Dept> wrapper) {
		return super.selectOne(wrapper);
	}

	@Override
	public boolean updateById(Dept entity) {
		return super.updateById(entity);
	}

	@Override
	public boolean deleteById(Serializable id) {
		return super.deleteById(id);
	}

}

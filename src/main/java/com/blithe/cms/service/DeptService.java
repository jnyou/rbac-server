package com.blithe.cms.service;


import com.baomidou.mybatisplus.service.IService;
import com.blithe.cms.common.tools.DataGridView;
import com.blithe.cms.pojo.system.Dept;

import java.io.Serializable;

/**
 * 部门管理
 * @author 夏小颜
 */
public interface DeptService extends IService<Dept> {

    /**
     * 查询
     * @param id
     * @return
     */
    @Override
    Dept selectById(Serializable id);

    /**
     * 更新
     * @param dept
     * @return
     */
    @Override
    boolean updateById(Dept dept);

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean deleteById(Serializable id);

    /**
     * 部门树
     * @param
     * @return
     */
    DataGridView loadDeptLeftDtreeJson();

}

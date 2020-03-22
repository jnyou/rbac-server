package com.blithe.cms.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blithe.cms.common.tools.DataGridView;
import com.blithe.cms.common.tools.TreeNode;
import com.blithe.cms.pojo.system.Dept;
import com.blithe.cms.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DeptController
 * @Description: 部门管理
 * @Author: 夏小颜
 * @Date: 19:12
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/dept")
public class DeptController {
    
    @Autowired
    private DeptService deptService;

    /**
     * 加载部门管理dtree JSON数据
     * @return
     */
    @RequestMapping("/loadDeptLeftDtreeJson")
    public DataGridView loadDeptLeftDtreeJson(){

        List<Dept> deptList = this.deptService.selectList(new EntityWrapper<>());
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Dept dept : deptList) {
            Boolean openFlag = dept.getOpen() == 1 ? true: false;
            treeNodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),openFlag));
        }
        return new DataGridView(treeNodes);
    }
    
    
}
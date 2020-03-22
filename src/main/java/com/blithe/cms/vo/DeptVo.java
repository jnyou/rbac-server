package com.blithe.cms.vo;


import com.blithe.cms.pojo.system.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门管理扩展
 * @author 夏小颜
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DeptVo extends Dept {

	private static final long serialVersionUID = 1L;

	
	private Integer page=1;
	private Integer limit=10;
	
}

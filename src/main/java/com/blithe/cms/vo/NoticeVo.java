package com.blithe.cms.vo;

import java.util.Date;

import com.blithe.cms.pojo.system.Notice;
import org.springframework.format.annotation.DateTimeFormat;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 夏小颜
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class NoticeVo extends Notice {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Integer page=1;
	private Integer limit=20;
	/**
	 * 接收多个ID
	 */
	private Integer[] ids;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}

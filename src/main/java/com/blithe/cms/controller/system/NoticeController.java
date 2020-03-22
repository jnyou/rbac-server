package com.blithe.cms.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.HttpContextUtils;
import com.blithe.cms.pojo.system.Notice;
import com.blithe.cms.pojo.system.SysUser;
import com.blithe.cms.service.NoticeService;
import com.blithe.cms.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName NoticeController
 * @Description: 系统公告模块
 * @Author: 夏小颜
 * @Date: 18:38
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public R queryNoticeList(NoticeVo noticeVo, Map<String,Object> params){
        NoticeVo noticeVoNew = (NoticeVo)params.get("noticeVo");
        Page<Notice> page = new Page<Notice>(noticeVoNew.getPage(),noticeVoNew.getLimit(),"createtime",false);
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        wrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        wrapper.ge(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        wrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());
        Page pages = noticeService.selectPage(page, wrapper);
        return R.ok().put("count",pages.getTotal()).put("data",pages.getRecords());
    }

    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestParam("params") String params){
        try {
            if(params.length() > 0){
                String[] ids = params.split(",");
                noticeService.deleteBatchIds(Arrays.asList(ids));
            }
        }catch (Exception e){
            return R.error(e.getMessage());
        }

        return R.ok();
    }


    @PostMapping("/noticeSaveOrUpdate")
    public R noticeSaveOrUpdate(Notice notice){
        try {
            if(notice.getId() == null){
                notice.setCreatetime(new Date());
                SysUser user = (SysUser) HttpContextUtils.getHttpServletRequest().getSession().getAttribute("user");
                notice.setOpername(user.getName());
                this.noticeService.insert(notice);
            }else{
                this.noticeService.updateById(notice);
            }
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }

}
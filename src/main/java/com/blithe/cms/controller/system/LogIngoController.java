package com.blithe.cms.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.common.tools.Query;
import com.blithe.cms.common.tools.StringUtil;
import com.blithe.cms.service.LoginfoService;
import com.blithe.cms.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LogIngoController
 * @Description: 登陆日志信息controller
 * @Author: 夏小颜
 * @Date: 12:29
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/log")
public class LogIngoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 查询
     * @return
     */
    @RequestMapping("/list")
    public R queryLogList(LoginfoVo loginfoVo, Map<String,Object> params) throws Exception {
        LoginfoVo loginfoVoNew = (LoginfoVo)params.get("loginfoVo");
        Page page = new Page(loginfoVoNew.getPage(),loginfoVoNew.getLimit());
        EntityWrapper queryWrapper = new EntityWrapper<>();
        // 条件构造b
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()),"loginname",loginfoVo.getLoginname());
        queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()),"loginip",loginfoVo.getLoginip());
        queryWrapper.ge(loginfoVo.getStartTime()!=null,"logintime",loginfoVo.getStartTime());
        queryWrapper.le(loginfoVo.getEndTime()!=null,"logintime",loginfoVo.getEndTime());
        queryWrapper.orderBy("logintime",false);
        Page pages = loginfoService.selectPage(page, queryWrapper);
        return R.ok().put("count",pages.getTotal()).put("data",pages.getRecords());
    }


    @RequestMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Map<String,Object>> params){
        try {
            if(params.size()>0){
                for (Map<String, Object> param : params) {
                    this.loginfoService.deleteByMap(param);
                }
            }
        }catch (Exception e){
            return R.error(e.getMessage());
        }
        return R.ok();
    }

}
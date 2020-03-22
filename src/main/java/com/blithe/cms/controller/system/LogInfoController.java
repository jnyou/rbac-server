package com.blithe.cms.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blithe.cms.common.exception.R;
import com.blithe.cms.service.LoginfoService;
import com.blithe.cms.vo.LoginfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class LogInfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 查询
     * @return
     */
    @GetMapping("/list")
    public R queryLogList(LoginfoVo loginfoVo, Map<String,Object> params) {
        LoginfoVo loginfoVoNew = (LoginfoVo)params.get("loginfoVo");
        Page page = new Page(loginfoVoNew.getPage(),loginfoVoNew.getLimit(),"logintime",false);
        EntityWrapper wrapper = new EntityWrapper<>();
        // 条件构造b
        wrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()),"loginname",loginfoVo.getLoginname());
        wrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()),"loginip",loginfoVo.getLoginip());
        wrapper.ge(loginfoVo.getStartTime()!=null,"logintime",loginfoVo.getStartTime());
        wrapper.le(loginfoVo.getEndTime()!=null,"logintime",loginfoVo.getEndTime());
        Page pages = this.loginfoService.selectPage(page, wrapper);
        return R.ok().put("count",pages.getTotal()).put("data",pages.getRecords());
    }


    @PostMapping(value = "/deleteBatch")
    public R deleteBatch(@RequestBody List<Map<String,Object>> params){
        try {
            if(CollectionUtils.isNotEmpty(params)){
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
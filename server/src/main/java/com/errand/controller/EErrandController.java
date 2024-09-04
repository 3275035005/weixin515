package com.errand.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.errand.entity.EErrand;
import com.errand.entity.SysUser;
import com.errand.entity.query.EErrandQuery;
import com.errand.service.EErrandService;
import com.errand.service.SysUserService;
import com.errand.utils.ResultSet;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 跑腿代购信息表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/errand")
public class EErrandController {


    @Autowired
    private EErrandService eErrandService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页条件查询
     * @param page
     * @param limit
     * @param eErrandQuery
     * @return
     */
    @PostMapping("pageQuery/{page}/{limit}")
    public ResultSet pageTeacherCondition(@PathVariable long page,
                                          @PathVariable long limit,
                                          @RequestBody EErrandQuery eErrandQuery){

        QueryWrapper<EErrand> qw = new QueryWrapper<>();
        LambdaQueryWrapper<EErrand> lambda = qw.lambda();

        // 按物品名称查询
        if(!Strings.isEmpty(eErrandQuery.getName())){
            lambda.like(EErrand::getName, eErrandQuery.getName());
        }

        // 按联系方式查询
        if(!Strings.isEmpty(eErrandQuery.getPhone())){
            lambda.like(EErrand::getPhone, eErrandQuery.getPhone());
        }

        // 按处理状态查询
        if(!Strings.isEmpty(eErrandQuery.getStatus())){
            lambda.like(EErrand::getStatus, eErrandQuery.getStatus());
        }


        // 按时间排序
        lambda.orderByDesc(EErrand::getCreated);


        Page<EErrand> pageParam = new Page<>(page, limit);

        IPage<EErrand> data = eErrandService.page(pageParam, qw);
        // 获取总条数
        long total = pageParam.getTotal();
        List<EErrand> records = data.getRecords();
        return ResultSet.ok().data("rows",records).data("total",total);
    }


    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResultSet saveAndUpdate(@PathVariable String id){
        eErrandService.removeById(id);

        return ResultSet.ok();
    }



    /**
     * 修改和新增功能
     * @return
     */
    @PostMapping("saveAndUpdate")
    public ResultSet delete( @RequestBody EErrand eErrand){

        if(Strings.isNotEmpty(eErrand.getId())){
            eErrandService.updateById(eErrand);
        }else{
            eErrandService.save(eErrand);
        }
        return ResultSet.ok();
    }


}


package com.errand.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.errand.entity.ELose;
import com.errand.entity.query.ELoseQuery;
import com.errand.service.ELoseService;
import com.errand.utils.ResultSet;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 失物招领信息表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/lose")
public class ELoseController {


    @Autowired
    private ELoseService eLoseService;


    /**
     * 分页条件查询
     * @param page
     * @param limit
     * @param eLoseQuery
     * @return
     */
    @PostMapping("pageQuery/{page}/{limit}")
    public ResultSet pageTeacherCondition(@PathVariable long page,
                                          @PathVariable long limit,
                                          @RequestBody ELoseQuery eLoseQuery){

        QueryWrapper<ELose> qw = new QueryWrapper<>();
        LambdaQueryWrapper<ELose> lambda = qw.lambda();

        // 按物品名称查询
        if(!Strings.isEmpty(eLoseQuery.getName())){
            lambda.like(ELose::getName, eLoseQuery.getName());
        }

        // 按状态查询
        if(!Strings.isEmpty(eLoseQuery.getStatus())){
            lambda.like(ELose::getStatus, eLoseQuery.getStatus());
        }

        // 按联系人查询
        if(!Strings.isEmpty(eLoseQuery.getPhone())){
            lambda.like(ELose::getPhone, eLoseQuery.getPhone());
        }

        // 按时间排序
        lambda.orderByDesc(ELose::getCreated);


        Page<ELose> pageParam = new Page<>(page, limit);

        IPage<ELose> data = eLoseService.page(pageParam, qw);
        // 获取总条数
        long total = pageParam.getTotal();
        List<ELose> records = data.getRecords();

        return ResultSet.ok().data("rows",records).data("total",total);

    }


    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResultSet delete(@PathVariable String id){
        eLoseService.removeById(id);

        return ResultSet.ok();
    }



    /**
     * 修改和新增功能
     * @return
     */
    @PostMapping("saveAndUpdate")
    public ResultSet saveAndUpdate( @RequestBody ELose eLose){

        if(Strings.isNotEmpty(eLose.getId())){
            eLoseService.updateById(eLose);
        }else{
            eLoseService.save(eLose);
        }
        return ResultSet.ok();
    }

}


package com.errand.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.errand.entity.ESecondhand;
import com.errand.entity.query.ESecondhandQuery;
import com.errand.service.ESecondhandService;
import com.errand.utils.ResultSet;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 二手闲置信息表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/secondhand")
public class ESecondhandController {


    @Autowired
    private ESecondhandService eSecondhandService;

    /**
     * 分页条件查询
     * @param page
     * @param limit
     * @param eSecondhandQuery
     * @return
     */
    @PostMapping("pageQuery/{page}/{limit}")
    public ResultSet pageTeacherCondition(@PathVariable long page,
                                          @PathVariable long limit,
                                          @RequestBody ESecondhandQuery eSecondhandQuery){

        QueryWrapper<ESecondhand> qw = new QueryWrapper<>();
        LambdaQueryWrapper<ESecondhand> lambda = qw.lambda();

        // 按物品名称查询
        if(!Strings.isEmpty(eSecondhandQuery.getName())){
            lambda.like(ESecondhand::getName, eSecondhandQuery.getName());
        }

        //  出售状态查询
        if(!Strings.isEmpty(eSecondhandQuery.getStatus())){
            lambda.like(ESecondhand::getName, eSecondhandQuery.getStatus());
        }

        // 按时间排序
        lambda.orderByDesc(ESecondhand::getCreated);


        Page<ESecondhand> pageParam = new Page<>(page, limit);

        IPage<ESecondhand> data = eSecondhandService.page(pageParam, qw);
        // 获取总条数
        long total = pageParam.getTotal();
        List<ESecondhand> records = data.getRecords();

        return ResultSet.ok().data("rows",records).data("total",total);

    }


    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResultSet delete(@PathVariable String id){
        eSecondhandService.removeById(id);

        return ResultSet.ok();
    }



    /**
     * 修改和新增功能
     * @return
     */
    @PostMapping("saveAndUpdate")
    public ResultSet saveAndUpdate( @RequestBody ESecondhand eSecondhand){

        if(Strings.isNotEmpty(eSecondhand.getId())){
            eSecondhandService.updateById(eSecondhand);
        }else{
            eSecondhandService.save(eSecondhand);
        }
        return ResultSet.ok();
    }
}


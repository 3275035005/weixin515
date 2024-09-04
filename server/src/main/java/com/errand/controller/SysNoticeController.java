package com.errand.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.errand.entity.SysNotice;
import com.errand.entity.query.SysNoticeQuery;
import com.errand.service.SysNoticeService;
import com.errand.utils.ResultSet;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 公告信息表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController {


    @Autowired
    private SysNoticeService sysNoticeService;

    /**
     * 分页条件查询
     *
     * @param page
     * @param limit
     * @param sysNoticeQuery
     * @return
     */
    @PostMapping("pageQuery/{page}/{limit}")
    public ResultSet pageTeacherCondition(@PathVariable long page,
                                          @PathVariable long limit,
                                          @RequestBody SysNoticeQuery sysNoticeQuery) {

        QueryWrapper<SysNotice> qw = new QueryWrapper<>();
        LambdaQueryWrapper<SysNotice> lambda = qw.lambda();
        if (!Strings.isEmpty(sysNoticeQuery.getContent())) {
            lambda.like(SysNotice::getContent, sysNoticeQuery.getContent());
        }
        // 按时间排序
        lambda.orderByDesc(SysNotice::getCreated);


        Page<SysNotice> pageParam = new Page<>(page, limit);

        IPage<SysNotice> data = sysNoticeService.page(pageParam, qw);
        // 获取总条数
        long total = pageParam.getTotal();
        List<SysNotice> records = data.getRecords();

        return ResultSet.ok().data("rows", records).data("total", total);

    }


    /**
     * 修改和新增功能
     *
     * @return
     */
    @PostMapping("saveAndUpdate")
    public ResultSet saveAndUpdate(@RequestBody SysNotice sysNotice) {
        sysNoticeService.updateById(sysNotice);
        return ResultSet.ok();
    }

}


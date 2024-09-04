package com.errand.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.errand.entity.SysUser;
import com.errand.entity.query.SysUserQuery;
import com.errand.service.SysUserService;
import com.errand.utils.ResultSet;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/user")
public class SysUserController {


    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页条件查询
     * @param page
     * @param limit
     * @param sysUserQuery
     * @return
     */
    @PostMapping("pageQuery/{page}/{limit}")
    public ResultSet pageTeacherCondition(@PathVariable long page,
                                          @PathVariable long limit,
                                          @RequestBody SysUserQuery sysUserQuery){

        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        LambdaQueryWrapper<SysUser> lambda = qw.lambda();
        if(!Strings.isEmpty(sysUserQuery.getUsername())){
            lambda.like(SysUser::getUsername, sysUserQuery.getUsername());
        }
        if(!Strings.isEmpty(sysUserQuery.getSex())){
            lambda.like(SysUser::getSex, sysUserQuery.getSex());
        }
        // 按时间排序
        lambda.orderByDesc(SysUser::getCreated);


        Page<SysUser> pageParam = new Page<>(page, limit);

        IPage<SysUser> data = sysUserService.page(pageParam, qw);
        // 获取总条数
        long total = pageParam.getTotal();
        List<SysUser> records = data.getRecords();

        return ResultSet.ok().data("rows",records).data("total",total);

    }


    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResultSet delete(@PathVariable String id){
        sysUserService.removeById(id);

        return ResultSet.ok();
    }

    @GetMapping("getAll")
    public ResultSet getAll(){

        List<SysUser> list = sysUserService.list(null);

        return ResultSet.ok().data("rows", list);
    }


    /**
     * 修改和新增功能
     * @return
     */
    @PostMapping("saveAndUpdate")
    public ResultSet saveAndUpdate( @RequestBody SysUser sysUser){

        if(Strings.isNotEmpty(sysUser.getId())){
            sysUserService.updateById(sysUser);
        }else{
            sysUser.setIsDelete(false);
            sysUserService.save(sysUser);
        }
        return ResultSet.ok();
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public ResultSet login(@RequestBody SysUser sysUser) {

        SysUser username = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", sysUser.getUsername()));

        if (username == null) {
            return ResultSet.error().message("账号不正确!");
        }
        if(!username.getPassword().equals(sysUser.getPassword())){
            return ResultSet.error().message("密码不正确!");
        }

        // 判断账号是否是禁用状态
        if(username.getStatus().equals("0")){
            return ResultSet.error().message("账号已禁用!");
        }

        if(username.getRole().equals("1")){
            return ResultSet.error().message("请使用管理员账号登录!");
        }
        return ResultSet.ok().data("token","token11111");
    }
}


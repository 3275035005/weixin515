package com.errand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.errand.entity.*;
import com.errand.entity.Vo.EErrandVo;
import com.errand.service.*;
import com.errand.utils.AceUtils;
import com.errand.utils.ResultSet;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  对微信小程序提供接口
 * </p>
 *
 */
@RestController
@RequestMapping("/wx")
public class WXController{


    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SysNoticeService sysNoticeService;


    @Autowired
    private EErrandService eErrandService;

    @Autowired
    private ELoseService eLoseService;

    @Autowired
    private ESecondhandService eSecondhandService;


    /**
     * 获取我的招领信息, 二手闲置信息, 失物招领信息
     */
    @GetMapping("getAll")
    public ResultSet getAll(String id){

        // 招领信息
        QueryWrapper<ELose> qw1 = new QueryWrapper<>();
        qw1.eq("user_id",id);
        List<ELose> list1 = eLoseService.list(qw1);
        for (ELose eLose : list1) {
            SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eLose.getUserId()));
            eLose.setUserId(sysUser.getName());
        }

        // 二手闲置
        QueryWrapper<ESecondhand> qw2 = new QueryWrapper<>();
        qw2.eq("user_id",id);
        List<ESecondhand> list2 = eSecondhandService.list(qw2);
        for (ESecondhand eSecondhand : list2) {
            SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eSecondhand.getUserId()));
            eSecondhand.setUserId(sysUser.getName());
        }

        // 跑腿代购
        QueryWrapper<EErrand> qw3 = new QueryWrapper<>();
        qw3.eq("user_id",id);
        List<EErrand> list3 = eErrandService.list(qw3);
        List<EErrandVo> list4 = new ArrayList<>();
        for (EErrand eErrand : list3) {

            SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eErrand.getUserId()));
            eErrand.setUserId(sysUser.getName());
            EErrandVo eErrandVo = new EErrandVo();
            eErrandVo.setImage(sysUser.getImage());
            eErrandVo.setSex(sysUser.getSex().equals("1") ?"男":"女");
            eErrandVo.setUserName(sysUser.getName());
            BeanUtils.copyProperties(eErrand,eErrandVo);
            list4.add(eErrandVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("eLose",list1);
        map.put("eSecondhand",list2);
        map.put("eErrand",list4);
        return ResultSet.ok().data("rows", map);
    }
    /**
     * 查询所有失误招领信息
     */
    @GetMapping("getLose")
    public ResultSet getLose(String name){

        QueryWrapper<ELose> qw = new QueryWrapper<>();
        if(Strings.isNotEmpty(name)){
            qw.eq("name",name);
        }
        List<ELose> list = eLoseService.list(qw);
        for (ELose eLose : list) {
            SysUser id = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eLose.getUserId()));
            eLose.setUserId(id.getName());
        }
        return ResultSet.ok().data("rows", list);
    }

    /**
     * 查询所有二手闲置信息
     */
    @GetMapping("getSecondhand")
    public ResultSet getSecondhand(String name){

        QueryWrapper<ESecondhand> qw = new QueryWrapper<>();
        if(Strings.isNotEmpty(name)){
            qw.eq("name",name);
        }
        List<ESecondhand> list = eSecondhandService.list(qw);
        for (ESecondhand eSecondhand : list) {
            SysUser id = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eSecondhand.getUserId()));
            eSecondhand.setUserId(id.getName());
        }
        return ResultSet.ok().data("rows", list);
    }

    /**
     * 查询所有失物招领信息
     */
    @GetMapping("getErrand")
    public ResultSet getErrand(){
        List<EErrand> list = eErrandService.list(null);
        List<EErrandVo> list1 = new ArrayList<>();
        for (EErrand eErrand : list) {

            SysUser id = sysUserService.getOne(new QueryWrapper<SysUser>().like("id", eErrand.getUserId()));
            eErrand.setUserId(id.getName());
            EErrandVo eErrandVo = new EErrandVo();
            eErrandVo.setImage(id.getImage());
            eErrandVo.setSex(id.getSex().equals("1") ?"男":"女");
            eErrandVo.setUserName(id.getName());
            BeanUtils.copyProperties(eErrand,eErrandVo);
            list1.add(eErrandVo);
        }
        return ResultSet.ok().data("rows", list1);
    }

    /**
     * 查询首页公告
     */
    @GetMapping("getContent")
    public ResultSet getContent(){
        SysNotice sysNotice = sysNoticeService.getOne(new QueryWrapper<SysNotice>().last("limit 1"));
        return ResultSet.ok().data("row", sysNotice);
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

        if(username.getRole().equals("0")){
            return ResultSet.error().message("请使用用户账号登录!");
        }

        return ResultSet.ok().data("token",username);
    }

}

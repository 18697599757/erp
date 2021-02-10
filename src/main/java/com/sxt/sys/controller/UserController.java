package com.sxt.sys.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.Constast;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.PinYinUtils;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Role;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.service.RoleService;
import com.sxt.sys.service.UserService;
import com.sxt.sys.vo.RoleVo;
import com.sxt.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mike
 * @since 2020-11-30
 */
@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;



    //查询用户
    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo) {
        IPage<User> page = new Page<>(userVo.getPage(), userVo.getLimit());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()),"loginname",userVo.getName()).or().eq(StringUtils.isNotBlank(userVo.getName()),"name",userVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()),"address",userVo.getAddress());

        queryWrapper.eq("type", Constast.USER_TYPE_NOMAL);
        queryWrapper.eq(userVo.getDeptid()!=null,"deptid",userVo.getDeptid());
        this.userService.page(page,queryWrapper);

        List<User> list=page.getRecords();
        for (User user : list) {
            Integer deptid = user.getDeptid();
            if (deptid!=null){
                Dept one = deptService.getById(deptid);
                user.setDeptname(one.getTitle());
            }

            Integer mgr = user.getMgr();
            if (mgr!=null){
                User one = this.userService.getById(mgr);
                user.setLeadername(one.getName());
            }
        }


        return new DataGridView(page.getTotal(), list);


    }


    //加载最大排序码
    @RequestMapping("loadUserMaxOrderNum")
    public Integer loadUserMaxOrderNum(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        queryWrapper.last("limit 1");
        User one = this.userService.getOne(queryWrapper);
        Integer i = one.getOrdernum() + 1;
        return i;
    }

    //根据部门Id查询用户
    @RequestMapping("loadUserByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(deptid!=null,"deptid",deptid);
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
        queryWrapper.eq("type",Constast.USER_TYPE_NOMAL);
        List<User> list = userService.list(queryWrapper);
       return new DataGridView(list);
    }
    //把用户名转成拼音
    @RequestMapping("changeChineseToPinYin")
    public Map<String,Object> changeChineseToPinYin(String username){
        Map<String,Object> map =new HashMap<>();
        if (username!=null){
            map.put("value", PinYinUtils.getPingYin(username));
        }else {
            map.put("value","");
        }
        return map;
    }

    //添加用户
    @RequestMapping("addUser")
    public ResultObj addUser(UserVo userVo){
        try {
            System.out.println(userVo.getHiredate());
            userVo.setType(Constast.USER_TYPE_NOMAL);
            String salt= IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);
            userVo.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());
            this.userService.save(userVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    //根据用户ID查询一个用户
    @RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id){
        return new DataGridView(this.userService.getById(id));
    }

    //修改用户
    @RequestMapping("updateUser")
    public ResultObj updateUser(UserVo userVo){
        try {
            this.userService.updateById(userVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    @RequestMapping("deleteUser")
    public ResultObj deleteUser(Integer id){
        try {
            this.userService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    @RequestMapping("resetPwd")
    public ResultObj resetPwd(Integer id){
        try {
            User user=new User();
            user.setId(id);
            String salt = IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);
            user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());
            this.userService.updateById(user);
            return ResultObj.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.RESET_ERROR;
        }
    }

    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id){
        //1.查询所有可用的角色
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
        List<Map<String, Object>> listMaps = this.roleService.listMaps(queryWrapper);
        //2.查询当前用户拥有的角色Id集合
        List<Integer> currentUserRoleIds = this.roleService.queryUserRoleIdsByUid(id);
        for (Map<String, Object> map : listMaps) {
            Boolean LAY_CHECKED=false;
            Integer roleId = (Integer) map.get("id");
            for (Integer rid : currentUserRoleIds) {
                if (rid==roleId){
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(listMaps.size()),listMaps);

    }

    @RequestMapping("saveUserRole")
    public ResultObj saveUserRole (Integer uid,Integer[] ids){
        try {
            this.userService.savaUserRole(uid,ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }

    }
}


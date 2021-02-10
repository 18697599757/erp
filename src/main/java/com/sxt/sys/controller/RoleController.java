package com.sxt.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.*;
import com.sxt.sys.domain.Permission;
import com.sxt.sys.domain.Role;
import com.sxt.sys.domain.User;
import com.sxt.sys.service.PermissionService;
import com.sxt.sys.service.RoleService;
import com.sxt.sys.vo.LoginfoVo;
import com.sxt.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mike
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    public RoleService roleService;

    @Autowired
    public PermissionService permissionService;

     /*
    查询
     */

    @RequestMapping("loadAllRole")
    public DataGridView loadAllRole(RoleVo roleVo) {
        IPage<Role> page = new Page<>(roleVo.getPage(), roleVo.getLimit());

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()), "name", roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()), "remark", roleVo.getRemark());
        queryWrapper.eq(roleVo.getAvailable() != null, "available", roleVo.getAvailable());

        queryWrapper.orderByDesc("createtime");
        this.roleService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());


    }

    /*
        添加
         */
    @RequestMapping("addRole")
    public ResultObj addRole(RoleVo roleVo) {
        try {

            roleVo.setCreatetime(new Date());
            this.roleService.save(roleVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /*
       修改
        */
    @RequestMapping("updateRole")
    public ResultObj updateRole(RoleVo roleVo) {
        try {
            this.roleService.updateById(roleVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }


    /*
     * 删除
     * */
    @RequestMapping("deleteRole")
    public ResultObj deleteRole(Integer id) {
        try {
            this.roleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    //根据角色id加载权限树
    @RequestMapping("initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer roleId){
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        //查询所有可用的权限
        List<Permission> allPermission = permissionService.list(queryWrapper);
        /*
        根据角色ID查询当前所有角色拥有的权限

         */
        List<Integer> currentRolePermissions= this.roleService.queryRolePermissionIdsByRid(roleId);
        List<Permission> carrentPermissions=null;
        if (currentRolePermissions.size()>0){
            queryWrapper.in("id",currentRolePermissions);
            carrentPermissions=permissionService.list(queryWrapper);
        }else {
            carrentPermissions=new ArrayList<>();
        }

        List<TreeNode> nodes=new ArrayList<>();
        for (Permission p1 : allPermission) {
            String checkArr="0";
            for (Permission p2 : carrentPermissions) {
                if (p1.getId()==p2.getId()){
                    checkArr="1";
                    break;
                }
            }
            Boolean spread=(p1.getOpen()==null||p1.getOpen()==1)?true:false;
            nodes.add(new TreeNode(p1.getId(),p1.getPid(),p1.getTitle(),spread,checkArr));

        }
        return new DataGridView(nodes);

    }



    /*
    保存角色和菜单权限之间的关系
     */

    @RequestMapping("saveRolePermission")
    public ResultObj saveRolePermission(Integer rid,Integer[] ids){
        try {
            this.roleService.saveRolePermission(rid,ids);
            return ResultObj.DISPATCH_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }
    }


}


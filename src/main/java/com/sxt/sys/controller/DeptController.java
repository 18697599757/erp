package com.sxt.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.TreeNode;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Notice;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.vo.DeptVo;
import com.sxt.sys.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.metadata.ItemHint;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mike
 * @since 2020-12-08
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo){
        List<Dept> list= this.deptService.list();
        List<TreeNode> treeNodes=new ArrayList<>();
        for (Dept dept : list) {

            Boolean spread = dept.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),spread));
        }
        return new DataGridView(treeNodes);
    }


    /*
    查询
     */
    @RequestMapping("loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo) {
        IPage<Dept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()), "title", deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()), "address", deptVo.getAddress());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()), "remark", deptVo.getRemark());
        queryWrapper.eq(deptVo.getId()!=null,"id",deptVo.getId()).or().eq(deptVo.getId()!=null,"pid",deptVo.getId());
        queryWrapper.orderByAsc("ordernum");
        this.deptService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());


    }
    /*
    添加
     */
    @RequestMapping("addDept")
    public ResultObj addDept(DeptVo deptVo){
        if (deptVo.getTitle().isEmpty()){
            return ResultObj.ADD_ERROR;
        }
        try {
            deptVo.setCreatetime(new Date());
            this.deptService.save(deptVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /*
    修改
     */
    @RequestMapping("updateDept")
    public ResultObj updateDept(DeptVo deptVo){
        try {
            this.deptService.updateById(deptVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    /*
    查询最大的排序码
     */
    @RequestMapping("loadDeptMaxOrderNum")
    public Integer loadDeptMaxOrderNum(){
        QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        queryWrapper.last("limit 1");
        Dept one = this.deptService.getOne(queryWrapper);
        Integer i = one.getOrdernum() + 1;
        return i;
    }

    /*
    检查是否存在子部门
     */
    @RequestMapping("checkDeptHasChildNode")
    public Map<String,Object> checkDeptHasChildNode(DeptVo deptVo){
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("pid",deptVo.getId());
        List<Dept> list = this.deptService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /*
    删除
     */
    @RequestMapping("deleteDept")
    public ResultObj deleteDept(DeptVo deptVo){
        try {
            this.deptService.removeById(deptVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


}


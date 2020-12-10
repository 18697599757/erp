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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


}


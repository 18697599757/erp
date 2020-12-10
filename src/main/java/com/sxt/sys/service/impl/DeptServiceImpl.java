package com.sxt.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.mapper.DeptMapper;
import com.sxt.sys.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mike
 * @since 2020-12-08
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean update(Dept entity, Wrapper<Dept> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    public Dept getOne(Wrapper<Dept> queryWrapper, boolean throwEx) {
        return super.getOne(queryWrapper, throwEx);
    }
}

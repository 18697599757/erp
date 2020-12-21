package com.sxt.sys.mapper;

import com.sxt.sys.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mike
 * @since 2020-12-07
 */
public interface RoleMapper extends BaseMapper<Role> {


    void deleteRolePermissionByRid(Serializable id);

    void deleteRoleUserByRid(Serializable id);
}

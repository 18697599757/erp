package com.sxt.sys.mapper;

import com.sxt.sys.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mike
 * @since 2020-12-02
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    void deleteRolePermissionByPid(@Param("id") Serializable id);
}

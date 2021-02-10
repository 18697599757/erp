package com.sxt.sys.service;

import com.sxt.sys.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mike
 * @since 2020-12-07
 */
public interface RoleService extends IService<Role> {

    List<Integer> queryRolePermissionIdsByRid(Integer roleId);

    void saveRolePermission( Integer rid, Integer[] ids);


    List<Integer> queryUserRoleIdsByUid(Integer id);
}

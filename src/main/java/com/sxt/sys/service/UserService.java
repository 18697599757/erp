package com.sxt.sys.service;

import com.sxt.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mike
 * @since 2020-11-30
 */
public interface UserService extends IService<User> {


    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    void savaUserRole(Integer uid, Integer[] ids);
}

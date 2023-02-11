package cn.edu.hutb.user.service;

import cn.edu.hutb.pojo.AppUser;

/**
 * @author 田章
 * @description
 * @date 2023/2/10
 */
public interface UserService {

    /**
     * 判断用户是否存在，如果存在则返回用户信息
     */
    AppUser queryMobileIsExist(String mobile);

    /**
     * 创建用户，新增用户记录到数据库
     */
    AppUser createUser(String mobile);
}

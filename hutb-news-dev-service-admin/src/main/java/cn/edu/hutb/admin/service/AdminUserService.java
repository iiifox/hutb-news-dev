package cn.edu.hutb.admin.service;

import cn.edu.hutb.pojo.AdminUser;
import cn.edu.hutb.pojo.bo.NewAdminBO;

/**
 * @author 田章
 * @description
 * @date 2023/2/15
 */
public interface AdminUserService {

    /**
     * 根据username获取管理员信息
     */
    AdminUser getAdminByUsername(String username);

    /**
     * 新增admin用户
     */
    void addAdmin(NewAdminBO bo);
}

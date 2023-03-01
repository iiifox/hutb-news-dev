package cn.edu.hutb.user.service;

import cn.edu.hutb.api.page.PageResult;

import java.util.Date;

public interface AppUserMngService {

    /**
     * 管理员查询用户列表
     */
    PageResult listByCondition(String nickname, Integer status, Date startDate, Date endDate, int page, int pageSize);

    /**
     * 冻结用户账号，或者解除冻结状态
     */
     void freezeUserOrNot(String userId, Integer doStatus);

}

package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.user.AppUserMngControllerApi;
import cn.edu.hutb.constant.PageConsts;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.UserStatus;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.user.service.AppUserMngService;
import cn.edu.hutb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 田章
 * @description 用户管理Controller
 * @date 2023/2/25
 */
@RestController
public class AppUserMngController implements AppUserMngControllerApi {

    @Autowired
    private AppUserMngService appUserMngService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JSONResult queryByCondition(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        System.out.println(startDate);
        System.out.println(endDate);

        // 为空，赋默认值
        page = (page == null) ? PageConsts.DEFAULT_START_PAGE : page;
        pageSize = (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize;
        return JSONResult.ok(appUserMngService.listByCondition(nickname, status, startDate, endDate, page, pageSize));
    }

    @Override
    public JSONResult userDetail(String userId) {
        return JSONResult.ok(userService.getUser(userId));
    }

    @Override
    public JSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.validStatus(doStatus)) {
            return JSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserMngService.freezeUserOrNot(userId, doStatus);

        // 刷新用户状态的两种方式，这里选择第一种方式
        // 1. 删除用户会话，从而保障用户需要重新登录以后再来刷新他的会话状态
        // 2. 查询最新用户的信息，重新放入redis中，做一次更新
        redisTemplate.delete(String.format(RedisConsts.USER_INFO_FORMATTER, userId));
        return JSONResult.ok();
    }
}

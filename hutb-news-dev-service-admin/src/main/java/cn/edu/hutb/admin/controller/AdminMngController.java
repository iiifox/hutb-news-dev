package cn.edu.hutb.admin.controller;

import cn.edu.hutb.admin.service.AdminUserService;
import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.admin.AdminMngControllerApi;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.AdminUser;
import cn.edu.hutb.pojo.bo.AdminLoginBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 田章
 * @description
 * @date 2023/2/15
 */
@RestController
public class AdminMngController extends BaseController
        implements AdminMngControllerApi {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JSONResult login(AdminLoginBO bo, HttpServletResponse response) {
        String username = bo.getUsername();
        String password = bo.getPassword();
        // 用户名和密码不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isBlank(password)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }

        // 查询用户信息
        AdminUser admin = adminUserService.getAdminByUsername(username);
        if (admin == null || !BCrypt.checkpw(password, admin.getPassword())) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        // admin用户登录成功后的基本设置
        loginSetting(admin, response);
        return JSONResult.ok();
    }

    @Override
    public JSONResult checkAdminUnique(String username) {
        if (adminUserService.getAdminByUsername(username) != null) {
            throw new CustomException(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
        return JSONResult.ok();
    }

    /**
     * admin用户登录成功后的基本设置
     */
    private void loginSetting(AdminUser admin, HttpServletResponse response) {
        String adminId = admin.getId();
        // 保存admin用户分布式会话的相关操作
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 保存token到Redis
        redisTemplate.opsForValue()
                .set(String.format(RedisConsts.ADMIN_TOKEN, adminId), token, 30, TimeUnit.DAYS);
        // 保存admin用户id和token到cookie中
        setCookieSevenDays(response, "atoken", token);
        setCookieSevenDays(response, "aid", adminId);
        setCookieSevenDays(response, "aname", admin.getAdminName());
    }
}

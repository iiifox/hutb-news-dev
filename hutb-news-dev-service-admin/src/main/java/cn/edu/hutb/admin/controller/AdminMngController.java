package cn.edu.hutb.admin.controller;

import cn.edu.hutb.admin.service.AdminUserService;
import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.admin.AdminMngControllerApi;
import cn.edu.hutb.constant.PageConsts;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.AdminUser;
import cn.edu.hutb.pojo.bo.AdminLoginBO;
import cn.edu.hutb.pojo.bo.NewAdminBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
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
    private RestTemplate restTemplate;

    @Override
    public JSONResult passwordLogin(AdminLoginBO bo, HttpServletResponse response) {
        // 用户名和密码不能为空
        String username = bo.getUsername();
        String password = bo.getPassword();
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

    @Override
    public JSONResult addAdmin(NewAdminBO bo) {
        // 用户名不能为空
        String username = bo.getUsername();
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        // base64为空，表示不是人脸入库，需要密码和确认密码
        String password = bo.getPassword();
        String confirmPassword = bo.getConfirmPassword();
        if (StringUtils.isBlank(bo.getImg64())) {
            if (StringUtils.isBlank(password) || !password.equals(confirmPassword)) {
                return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        // 校验唯一性后新增管理员信息入库
        checkAdminUnique(username);
        adminUserService.addAdmin(bo);
        return JSONResult.ok();
    }

    @Override
    public JSONResult getAdminList(Integer page, Integer pageSize) {
        // 为空，赋默认值
        page = (page == null) ? PageConsts.DEFAULT_PAGE_NUM : page;
        pageSize = (pageSize == null) ? PageConsts.DEFAULT_PAGE_SIZE : pageSize;
        return JSONResult.ok(adminUserService.listAdmin(page, pageSize));
    }

    @Override
    public JSONResult logout(String adminId, HttpServletResponse response) {
        // 从redis中删除admin的会话token
        redisTemplate.delete(String.format(RedisConsts.ADMIN_TOKEN_FORMATTER, adminId));
        // 从cookie中清理admin登录的相关信息
        deleteCookie(response, "atoken");
        deleteCookie(response, "aid");
        deleteCookie(response, "aname");
        return JSONResult.ok();
    }

    @Override
    public JSONResult faceLogin(AdminLoginBO bo, HttpServletResponse response) {
        // 用户名和人脸信息不能为空
        String username = bo.getUsername();
        String img64 = bo.getImg64();
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isBlank(img64)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

        // 从数据库中查询出faceId
        AdminUser admin = adminUserService.getAdminByUsername(username);
        if (admin == null) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        String faceId = admin.getFaceId();
        if (StringUtils.isBlank(faceId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }

        // 请求文件服务，获取人脸信息的base64数据
        String url = "http://files.hutbnews.com:8004/fs/readFace64InGridFS?faceId=" + faceId;
        // 这里注意：第二个参数类型必须有无参构造器
        JSONResult result = restTemplate.getForObject(url, JSONResult.class);
        String base64DB = (String) result.getData();

        // TODO 调用阿里云人脸比对接口，对可信度进行判断，从而实现人脸登录
        // TODO 需付费，暂未提供实现

        // admin用户登录成功后的基本设置
        loginSetting(admin, response);
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
                .set(String.format(RedisConsts.ADMIN_TOKEN_FORMATTER, adminId), token, 30, TimeUnit.DAYS);
        // 保存admin用户id和token到cookie中
        setCookieSevenDays(response, "atoken", token);
        setCookieSevenDays(response, "aid", adminId);
        setCookieSevenDays(response, "aname", admin.getAdminName());
    }
}

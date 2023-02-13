package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.user.UserControllerApi;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.pojo.bo.UpdateUserInfoBO;
import cn.edu.hutb.pojo.vo.AppUserVO;
import cn.edu.hutb.pojo.vo.UserAccountInfoVO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.user.service.UserService;
import cn.edu.hutb.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 田章
 * @description 用户信息相关Controller
 * @date 2023/2/11
 */
@RestController
public class UserController extends BaseController
        implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public JSONResult getAccountInfo(String userId) {
        // 判断参数不能为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 根据用户userId查询用户信息
        AppUser user = getUser(userId);

        // 返回用户信息
        UserAccountInfoVO infoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, infoVO);
        return JSONResult.ok(infoVO);
    }

    @Override
    public JSONResult updateUserInfo(UpdateUserInfoBO bo, BindingResult result) {
        // 判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        // 执行更新操作
        userService.updateUserInfo(bo);
        return JSONResult.ok();
    }

    @Override
    public JSONResult getUserBasicInfo(String userId) {
        // 判断参数不能为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 根据用户userId查询用户信息
        AppUser user = getUser(userId);

        // 返回用户基本信息
        AppUserVO infoVO = new AppUserVO();
        BeanUtils.copyProperties(user, infoVO);
        return JSONResult.ok(infoVO);
    }

    /**
     * 根据用户id获取用户信息。<br/>
     * 由于用户信息不怎么会变动，因此可以依靠前端的session storage和后端的Redis。
     */
    private AppUser getUser(String userId) {
        // 如果Redis中是包含用户信息，则直接返回（不查询数据库）
        String userJson = redisTemplate.opsForValue()
                .get(String.format(RedisConsts.USER_INFO, userId));
        if (userJson != null) {
            return JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            AppUser user = userService.getUser(userId);
            redisTemplate.opsForValue().set(String.format(RedisConsts.USER_INFO, userId),
                    Objects.requireNonNull(JsonUtils.objectToJson(user)), 30, TimeUnit.DAYS);
            return user;
        }
    }
}

package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.user.PassportControllerApi;
import cn.edu.hutb.component.AliyunComponent;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.UserStatus;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.pojo.bo.RegisterLoginBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.user.service.UserService;
import cn.edu.hutb.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 田章
 * @description 用户通行证（注册、登录、退出登录）
 * @date 2023/2/10
 */
@RestController
public class PassportController extends BaseController
        implements PassportControllerApi {

    @Autowired
    private AliyunComponent aliyunComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    private static final Random RANDOM = new Random();

    @Override
    public JSONResult getSmsCode(String mobile, HttpServletRequest request) {
        // 获取ip
        String ip = IpUtils.getRemoteIp(request);
        // 对ip进行限制，同一个ip在一分钟内不能够重复请求
        if (!Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(String.format(RedisConsts.MOBILE_SMSCODE, ip), ip, 60, TimeUnit.SECONDS))) {
            throw new CustomException(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
        }

        // 生成六位数字的随机验证码
        String code = String.format("%06d", RANDOM.nextInt(1000000));
        // 发送验证码
        try {
            // TODO 阿里云发生短信，暂时注释（发短信需要收费）
            // aliyunComponent.sendSms(mobile, code);
        } catch (Exception e) {
            throw new CustomException(ResponseStatusEnum.MOBILE_ERROR);
        }
        // 把验证码存入Redis，用于后续进行验证
        redisTemplate.opsForValue()
                .set(String.format(RedisConsts.MOBILE_SMSCODE, mobile), code, 5, TimeUnit.MINUTES);
        return JSONResult.ok();
    }

    @Override
    public JSONResult registerLogin(RegisterLoginBO bo, BindingResult result, HttpServletResponse response) {
        // 判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        // 校验验证码是否匹配
        String mobile = bo.getMobile();
        String smsCode = bo.getSmsCode();
        if (!smsCode.equals(redisTemplate.opsForValue().get(String.format(RedisConsts.MOBILE_SMSCODE, mobile)))) {
            throw new CustomException(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 查询数据库，判断该用户是否已经注册
        AppUser user = userService.getUserByMobile(mobile);
        // 用户没有注册过，需要注册信息入库
        if (user == null) {
            user = userService.saveUser(mobile);
        } else if (UserStatus.FROZEN.type.equals(user.getActiveStatus())) {
            // 用户已被冻结，禁止登录
            return JSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }

        // 保存用户分布式会话的相关操作
        String uToken = UUID.randomUUID().toString().replaceAll("-", "");
        // 保存token到Redis
        redisTemplate.opsForValue()
                .set(String.format(RedisConsts.USER_TOKEN, user.getId()), uToken, 30, TimeUnit.DAYS);
        // 保存用户id和token到cookie中
        setCookieSevenDays(response, "utoken", uToken);
        setCookieSevenDays(response, "uid", user.getId());

        // 用户登录或注册成功以后，删除Redis中的短信验证码
        redisTemplate.delete(String.format(RedisConsts.MOBILE_SMSCODE, mobile));
        return JSONResult.ok(user.getActiveStatus());
    }
}

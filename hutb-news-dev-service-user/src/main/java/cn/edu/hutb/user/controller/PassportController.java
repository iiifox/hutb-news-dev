package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.PassportControllerApi;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.component.AliyunComponent;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 田章
 * @description 用户通行证（注册、登录、退出登录）
 * @date 2023/2/10
 */
@RestController
public class PassportController implements PassportControllerApi {

    @Autowired
    private AliyunComponent aliyunComponent;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Random RANDOM = new Random();

    @Override
    public JSONResult getSmsCode(String mobile, HttpServletRequest request) {
        // 获取ip
        String ip = IpUtils.getRemoteIp(request);
        // 对ip进行限制，同一个ip在一分钟内不能够重复请求
        if (!Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(String.format(RedisConsts.MOBILE_SMSCODE_IP, ip), ip, 60, TimeUnit.SECONDS))) {
            throw new CustomException(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
        }

        // 生成六位数字的随机验证码
        String code = String.format("%06d", RANDOM.nextInt(1000000));
        // 发送验证码
        try {
            aliyunComponent.sendSms(mobile, code);
        } catch (Exception e) {
            throw new CustomException(ResponseStatusEnum.MOBILE_ERROR);
        }
        // 把验证码存入Redis，用于后续进行验证
        redisTemplate.opsForValue()
                .set(String.format(RedisConsts.MOBILE_SMSCODE_CODE, mobile), code, 5, TimeUnit.MINUTES);
        return JSONResult.ok();
    }
}

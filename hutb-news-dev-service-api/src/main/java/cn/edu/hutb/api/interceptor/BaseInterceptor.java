package cn.edu.hutb.api.interceptor;


import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import wiremock.org.apache.commons.lang3.StringUtils;

public class BaseInterceptor {

    @Autowired
    public StringRedisTemplate redisTemplate;

    public void verifyToken(String id, String token, String redisKey) {
        String redisToken = redisTemplate.opsForValue().get(String.format(redisKey, id));
        if (StringUtils.isBlank(id) || StringUtils.isBlank(token) || redisToken == null) {
            throw new CustomException(ResponseStatusEnum.UN_LOGIN);
        }
        if (!redisToken.equals(token)) {
            throw new CustomException((ResponseStatusEnum.TICKET_INVALID));
        }
    }
}

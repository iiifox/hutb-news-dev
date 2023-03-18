package cn.edu.hutb.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 田章
 * @description
 * @date 2023/2/10
 */
public class BaseController {

    @Value("${cn.edu.hutb.website.domain}")
    private String domain;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    /**
     * 获取BindingResult中的错误信息
     */
    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            // key：验证错误的属性  value：验证的错误信息
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }

    protected Integer getCountsFromRedis(String key) {
        String countsStr = redisTemplate.opsForValue().get(key);
        if (countsStr == null) {
            return 0;
        }
        return Integer.valueOf(countsStr);
    }

    protected void setCookieSevenDays(HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(response, cookieName, cookieValue, 7 * 24 * 60 * 60);
    }

    protected void deleteCookie(HttpServletResponse response, String cookieName) {
        setCookie(response, cookieName, "", 0);
    }

    /**
     * 保存cookie到响应中
     */
    private void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        try {
            cookieValue  = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

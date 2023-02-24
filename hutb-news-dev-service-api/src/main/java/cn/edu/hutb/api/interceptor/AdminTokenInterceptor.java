package cn.edu.hutb.api.interceptor;

import cn.edu.hutb.constant.RedisConsts;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminTokenInterceptor extends BaseInterceptor
        implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = request.getHeader("adminUserId");
        String token = request.getHeader("adminUserToken");
        verifyToken(id, token, RedisConsts.ADMIN_TOKEN_FORMATTER);
        return true;
    }
}

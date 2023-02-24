package cn.edu.hutb.api.interceptor;


import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.UserStatus;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.util.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户激活状态检查拦截器
 * <p>
 * 发文章，修改文章，删除文章，发表评论，查看评论等接口都是需要在用户激活以后才能进行
 * <p/>
 */
public class UserActiveInterceptor extends BaseInterceptor
        implements HandlerInterceptor {

    /**
     * 拦截请求，访问controller之前
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return false：请求被拦截。  true：请求通过验证，放行。
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userJson = redisTemplate.opsForValue()
                .get(String.format(RedisConsts.USER_INFO_FORMATTER, userId));
        if (userJson == null) {
            throw new CustomException(ResponseStatusEnum.UN_LOGIN);
        }

        AppUser user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        assert user != null;
        if (!UserStatus.ACTIVE.type.equals(user.getActiveStatus())) {
            throw new CustomException(ResponseStatusEnum.USER_INACTIVE_ERROR);
        }
        return true;
    }
}

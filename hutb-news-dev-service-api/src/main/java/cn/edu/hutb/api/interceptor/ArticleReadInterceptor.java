package cn.edu.hutb.api.interceptor;

import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.util.IpUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 田章
 * @description 文章阅读数防刷拦截器
 * @date 2023/3/18
 */
public class ArticleReadInterceptor extends BaseInterceptor
        implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String articleId = request.getParameter("articleId");
        String remoteIp = IpUtils.getRemoteIp(request);
        return Boolean.FALSE.equals(redisTemplate.hasKey(String.format(RedisConsts.ARTICLE_ALREADY_READ_FORMATTER, articleId, remoteIp)));
    }
}

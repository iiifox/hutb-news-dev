package cn.edu.hutb.api.config;

import cn.edu.hutb.api.interceptor.UserActiveInterceptor;
import cn.edu.hutb.api.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 田章
 * @description 拦截器配置类
 * @date 2023/2/13
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor userActiveInterceptor() {
        return new UserActiveInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户登录校验拦截器
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo", "/user/updateUserInfo")
                .addPathPatterns("/fs/uploadFace");
        // 用户激活状态检查拦截器
        // registry.addInterceptor(userActiveInterceptor())
        //         .addPathPatterns();
    }
}

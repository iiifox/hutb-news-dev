package cn.edu.hutb.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 田章
 * @description 允许跨域，通过拦截器方式进行配置，其优先级为最高
 * @date 2023/2/10
 */
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 处理所有请求的跨域配置
        source.registerCorsConfiguration("/**", new CorsConfiguration() {{
            // 允许cookie跨域
            setAllowCredentials(true);
            // 允许任何源(域名)使用
            addAllowedOriginPattern(CorsConfiguration.ALL);
            // 允许任何请求方式
            addAllowedMethod(CorsConfiguration.ALL);
            // 允许任何请求头
            addAllowedHeader(CorsConfiguration.ALL);
        }});

        // 注册自定义过滤器
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 优先级最高
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

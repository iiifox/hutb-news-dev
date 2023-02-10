package cn.edu.hutb.api.controller;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 田章
 * @description 用户通行证（注册、登录、退出登录）
 * @date 2023/2/10
 */
@RequestMapping("/passport")
public interface PassportControllerApi {

    /**
     * 获取短信验证码
     */
    @GetMapping("/getSMSCode")
    JSONResult getSmsCode(String mobile, HttpServletRequest request);
}

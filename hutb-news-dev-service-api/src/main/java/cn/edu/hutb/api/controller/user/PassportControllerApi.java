package cn.edu.hutb.api.controller.user;

import cn.edu.hutb.pojo.bo.RegisterLoginBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author 田章
 * @description 用户通行证（注册、登录、退出登录）
 * @date 2023/2/10
 */
@RequestMapping("/passport")
public interface PassportControllerApi {

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     */
    @GetMapping("/getSMSCode")
    JSONResult getSmsCode(@RequestParam String mobile, HttpServletRequest request);

    /**
     * 一键 注册/登录
     *
     * @return 用户激活状态 activeStatus
     */
    @PostMapping("/doLogin")
    JSONResult registerLogin(@RequestBody @Valid RegisterLoginBO bo, BindingResult result, HttpServletResponse response);

    /**
     * 用户退出登录
     *
     * @param userId 用户id
     */
    @PostMapping("/logout")
    JSONResult logout(@RequestParam String userId, HttpServletResponse response);
}

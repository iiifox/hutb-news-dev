package cn.edu.hutb.api.controller.user;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 田章
 * @description 用户信息相关接口
 * @date 2023/2/11
 */
@RequestMapping("/user")
public interface UserControllerApi {

    /**
     * 获取用户信息
     */
    @PostMapping("/getAccountInfo")
    JSONResult getAccountInfo(@RequestParam String userId);
}

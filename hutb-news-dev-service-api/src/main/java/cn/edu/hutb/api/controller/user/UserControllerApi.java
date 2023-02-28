package cn.edu.hutb.api.controller.user;

import cn.edu.hutb.pojo.bo.UpdateUserInfoBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    /**
     * 修改/完善用户信息
     */
    @PostMapping("/updateUserInfo")
    JSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO bo, BindingResult result);

    /**
     * 获取用户基本信息
     */
    @PostMapping("/getUserInfo")
    JSONResult getUserBasicInfo(@RequestParam String userId);

    /**
     * 根据用户的ids查询用户列表
     */
    @GetMapping("/queryByIds")
    JSONResult queryByIds(@RequestParam String userIds);
}

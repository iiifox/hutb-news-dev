package cn.edu.hutb.api.controller.user;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 用户管理相关的接口定义
 */
@RequestMapping("/appUser")
public interface AppUserMngControllerApi {

    /**
     * 根据条件查询网站用户
     */
    @PostMapping("/queryAll")
    JSONResult queryByCondition(@RequestParam(required = false) String nickname,
                                @RequestParam(required = false) Integer status,
                                @RequestParam(required = false) Date startDate,
                                @RequestParam(required = false) Date endDate,
                                @RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer pageSize);

    /**
     * 查看用户详情
     */
    @PostMapping("/userDetail")
    JSONResult userDetail(@RequestParam String userId);

    /**
     * 冻结/解冻用户
     */
    @PostMapping("/freezeUserOrNot")
    JSONResult freezeUserOrNot(@RequestParam String userId, @RequestParam Integer doStatus);
}
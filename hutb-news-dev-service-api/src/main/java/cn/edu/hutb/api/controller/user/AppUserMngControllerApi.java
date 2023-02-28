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
     *
     * @param nickname  用户昵称
     * @param status    用户状态
     * @param startDate 按用户创建时间查 --> 开始日期
     * @param endDate   按用户创建时间查 --> 截至日期
     * @param page      展示的第几页
     * @param pageSize  每页数据条数
     * @return 分页返回
     */
    @PostMapping("/queryAll")
    JSONResult queryByCondition(@RequestParam(required = false) String nickname,
                                @RequestParam(required = false) Integer status,
                                @RequestParam(required = false) Date startDate,
                                @RequestParam(required = false) Date endDate,
                                @RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer pageSize);

    /**
     * 根据用户id查看用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @PostMapping("/userDetail")
    JSONResult userDetail(@RequestParam String userId);

    /**
     * 冻结/解冻用户
     *
     * @param userId   用户id
     * @param doStatus 将用户设置为该状态
     */
    @PostMapping("/freezeUserOrNot")
    JSONResult freezeUserOrNot(@RequestParam String userId, @RequestParam Integer doStatus);
}

package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.user.UserControllerApi;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.pojo.bo.UpdateUserInfoBO;
import cn.edu.hutb.pojo.vo.UserAccountInfoVO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import wiremock.org.apache.commons.lang3.StringUtils;

/**
 * @author 田章
 * @description 用户信息相关Controller
 * @date 2023/2/11
 */
@RestController
public class UserController extends BaseController
        implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Override
    public JSONResult getAccountInfo(String userId) {
        // 判断参数不能为空
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 根据用户userId查询用户信息
        AppUser user = userService.getUser(userId);

        // 返回用户信息
        UserAccountInfoVO infoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, infoVO);
        return JSONResult.ok(infoVO);
    }

    @Override
    public JSONResult updateUserInfo(UpdateUserInfoBO bo, BindingResult result) {
        // 判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        // 执行更新操作
        userService.updateUserInfo(bo);
        return JSONResult.ok();
    }
}

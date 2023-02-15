package cn.edu.hutb.api.controller.admin;

import cn.edu.hutb.pojo.bo.AdminLoginBO;
import cn.edu.hutb.pojo.bo.NewAdminBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 田章
 * @description
 * @date 2023/2/15
 */
@RequestMapping("/adminMng")
public interface AdminMngControllerApi {

    /**
     * admin用户登录
     */
    @PostMapping("/adminLogin")
    JSONResult login(@RequestBody AdminLoginBO bo, HttpServletResponse response);

    /**
     * 校验admin登录名唯一
     */
    @PostMapping("/adminIsExist")
    JSONResult checkAdminUnique(@RequestParam String username);

    /**
     * 添加admin用户
     */
    @PostMapping("/addNewAdmin")
    JSONResult addAdmin(@RequestBody NewAdminBO bo);
}

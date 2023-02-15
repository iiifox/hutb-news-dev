package cn.edu.hutb.api.controller.admin;

import cn.edu.hutb.pojo.bo.AdminLoginBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

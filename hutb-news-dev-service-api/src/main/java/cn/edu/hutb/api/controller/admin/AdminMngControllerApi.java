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
 * @description admin用户管理接口
 * @date 2023/2/15
 */
@RequestMapping("/adminMng")
public interface AdminMngControllerApi {

    /**
     * admin用户密码登录
     */
    @PostMapping("/adminLogin")
    JSONResult passwordLogin(@RequestBody AdminLoginBO bo, HttpServletResponse response);

    /**
     * 校验admin登录名唯一
     *
     * @param username 管理人员username
     */
    @PostMapping("/adminIsExist")
    JSONResult checkAdminUnique(@RequestParam String username);

    /**
     * 添加admin用户
     */
    @PostMapping("/addNewAdmin")
    JSONResult addAdmin(@RequestBody NewAdminBO bo);

    /**
     * 查询admin用户列表
     *
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @PostMapping("getAdminList")
    JSONResult getAdminList(@RequestParam(required = false) Integer page,
                            @RequestParam(required = false) Integer pageSize);

    /**
     * admin用户退出登录
     *
     * @param adminId 管理员id
     */
    @PostMapping("/adminLogout")
    JSONResult logout(@RequestParam String adminId, HttpServletResponse response);

    /**
     * admin管理员人脸登录
     */
    @PostMapping("/adminFaceLogin")
    JSONResult faceLogin(@RequestBody AdminLoginBO bo, HttpServletResponse response);
}


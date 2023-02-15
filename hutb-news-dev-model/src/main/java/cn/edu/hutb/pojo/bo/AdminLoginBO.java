package cn.edu.hutb.pojo.bo;

import lombok.Data;

/**
 * 管理员登录的BO
 */
@Data
public class AdminLoginBO {
    private String username;
    private String password;
    private String img64;
}

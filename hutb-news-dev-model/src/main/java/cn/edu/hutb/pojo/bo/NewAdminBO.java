package cn.edu.hutb.pojo.bo;

import lombok.Data;

/**
 * 添加管理人员的BO
 */
@Data
public class NewAdminBO {
    private String username;
    private String adminName;
    private String password;
    private String confirmPassword;
    private String img64;
    private String faceId;
}

package cn.edu.hutb.constant;

/**
 * @author 田章
 * @description Redis缓存key值常量类
 * @date 2023/2/10
 */
public class RedisConsts {

    /**
     * 保存短信验证码（拼接手机号）<br/>
     * 限制一分钟内频繁请求发送短信（拼接ip）
     */
    public static final String MOBILE_SMSCODE = "mobile:smscode:%s";

    /**
     * 保存用户token（拼接userId）
     */
    public static final String USER_TOKEN = "user:token:%s";

    /**
     * 保存用户信息（拼接userId）
     */
    public static final String USER_INFO = "user:info:%s";

    /**
     * 保存admin用户token（拼接adminUserId）
     */
    public static final String ADMIN_TOKEN = "admin:token:%s";
}

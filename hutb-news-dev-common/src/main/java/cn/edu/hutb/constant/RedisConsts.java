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
    public static final String MOBILE_SMSCODE_FORMATTER = "mobile:smscode:%s";

    /**
     * 保存用户token（拼接userId）
     */
    public static final String USER_TOKEN_FORMATTER = "user:token:%s";

    /**
     * 保存用户信息（拼接userId）
     */
    public static final String USER_INFO_FORMATTER = "user:info:%s";

    /**
     * 保存admin用户token（拼接adminUserId）
     */
    public static final String ADMIN_TOKEN_FORMATTER = "admin:token:%s";

    public static final String ALL_CATEGORY = "all:category";

    /**
     * 作家粉丝数（拼接作家id）
     */
    public static final String FAN_COUNT_FORMATTER = "fan:count:%s";

    /**
     * 粉丝(我)的关注数（拼接粉丝id）
     */
    public static final String FOLLOW_COUNT_FORMATTER = "follow:count:%s";

    /**
     * 文章阅读数（拼接文章id）
     */
    public static final String ARTICLE_READ_COUNTS_FORMATTER = "article:count:%s";

    /**
     * 文章已经阅读过（拼接文章id和ip两个参数）
     */
    public static final String ARTICLE_ALREADY_READ_FORMATTER = "article:read:%s:%s";

    /**
     * 文章评论数（拼接文章id）
     */
    public static final String ARTICLE_COMMENT_COUNTS_FORMATTER = "article:comment:counts:%s";
}

package cn.edu.hutb.enums;

/**
 * 用户状态枚举类
 */
public enum UserStatus {
    INACTIVE(0, "未激活"),
    /**
     * 已激活：基本信息是否完善，真实姓名，邮箱地址，性别，生日，住址等，
     * 如果没有完善，则用户不能发表评论，不能点赞，不能关注，不能操作任何入库的功能。
     */
    ACTIVE(1, "已激活"),
    FROZEN(2, "已冻结");

    public final Integer type;
    private final String value;

    UserStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 判断传入的用户状态是不是有效的值
     */
    public static boolean validStatus(Integer status) {
        return INACTIVE.type.equals(status) || ACTIVE.type.equals(status) || FROZEN.type.equals(status);
    }

}

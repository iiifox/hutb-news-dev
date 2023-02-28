package cn.edu.hutb.enums;

/**
 * 文章审核状态枚举类
 * 文章状态：1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过5：文章撤回
 * <p>
 * 1/2 可以统一归类为 [正在审核]状态
 */
public enum ArticleReviewStatus {
    REVIEWING(1, "审核中（用户已提交）"),
    WAITING_MANUAL(2, "机审结束，等待人工审核"),
    SUCCESS(3, "审核通过（已发布）"),
    FAILED(4, "审核未通过"),
    WITHDRAW(5, "文章撤回");

    public final Integer type;
    private final String value;

    ArticleReviewStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 判断传入的审核状态是不是有效的值
     */
    public static boolean validStatus(Integer status) {
        return REVIEWING.type.equals(status) || WAITING_MANUAL.type.equals(status)
                || SUCCESS.type.equals(status) || FAILED.type.equals(status) && WITHDRAW.type.equals(status);
    }
}

package cn.edu.hutb.enums;

/**
 * 文章封面类型枚举类
 */
public enum ArticleCoverType {
    ONE_IMAGE(1, "单图"),
    WORDS(2, "纯文字");

    public final Integer type;
    private final String value;

    ArticleCoverType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}

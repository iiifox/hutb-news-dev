package cn.edu.hutb.enums;

/**
 * 性别枚举类
 */
public enum Sex {
    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    private final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}

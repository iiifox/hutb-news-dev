package cn.edu.hutb.util;

/**
 * 脱敏工具类
 */
public final class DesensitizationUtils {

    /**
     * 【11位手机号码】前三后四，比如：176****4099
     * 注意：如果手机号不是11位，则返回null
     */
    public static String mobilePhone(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return null;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

}

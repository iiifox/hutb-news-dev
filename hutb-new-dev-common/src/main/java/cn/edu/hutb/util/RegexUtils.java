package cn.edu.hutb.util;

import java.util.regex.Pattern;

/**
 * 正则工具类
 */
public final class RegexUtils {

    // + "(http|https|ftp|file)://"
    // + "([a-z0-9][a-z0-9-]*[a-z0-9]\\.)+([a-z0-9]+[a-z0-9-]*[a-z0-9]+)(:\\d+)?"
    // + "(/[a-z0-9]+[a-z0-9-]*[a-z0-9]+)*"
    // + "([a-z0-9][a-z0-9-]*[a-z0-9])+"
    // + "(\\?((\\w[\\w-]+\\w(=\\w[\\w-]+\\w)?)))?"

    // "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
    /**
     * TODO 编译正则表达式（后续会修改）
     */
    private static final Pattern URL_PATTERN = Pattern.compile("^"
            + "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
            + "$", Pattern.CASE_INSENSITIVE);

    /**
     * 验证是否是URL
     */
    public static boolean isUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

}

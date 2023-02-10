package cn.edu.hutb.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author 田章
 * @description IP 工具类
 * @date 2022/12/24
 */
public final class IpUtils {

    private IpUtils() {
        throw new AssertionError("cn.edu.hutb.util.IpUtils instances for you!");
    }

    private static final String UNKNOWN = "unknown";

    /**
     * 简称 XFF 头，只有在经过了 HTTP 代理或者负载均衡服务器时才会添加该项。
     * <p>
     * 若只存在 1 级的代理服务器，则该参数为客户端的真实 IP；若是存在多级代理时，每经过一级代理服务器，
     * 则追加上级代理服务的 IP，多个 IP 地址使用 逗号+空格 分隔，此时仍可以获取到真实的用户 IP。
     * 但若是遇到伪造的 X-Forwarded-For 信息或第一级代理未启用 X-Forwarded-For 都不能获取到真实用户 IP。
     * </p>
     * 注：forwarded_for 项默认为 on。如果 forwarded_for 设成了 off，则：X-Forwarded-For:unknown
     */
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * <p>
     * Proxy-Client-IP / WL-Proxy-Client-IP ： <br/>
     * 在 Apache+WebLogic 整合系统中，Apache 会对 request 对象进行再包装，附加 Proxy-Client-IP,
     * WL-Proxy-Client-IP(weblogic设置了才会有这个参数) 这些 WLS 要用的头信息。一般情况下这两一样。<br/>
     * 注：forwarded_for 设成了 off 时，在 Apache+WebLogic 的系统中这两个头信息可以得到用户的真实 IP
     * </p>
     * <p>HTTP_CLIENT_IP：有些代理服务器会加上此请求头。可能存在，可伪造。</p>
     * <p>
     * X-Real-IP：Nginx代理一般会加上此请求头。若只存在 1 级的代理服务器，则该参数的确就是客户端的真实 IP；
     * 但若是存在多级代理时，此信息为上级代理的 IP 信息，并不能获取到真实的用户 IP。</p>
     */
    private static final String[] HEADERS_TO_TRY = {
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP"
    };

    /**
     * 获取请求头当中的用户的真实 IP（任何方式都不一定能够准确获取，ip伪造等情况该方法无法解决）
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (IpFound(ip)) {
            // 多次反向代理后会有多个 ip 值，一般第一个就是客户端 ip
            return ip.contains(",") ? ip.substring(0, ip.indexOf(',')) : ip;
        }
        return Arrays.stream(HEADERS_TO_TRY)
                .map(request::getHeader)
                .filter(IpUtils::IpFound)
                .findFirst().orElse(IpUtils.getRemoteAddr(request));
    }

    private static boolean IpFound(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 获取请求头中用户 ip （无代理情况下）
     */
    private static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if ("127.0.0.1".equals(remoteAddr) || "0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return "127.0.0.1";
            }
        }
        return remoteAddr;
    }

}
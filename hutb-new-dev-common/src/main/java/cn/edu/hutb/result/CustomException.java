package cn.edu.hutb.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * 目的：统一处理异常信息
 * 便于解耦，service与controller错误的解耦，不会被service返回的类型而限制
 */
public class CustomException extends RuntimeException {

    @Getter
    @Setter
    private ResponseStatusEnum responseStatusEnum;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码为：" + responseStatusEnum.status()
                + "；具体异常信息为：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }
}

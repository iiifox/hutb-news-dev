package cn.edu.hutb.result;

import lombok.*;

import java.util.Map;

/**
 * 自定义响应数据类型枚举升级版本
 *
 * @author 慕课网 - 风间影月
 * @version V2.0
 * @Title: IMOOCJSONResult.java
 * @Package com.imooc.utils
 * @Description: 自定义响应数据结构
 * 本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 * @Copyright: Copyright (c) 2020
 * @Company: www.imooc.com
 */
@Getter
@Setter
public class JSONResult {

    /**
     * 响应业务状态码
     */
    private Integer status;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 响应数据，可以是Object，也可以是List或Map等
     */
    private Object data;

    private JSONResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }

    private JSONResult(ResponseStatusEnum responseStatus, Object data) {
        this(responseStatus);
        this.data = data;
    }

    private JSONResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    /**
     * 成功返回，带有数据的，直接往OK方法丢data数据即可
     */
    public static JSONResult ok(Object data) {
        return new JSONResult(ResponseStatusEnum.SUCCESS, data);
    }

    /**
     * 成功返回，不带有数据的，直接调用ok方法，data无须传入（其实就是null）
     */
    public static JSONResult ok() {
        return new JSONResult(ResponseStatusEnum.SUCCESS);
    }

    /**
     * 错误返回，直接调用error方法即可，当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     */
    public static JSONResult error() {
        return new JSONResult(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回，map中包含了多条错误信息，可以用于表单验证，把错误统一的全部返回出去
     */
    public static JSONResult errorMap(Map map) {
        return new JSONResult(ResponseStatusEnum.FAILED, map);
    }

    /**
     * 错误返回，直接返回错误的消息
     */
    public static JSONResult errorMsg(String msg) {
        return new JSONResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 错误返回，token异常，一些通用的可以在这里统一定义
     */
    public static JSONResult errorTicket() {
        return new JSONResult(ResponseStatusEnum.TICKET_INVALID);
    }

    static JSONResult exception(ResponseStatusEnum responseStatus) {
        return new JSONResult(responseStatus);
    }

}

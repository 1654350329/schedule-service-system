package com.tree.clouds.schedule.common;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@ApiModel(value = "返回类")
public class RestResponse<T> implements Serializable {

    @ApiModelProperty(value = "code")
    private int code;
    @ApiModelProperty(value = "描述")
    private String msg;
    @ApiModelProperty(value = "对象")
    private T data;

    public RestResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RestResponse<Boolean> ok() {
        return new RestResponse<Boolean>(HttpStatus.OK.value(), "操作成功", true);
    }

    public static <E> RestResponse<E> ok(E data) {
        return new RestResponse<E>(HttpStatus.OK.value(), "ok", data);
    }

    public static <E> RestResponse<E> ok(E data, String message) {
        return new RestResponse<E>(HttpStatus.OK.value(), message, data);
    }

    public static <E> RestResponse<E> fail(Integer code, String message) {
        return new RestResponse<E>(code, StrUtil.isAllBlank(message) ? "系统异常" : message, null);
    }

    public static <E> RestResponse<E> fail(String message) {
        return new RestResponse<E>(400, StrUtil.isAllBlank(message) ? "系统异常" : message, null);
    }
}

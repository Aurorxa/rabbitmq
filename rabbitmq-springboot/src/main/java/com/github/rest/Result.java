package com.github.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 返回结果的模型类，用于后端和前端进行数据格式统一，也称为"前后端数据协议" <br/>
 * 对于增删改查操作： <br/>
 * ① 如果接口成功，返回 {"status":true,"data":null} <br/>
 * ② 如果接口失败，返回 {"status":false,"data":null} <br/>
 * 对于查询操作： <br/>
 * ① 如果接口成功，返回 {"status":true,"data": {}} <br/>
 * ② 如果接口成功，但是数据不存在，返回 {"status":true,"data": null} <br/>
 * ③ 如果查询失败（服务器异常），返回 {"status":false,"data": null} <br/>
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-01-14 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    public static Boolean SUCCESS_STATUS = true;

    public static Boolean ERROR_STATUS = false;

    private Boolean status;

    private T data;

    private String msg;

    private String timestamp = LocalDateTime.now().toString();


    public static <T> Result<?> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.setStatus(Result.SUCCESS_STATUS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<?> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> Result<?> success() {
        return success(null, "操作成功");
    }

    public static <T> Result<?> error(T data, String msg) {
        Result<T> result = new Result<>();
        result.setStatus(Result.ERROR_STATUS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<?> error(T data) {
        return error(data, "操作失败");
    }

    public static <T> Result<?> error() {
        return error(null, "操作失败");
    }


}

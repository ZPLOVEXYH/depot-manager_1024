package cn.samples.depot.common.utils;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.Status;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.Serializable;
import java.net.ConnectException;
import java.sql.SQLException;


/**
 * Description: 用户服务层
 *
 * @className: JsonResult
 * @Author: zhangpeng
 * @Date 2019/7/16 14:42
 * @Version 1.0
 **/
@Slf4j
@Getter
@Builder
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1071681926787951549L;

    public interface View extends PageView.View, Status.View {
    }

    /**
     * 状态码
     */
    @JsonView(View.class)
    private String code;

    /**
     * 状态说明
     */
    @JsonView(View.class)
    private String message;

    /**
     * 返回数据
     */
    @JsonView(View.class)
    private T data;

    /**
     * <p>返回成功</p>
     *
     * @param message 错误说明
     * @param data    数据
     */
    public JsonResult(String message, T data) {
        this.code = "0000";
        this.message = message;
        this.data = data;
    }

    /**
     * <p>返回成功</p>
     *
     * @param message 错误说明
     */
    public JsonResult(String message) {
        this.code = "0000";
        this.message = message;
    }

    public JsonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonResult() {
        this.code = "0000";
    }

    public JsonResult(Throwable throwable) {
        log.error(throwable.getMessage());
        if (throwable instanceof NullPointerException) {
            this.code = "1001";
            this.message = "空指针异常";
        } else if (throwable instanceof ClassCastException) {
            this.code = "1002";
            this.message = "类型强制转换异常";
        } else if (throwable instanceof ConnectException) {
            this.code = "1003";
            this.message = "链接失败异常";
        } else if (throwable instanceof IllegalArgumentException) {
            this.code = "1004";
            this.message = "传递非法参数异常";
        } else if (throwable instanceof NumberFormatException) {
            this.code = "1005";
            this.message = "数字格式异常";
        } else if (throwable instanceof IndexOutOfBoundsException) {
            this.code = "1006";
            this.message = "下标越界异常";
        } else if (throwable instanceof SecurityException) {
            this.code = "1007";
            this.message = "安全异常";
        } else if (throwable instanceof SQLException) {
            this.code = "1008";
            this.message = "数据库异常";
        } else if (throwable instanceof ArithmeticException) {
            this.code = "1009";
            this.message = "算术运算异常";
        } else if (throwable instanceof DuplicateKeyException) {
            this.code = "1010";
            this.message = "数据库主键冲突";
        } else if (throwable instanceof HttpMessageNotReadableException) {
            this.code = "1011";
            this.message = "缺少所需的请求主体";
        } else if (throwable instanceof HttpRequestMethodNotSupportedException) {
            this.code = "1012";
            this.message = "不支持请求方法" + throwable.getMessage();
        } else if (throwable instanceof RuntimeException) {
            this.code = "1013";
            this.message = "运行时异常";
        } else if (throwable instanceof BizException) {
            log.error("业务异常");
            this.code = "9998";
            this.message = throwable.getMessage();
        } else if (throwable instanceof Exception) {
            log.error("未知异常");
            this.code = "9999";
            this.message = "未知异常";
        }
    }

    public static JsonResult<?> success() {
        return JsonResult.builder().code("0000").message("success").build();
    }

    public static JsonResult<?> error(String msg) {
        return JsonResult.builder().code("9999").message(msg).build();
    }


    public static JsonResult<?> error(String code, String msg) {
        return JsonResult.builder().code(code).message(msg).build();
    }

    public static <T> JsonResult<T> error(String code, String msg, T data) {
        return JsonResult.<T>builder().code(code).message(msg).data(data).build();
    }

    public static <T> JsonResult<T> data(T data) {
        return JsonResult.<T>builder().code("0000").message("success").data(data).build();
    }

    public static JsonResult<Params> data(String key, Object data) {
        return data(Params.param(key, data));
    }

}

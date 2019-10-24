package cn.samples.depot.web.handler;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.LogExceptionStackUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.StringJoiner;

/**
 * Description:
 *
 * @className: JsonResultExceptionHandler
 * @Author: zhangpeng
 * @Date 2019/7/16 14:24
 * @Version 1.0
 **/
@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class JsonResultExceptionHandler {

    private void printMsgAtConsole(Throwable e) {
        e.printStackTrace();
        log.error("出错了" + LogExceptionStackUtil.logExceptionStack(e));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public JsonResult handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        printMsgAtConsole(e);
        return JsonResult.error("您没有权限访问");
    }

    @ExceptionHandler(value = Exception.class)
    public JsonResult handleException(HttpServletRequest req, Throwable e) {
        printMsgAtConsole(e);
        return new JsonResult(e);
    }

//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public JsonResult handleSQLIntegrityConstraintViolationException(HttpServletRequest req, Exception e) {
//        printMsgAtConsole(e);
//        return JsonResult.error("数据库表主键冲突");
//    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public JsonResult bindingResultExceptionHandler(Exception ex) {
        BindingResult bindingResult;
        String msg;
        if (ex instanceof MethodArgumentNotValidException) {
            msg = "BindException异常信息";
            bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        } else {
            bindingResult = ((BindException) ex).getBindingResult();
            msg = "BindException异常信息";
        }
        StringJoiner sj = new StringJoiner(";");
        bindingResult.getAllErrors().forEach(e -> sj.add(e.getDefaultMessage()));
        JSONObject jsonResult = new JSONObject();
        jsonResult.put(bindingResult.getObjectName(), sj.toString());
        log.error("{}：[{}]", msg, jsonResult.toJSONString(), ex);
        return JsonResult.error("9998", sj.toString());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, BizException.class})
    public JsonResult handleConstraintViolationException(HttpServletRequest req, Exception e) {
        printMsgAtConsole(e);
        return JsonResult.error(e.getMessage());
    }
}
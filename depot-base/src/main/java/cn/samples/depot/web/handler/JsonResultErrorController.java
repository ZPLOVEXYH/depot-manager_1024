package cn.samples.depot.web.handler;

import cn.samples.depot.common.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Description:
 *
 * @className: JsonResultErrorController
 * @Author: zhangpeng
 * @Date 2019/7/16 14:24
 * @Version 1.0
 **/
@RestController
@SuppressWarnings("rawtypes")
public class JsonResultErrorController implements ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public JsonResult error(WebRequest request) {
        Map<String, Object> map = errorAttributes.getErrorAttributes(request, true);
        return JsonResult.error(String.valueOf(map.get("status")), String.valueOf(map.get("error")));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
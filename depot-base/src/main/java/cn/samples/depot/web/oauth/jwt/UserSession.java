package cn.samples.depot.web.oauth.jwt;

import cn.samples.depot.common.utils.JsonResult;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Description: 用户登录session缓存
 *
 * @className: UserSession
 * @Author: ChenJie
 * @Date 2019/8/29
 * @Version 1.0
 **/
@Component
@SuppressWarnings("rawtypes")
public class UserSession {
    @Cacheable(value = "userLogin", key = "#username")
    public JsonResult get(String username) {
        return JsonResult.error("缓存失效");
    }
}

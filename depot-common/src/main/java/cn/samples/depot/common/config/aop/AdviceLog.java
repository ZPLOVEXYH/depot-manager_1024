package cn.samples.depot.common.config.aop;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.DateUtils;
import cn.samples.depot.common.utils.FormatUtil;
import cn.samples.depot.common.utils.HttpContextUtil;
import cn.samples.depot.common.utils.IPUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Description:
 *
 * @className: AdviceLog
 * @Author: zhangpeng
 * @Date 2019/7/16 14:00
 * @Version 1.0
 **/
@Slf4j
@Aspect
@Component
public class AdviceLog {

    @Pointcut(value = "@annotation(AddLog)")
    public void pointcut() {

    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) throws BizException {
//        validateReq(joinPoint);
        SysLogEntity sysLog = this.initSysLog(joinPoint);
        // TODO 在此处获取操作日志信息，保存操作日志

        String req = FormatUtil.formatJson(sysLog.getParams());
        sysLog.setParams(req);
        log.info("系统日志-req:{}\r\n", sysLog);
    }

    @AfterReturning(value = "pointcut()", returning = "retValue")
    public void afterReturning(JoinPoint joinPoint, Object retValue) {
        SysLogEntity sysLog = this.initSysLog(joinPoint);
        Gson gson = new Gson();
        String rsp = gson.toJson(retValue);
        String rsp2 = FormatUtil.formatJson(rsp);
        sysLog.setRsp(rsp2);
        log.info("系统日志-rsp:{}\r\n", sysLog);
    }

    /**
     * @param joinPoint
     * @return
     * @Description:初始化系统日志实体
     * @version:v1.0
     * @date:2019年3月21日 上午09:36:18
     * @author:ZhangPeng
     */
    private SysLogEntity initSysLog(JoinPoint joinPoint) {
        SysLogEntity sysLog = new SysLogEntity();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 注解值
        AddLog syslogAnno = method.getAnnotation(AddLog.class);
        if (syslogAnno != null) {
            // 注解上的描述
            sysLog.setOperation(syslogAnno.value());
            // 获取得到操作描述
            sysLog.setOptInfo(syslogAnno.optInfo());
        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        // TODO 请求的参数
        Object[] args = joinPoint.getArgs();

        Gson gson = new Gson();
        String params = args == null || args.length == 0 ? null : gson.toJson(args[0]);
        sysLog.setParams(params);

        // 获取request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(IPUtil.getIpAddr(request));
        sysLog.setCreateDate(DateUtils.getCurrentTimeStamp());
        sysLog.setTime(System.currentTimeMillis());

        return sysLog;
    }

    // Controller层校验请求参数，配合hibernate validator使用
//	private void validateReq(JoinPoint joinPoint) throws BizException {
//		Object[] args = joinPoint.getArgs();
//		if (args.length > 1 && args[1] != null && args[1] instanceof BindingResult)
//		{
//			BindingResult result = (BindingResult) args[1];
//			if (result.hasErrors()) {
//                throw new BizException(result.getFieldError().getDefaultMessage());
//            }
//		}
//	}
}

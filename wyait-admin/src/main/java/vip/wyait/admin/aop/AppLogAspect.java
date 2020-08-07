package vip.wyait.admin.aop;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.wyait.admin.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @项目名称：wyait
 * @类名称：AppLogAspect
 * @类描述：mdc记录系统日志
 * @创建人：wyait
 */
@Aspect
@Component
public class AppLogAspect {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AppLogAspect.class);

//    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    /**
     * @Description: 自定义切入点配置
     * @Author:   wyait
     */
//    @Pointcut("(!execution(* com.wyait.vip.controller.demo..*.*(..))) && (execution(* com.wyait.vip.controller..*.*(..)))  ")
    @Pointcut("(execution(* vip.wyait.admin.controller..*.*(..)))  ")
    public void webLog() {

    }

    /**
     * @描述：环绕通知--必须返回object，否则目标方法执行后，无返回结果【也可改为前置通知结合后置通知实现】
     * 注意：该方法在controller层使用流返回信息的时候，可能会有流冲突问题。尽量使用前置、后置通知记录日志
     * @创建人：wyait
     */
    @Around("webLog()")
    public Object around(JoinPoint joinPoint) {
        LOGGER.debug("==前置通知--日志记录==start==");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        //必须返回object，否则目标方法执行后，无返回结果
        Object obj=null;
        try {
            // 获取请求头中的traceId
            String traceId=request.getHeader(Constant.TRACEID);
            LOGGER.debug("==请求头header==traceId:{}",traceId);
            if(StringUtils.isBlank(traceId) || traceId.length()>50){
                //没有，设置traceId
                traceId = String.valueOf(UUID.randomUUID());
            }
            LOGGER.debug("==mdc日志跟踪==traceId:{}",traceId);
            MDC.put(Constant.TRACEID,traceId);
            //设置请求开始时间在本地线程中
            startTime.set(System.currentTimeMillis());
            // 请求的IP
            String ip = getIpAddr(request);
            // 请求域名
            String domain = request.getHeader("Host");
            // 请求uri
            String pageUrl = String.valueOf(request.getRequestURL());
            // LOGGER.debug("***==用户登录信息数据：userName:{}", userName);
            // 目标类
            String targetName = joinPoint.getTarget().getClass().getName();
            // LOGGER.debug("===targetName:{}", targetName);
            // 目标方法名
            String methodName = joinPoint.getSignature().getName();
            // LOGGER.debug("===methodName:{}", methodName);
            // 请求参数
            Object[] arguments = joinPoint.getArgs();
            // String params = objectMapper.writeValueAsString(arguments);
            // LOGGER.debug("===params:{}", params);
            // 获取参数名称和值
            Map<String, Object> nameAndArgs = null;
            if (null != arguments && arguments.length > 0) {
                nameAndArgs = getFieldsName(this.getClass(), targetName,
                        methodName, arguments);
            }
            /**
             * @Description: 【问题】对象转换为json字符串，需要依赖outputStream流对象；会和controller层response直接返回数据产出冲突，产生异常：<br></br>
             *  getOutputStream() has already been called for this response
             */
//            String params = "";
//            try {
//                params = (nameAndArgs == null) ? "" : objectMapper
//                        .writeValueAsString(nameAndArgs);
//                params = (nameAndArgs == null) ? "" : nameAndArgs;
//            } catch (JsonProcessingException e) {
//                // 记录对象转为json异常日志 TODO
////                LOGGER.error(
////                        "[error-log]AppLogAspect-before=记录系统日志，对象转为json异常======异常e:{}",
////                        e.getMessage());
//            }
            //执行目标方法
            obj=((ProceedingJoinPoint) joinPoint).proceed();

            // 计算接口执行时间
            // *========日志输出=========*//
            LOGGER.info("({})url:{}，method:{}.{}()，ip:{}，params:{}，time:{}ms", domain,
                    pageUrl, joinPoint.getTarget().getClass().getName(),
                    joinPoint.getSignature().getName(), ip, nameAndArgs,System.currentTimeMillis()-startTime.get());
            LOGGER.debug("==前置通知--日志记录==end==");

        } catch (NotFoundException e) {
            LOGGER.error(
                    "[error-log]AppLogAspect-before=记录系统日志异常======异常e:{}",
                    e.getMessage());
        } catch (Throwable throwable) {
            LOGGER.error(
                    "[error-log]AppLogAspect-before=记录系统日志异常======异常e:{}",
                    throwable.getMessage());
        }
        return obj;
    }

    /**
     * 计算接口执行时间【Finally增强】
     */
    @After("webLog()")
    public void doAfter() {
        // 处理完请求，清除本次的traceId。mdc是基于ThreadContext实现的【源码】
        MDC.clear();
        //响应结果
//        long costTime = System.currentTimeMillis() - startTime.get();
//        LOGGER.debug("====> time:{}ms " ,costTime);
    }

    /**
     * @param joinPoint
     * @param e
     * @描述：异常通知--用于记录异常日志
     * @创建人：wyait
     */
    @AfterThrowing(value = "webLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LOGGER.debug("==异常通知--用于记录异常日志 ==start==");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        try {
            // 请求的IP
            String ip = getIpAddr(request);
            // 获取用户请求方法的参数并序列化为JSON格式字符串
            // 请求参数
            Object[] arguments = joinPoint.getArgs();
            String domain = request.getHeader("Host");
            String pageUrl = String.valueOf(request.getRequestURL());
            // 目标类、目标类方法
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Map<String, Object> nameAndArgs = null;
            if (null != arguments && arguments.length > 0) {
                nameAndArgs = getFieldsName(this.getClass(), targetName,
                        methodName, arguments);
            }
            /**
             * @Description: 【问题】对象转换为json字符串，需要依赖outputStream流对象；会和controller层response直接返回产出冲突，产生异常：<br></br>
             *  getOutputStream() has already been called for this response
             */
//            String params = "";
//            try {
//                params = (nameAndArgs == null) ? "" : objectMapper
//                        .writeValueAsString(nameAndArgs);
//            } catch (JsonProcessingException je) {
//                // 记录本地异常日志 TODO
////                LOGGER.error(
////                        "[error-log]AppLogAspect-afterThrowing=记录系统日志，对象转为json异常======异常e:{}",
////                        je.getMessage());
//            }
            // *========日志输出=========*//
            LOGGER.error("({})url:{}，method:{}.{}()，ip:{}，params:{}，time:{}ms，exception:{}",
                    domain, pageUrl,
                    joinPoint.getTarget().getClass().getName(), joinPoint
                            .getSignature().getName(), ip, nameAndArgs, System.currentTimeMillis() - startTime.get(), e);
            LOGGER.debug("==异常通知--用于记录异常日志 ==end==");
        } catch (Exception ex) {
            // 记录本地异常日志
            LOGGER.error(
                    "[error-log]AppLogAspect-afterThrowing=记录系统日志异常======异常信息:{}",
                    ex.getMessage());
        }
    }

    /**
     * @param request
     * @return
     * @描述：获取用户真实IP
     * @创建人：wyait
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = "";
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * @param cls
     * @param clazzName
     * @param methodName
     * @param arguments
     * @return
     * @throws NotFoundException
     * @描述：获取参数名和参数值
     * @创建人：wyait
     */
    private Map<String, Object> getFieldsName(
            Class<? extends AppLogAspect> cls, String clazzName,
            String methodName, Object[] arguments) throws NotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        // ClassClassPath classPath = new ClassClassPath(this.getClass());
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        javassist.bytecode.MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        } else {
            // String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                map.put(attr.variableName(i + pos), arguments[i]);// paramNames即参数名
            }
        }
        // Map<>
        return map;
    }
}

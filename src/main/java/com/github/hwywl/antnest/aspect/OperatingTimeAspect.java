package com.github.hwywl.antnest.aspect;

import com.github.hwywl.antnest.annotation.web.OperatingTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 请求拦截，耗时计算
 *
 * @author YI
 * @date 2019年6月15日
 */
@Slf4j
@Aspect
@Component
public class OperatingTimeAspect {

    /**
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.web.OperatingTime)")
    public void logPointCut() {

    }

    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 耗时接口描述
        log.info("========================================== Start ==========================================");
        log.info("接口描述 : " + getAspectLogDescription(joinPoint));
    }

    /**
     * 环绕通知,环绕增强
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        // ob 为方法的返回值
        Object ob = pjp.proceed();
        log.info("耗时 : " + (System.currentTimeMillis() - startTime) + " 毫秒");

        return ob;
    }

    /**
     * 在切点之后织入
     */
    @After(value = "logPointCut()")
    public void doAfter() {
        // 接口结束后换行，方便分割查看
        log.info("=========================================== End ===========================================");
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);

        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(OperatingTime.class).description());
                    break;
                }
            }
        }

        return description.toString();
    }
}

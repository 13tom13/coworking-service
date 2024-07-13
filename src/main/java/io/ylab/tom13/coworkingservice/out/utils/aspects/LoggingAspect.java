package io.ylab.tom13.coworkingservice.out.utils.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования времени выполнения методов в приложении.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Определяет pointcut для всех методов внутри пакета `io.ylab.tom13.coworkingservice.out.rest`.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest..*(..))")
    public void applicationPackagePointcut() {
    }

    /**
     * Аспект, который логирует время выполнения методов.
     *
     * @param joinPoint точка присоединения, представляющая вызываемый метод
     * @return результат выполнения метода, возвращаемый после выполнения
     * @throws Throwable любое исключение, выброшенное методом
     */
    @Around("applicationPackagePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("{}.{} executed in {} ms", className, methodName, executionTime);

        return proceed;
    }
}

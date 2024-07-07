package io.ylab.tom13.coworkingservice.out.utils.aspects;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ValidationExceptionHandlerAspect {

    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.servlet.*Servlet.*(..))")
    public void servletMethods() {
    }

    @Around("servletMethods()")
    public Object handleValidationException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ValidationException e) {
            HttpServletResponse response = findHttpServletResponse(joinPoint.getArgs());
            if (response != null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } else {
                throw e;
            }
            return null;
        }
    }

    private HttpServletResponse findHttpServletResponse(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        return null;
    }
}

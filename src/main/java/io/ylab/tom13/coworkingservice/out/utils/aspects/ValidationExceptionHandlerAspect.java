package io.ylab.tom13.coworkingservice.out.utils.aspects;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ValidationExceptionHandlerAspect {

    @Around("execution(* io.ylab.tom13.coworkingservice.out.rest.servlet.*Servlet.*(..))")
    public Object handleValidationException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ValidationException e) {
            HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[1];
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }
}

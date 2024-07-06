package io.ylab.tom13.coworkingservice.out.utils.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Set;

@Aspect
public class ValidationAspect {

    private final Validator validator;

    public ValidationAspect() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.servlet.*Servlet.do*(..))")
    public void servletMethods() {
    }

    @Around("servletMethods()")
    public Object validateObjects(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && isDTO(arg.getClass())) {
                validateDTO(arg);
            }
        }
        return joinPoint.proceed();
    }

    private boolean isDTO(Class<?> clazz) {
        return clazz.getSimpleName().endsWith("DTO");
    }

    private void validateDTO(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new ValidationException("Validation failed:\n" + sb);
        }
    }
}

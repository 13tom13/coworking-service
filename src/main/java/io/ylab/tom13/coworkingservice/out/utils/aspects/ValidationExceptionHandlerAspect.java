package io.ylab.tom13.coworkingservice.out.utils.aspects;


import jakarta.validation.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Аспект для обработки исключений валидации в контроллерах.
 */
@Aspect
@Component
public class ValidationExceptionHandlerAspect {

    /**
     * Определяет pointcut для всех методов всех контроллеров.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.controller..*.*(..))")
    public void controllerMethods() {
    }

    /**
     * Аспект, который перехватывает и обрабатывает исключения валидации, возникающие в контроллерах.
     *
     * @param joinPoint точка присоединения, представляющая вызываемый метод контроллера
     * @return результат выполнения метода или ResponseEntity с ошибкой, если произошло исключение валидации
     * @throws Throwable если произошло исключение, отличное от ValidationException
     */
    @Around("controllerMethods()")
    public Object handleValidationException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

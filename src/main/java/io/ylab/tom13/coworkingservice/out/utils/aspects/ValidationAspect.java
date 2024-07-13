package io.ylab.tom13.coworkingservice.out.utils.aspects;

import io.ylab.tom13.coworkingservice.out.rest.controller.BookingController;
import jakarta.validation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Аспект для выполнения валидации объектов DTO в сервисах.
 */
@Aspect
@Component
public class ValidationAspect {

    private final Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    /**
     * Инициализирует аспект и получает экземпляр валидатора.
     */
    public ValidationAspect() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Определяет pointcut для всех методов всех сервисов.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.services..*.*(..))")
    public void serviceMethods() {
    }

    /**
     * Аспект, который выполняет валидацию объектов DTO перед выполнением методов сервисов.
     *
     * @param joinPoint точка присоединения, представляющая вызываемый метод
     * @return результат выполнения метода
     * @throws Throwable если валидация не удалась или возникло исключение при выполнении метода
     */
    @Around("serviceMethods()")
    public Object validateObjects(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && isDTO(arg.getClass())) {
                validateDTO(arg);
            }
        }
        return joinPoint.proceed();
    }

    /**
     * Проверяет, является ли класс DTO.
     *
     * @param clazz класс для проверки
     * @return true, если класс является DTO, иначе false
     */
    private boolean isDTO(Class<?> clazz) {
        return clazz.getSimpleName().endsWith("DTO");
    }

    /**
     * Выполняет валидацию объекта DTO с использованием валидатора.
     *
     * @param dto объект DTO для валидации
     * @throws ValidationException если валидация не удалась
     */
    private void validateDTO(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                sb.append(violation.getMessage());
            }
            throw new ValidationException("Validation failed: " + sb);
        }
    }
}

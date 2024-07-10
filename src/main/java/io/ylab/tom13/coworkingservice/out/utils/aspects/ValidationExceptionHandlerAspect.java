package io.ylab.tom13.coworkingservice.out.utils.aspects;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Аспект для обработки исключений валидации в сервлетах.
 */
@Aspect
public class ValidationExceptionHandlerAspect {

    /**
     * Определяет pointcut для всех методов всех сервлетов.
     */
    @Pointcut("execution(* io.ylab.tom13.coworkingservice.out.rest.servlet.*Servlet.*(..))")
    public void servletMethods() {
    }

    /**
     * Аспект, который перехватывает и обрабатывает исключения валидации, возникающие в сервлетах.
     *
     * @param joinPoint точка присоединения, представляющая вызываемый метод сервлета
     * @return результат выполнения метода или null, если произошло исключение валидации
     * @throws Throwable если произошло исключение, отличное от ValidationException
     */
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

    /**
     * Находит объект HttpServletResponse среди аргументов метода.
     *
     * @param args аргументы метода, включая объекты запроса и ответа
     * @return объект HttpServletResponse или null, если не найден
     */
    private HttpServletResponse findHttpServletResponse(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        return null;
    }
}

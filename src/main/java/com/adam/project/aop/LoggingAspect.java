package com.adam.project.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(* com.adam.project.service.UserService.addUser(..))", returning = "isCreated")
    public void afterUserRegistrationLoggingAdvice(boolean isCreated){
        if (isCreated)
            System.out.println("afterUserRegistrationLoggingAdvice: Регистрация нового пользователя");
    }

    @After("execution(* com.adam.project.controller.QuestionController.createQuestion(..))")
    public void afterAskLoggingAdvice(){
        System.out.println("afterAskLoggingAdvice: был задан новый вопрос");
    }

    @After("execution(* com.adam.project.controller.AnswererController.answer(..))")
    public void afterAnswerLoggingAdvice(){
        System.out.println("afterAnswerLoggingAdvice: получен ответ на один вопрос");
    }
}

package com.toyproject.bookmanagement.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.toyproject.bookmanagement.exception.CustomException;

@Aspect
@Component
public class ValidationAop {
	
	// 모든 정보를 다 들고 온다.(* Controller에 있는 @Valid의 정보를 들고오는 것)
	@Pointcut("@annotation(com.toyproject.bookmanagement.aop.annotation.ValidAspect)")
	private void pointCut() {}
	
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		
		// [0]: Dto, [1]: BindingResult. 하지만 Dto의 오류를 전부 BR한테 넘기기 때문에 실질적은 오류에 관한 모든 정보는 BR한테 있다.
		Object[] args = joinPoint.getArgs();
		BindingResult bindingResult = null;
		
		for(Object arg : args) {
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				bindingResult = (BeanPropertyBindingResult) arg;
			}
		}
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			bindingResult.getFieldErrors().forEach(error -> {
				errorMap.put(error.getField(), error.getDefaultMessage());
			});
			throw new CustomException("Validation Failed", errorMap);
		}
		
		return joinPoint.proceed();
	}
}

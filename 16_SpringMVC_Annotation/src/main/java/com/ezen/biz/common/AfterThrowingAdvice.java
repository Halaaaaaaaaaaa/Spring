package com.ezen.biz.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class AfterThrowingAdvice {
	//@Pointcut("execution(* com.ezen.biz..*Impl.*(..))")
	//	public void allPointcut() {}
	
	@AfterThrowing(pointcut="PointcutCommon.allPointcut()", throwing="exceptObj")
	public void exceptionLog(JoinPoint jp, Exception exceptObj) {
		String method = jp.getSignature().getName();

		
		System.out.printf("[예외 처리] " + method + 
				"() 수행 중 예외 발생:" + exceptObj.getMessage());
	}
}

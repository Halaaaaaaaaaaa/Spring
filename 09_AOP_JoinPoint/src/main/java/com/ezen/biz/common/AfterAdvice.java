package com.ezen.biz.common;

public class AfterAdvice {

	public void finallyLog() {
		System.out.println("\n*** [사후 처리] 비즈니스 로직 수행 후 무조건 수행 ***");
	}
	
}

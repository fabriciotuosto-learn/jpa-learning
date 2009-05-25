package org.jpa.learning.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jpa.utils.Timer;

/**
 *
 * @author fabricio
 *
 */
public class PerformanceInterceptor implements MethodInterceptor
{

	/**
	 *
	 */
	public Object invoke(MethodInvocation arg0) throws Throwable {
		Timer timer = new Timer();
		Object result = null;
		try {
			result = arg0.proceed();
		} finally{
			System.out.println(String.format("%s() -> %s",arg0.getMethod().getName(),timer.elapsedTimeMessage()));
		}
		return result;
	}

}


package org.jpa.learning.guice;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import javax.persistence.EntityManager;

import org.jpa.learning.annotations.PerforamanceLog;
import org.jpa.learning.annotations.Tranactional;
import org.jpa.learning.interceptors.PerformanceInterceptor;
import org.jpa.learning.interceptors.TransactionInterceptor;

import com.google.inject.AbstractModule;

/**
 *
 * @author fabricio
 *
 */
public class JpaTestModule extends AbstractModule {

	/**
	 *
	 */
	@Override
	protected void configure() {
		bind(EntityManager.class)
		.toProvider(EntityManagerProvider.class);

		bindInterceptor(any(),
				annotatedWith(Tranactional.class),
				new TransactionInterceptor());

		bindInterceptor(any(),
				annotatedWith(PerforamanceLog.class),
				new PerformanceInterceptor());
	}

}


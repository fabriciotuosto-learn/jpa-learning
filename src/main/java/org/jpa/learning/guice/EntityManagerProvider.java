package org.jpa.learning.guice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 *
 * @author fabricio
 *
 */
@Singleton
public class EntityManagerProvider implements Provider<EntityManager>
{
	/**
	 *
	 */
	private final EntityManagerFactory factory;

	public EntityManagerProvider() {
		factory = Persistence.createEntityManagerFactory("test");
	}

	/**
	 *
	 */
	public EntityManager get() {
		return factory.createEntityManager();
	}

}


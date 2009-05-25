package org.jpa.learning.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.jpa.learning.annotations.PerforamanceLog;
import org.jpa.learning.annotations.Tranactional;

import com.google.inject.Inject;

/**
 *
 * @author fabricio
 *
 */
public class Dao {
	/**
	 *
	 */
	private static final Map<String, Object> EMPTY_PARAMS = Collections.emptyMap();

	/**
	 *
	 */
	private final EntityManager em;

	/**
	 *
	 * @param em
	 */
	@Inject
	public Dao(EntityManager em) {
		this.em = em;
	}

	/**
	 *
	 * @return
	 */
	public JpaQueryBuilder getQueryBuilder(Class<?> klass) {
		return new JpaQueryBuilder(em,klass);
	}

	/**
	 *
	 * @param <E>
	 * @param elements
	 * @return
	 */
	@Tranactional @PerforamanceLog
	public <E> E remove(E elements) {
		em.remove(elements);
		return elements;
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	@Tranactional @PerforamanceLog
	public <E> List<E> removeAll(Class<E> clazz) {
		List<E> elist = findAllByClass(clazz);
		for(E e : elist)
		{
			remove(e);
		}
		return elist;
	}

	/**
	 *
	 * @param <E>
	 * @param <F>
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Tranactional
	@PerforamanceLog
	public <E, F> E removeById(Class<E> clazz, F id) {
		E e = findById(clazz, id);
		em.remove(e);
		return e;
	}

	/**
	 *
	 * @param <E>
	 * @param e
	 * @return
	 */
	@Tranactional @PerforamanceLog
	public <E> E persist(E e) {
		em.persist(e);
		return e;
	}

	/**
	 *
	 * @param <E>
	 * @param e
	 * @return
	 */
	@Tranactional @PerforamanceLog
	public <E> E persistNow(E e) {
		persist(e);
		em.flush();
		return e;
	}

	/**
	 *
	 * @param <E>
	 * @param e
	 * @return
	 */
	@Tranactional @PerforamanceLog
	public <E> E merge(E e) {
		em.merge(e);
		return e;
	}

	/**
	 *
	 * @return
	 */
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 *
	 * @param <E>
	 * @param <P>
	 * @param clazz
	 * @param id
	 * @return
	 */
	@PerforamanceLog
	public <E, P> E findById(Class<E> clazz, P id) {
		return em.find(clazz, id);
	}

	/**
	 *
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	@PerforamanceLog
	public <E> List<E> findAllByClass(Class<E> clazz) {
		return findByQuery(String.format("select obj from %s obj", clazz.getCanonicalName()), EMPTY_PARAMS);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @param params
	 * @return
	 */
	@PerforamanceLog
	public <E> E findUniqueByQuery(String query, Map<String, Object> params) {
		Validate.notNull(query);
		return findByQuery(em.createQuery(query), params, ResultStrategy.SINGLE);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @param params
	 * @return
	 */
	@PerforamanceLog
	public <E> List<E> findByQuery(String query, Map<String, Object> params) {
		Validate.notNull(query);
		return findByQuery(em.createQuery(query), params);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @param params
	 * @return
	 */
	@PerforamanceLog
	public <E> List<E> findByQuery(Query query, Map<String, Object> params) {
		return findByQuery(query, params, ResultStrategy.LIST);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @return
	 */
	@PerforamanceLog
	public <E> List<E> findByQuery(Query query) {
		return findByQuery(query, ResultStrategy.LIST);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @param strategy
	 * @return
	 */
	@PerforamanceLog
	public <E> E findByQuery(Query query, ResultStrategy strategy) {
		return strategy.getResult(query);
	}

	/**
	 *
	 * @param <E>
	 * @param query
	 * @param params
	 * @param strategy
	 * @return
	 */
	@PerforamanceLog
	public <E> E findByQuery(Query query, Map<String, Object> params,
			ResultStrategy strategy) {
		// validate arguments
		Validate.notNull(query);
		Validate.notNull(strategy);
		// Set parameters if they exists
		if (params != null && !params.isEmpty()) {
			for (Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
		// return a list or objects according to the strategy
		return strategy.getResult(query);
	}

	/**
	 *
	 * @param <E>
	 * @param strategy
	 * @param query
	 * @param params
	 * @return
	 */
	@PerforamanceLog
	public <E> E findByQuery(ResultStrategy strategy, String query,
			Object... params) {
		return findByQuery(strategy, em.createQuery(query), params);
	}

	/**
	 *
	 */
	@PerforamanceLog
	public <E> E findByQuery(ResultStrategy strategy, Query query,
			Object... params) {
		// Validate arguments
		Validate.notNull(query);
		Validate.notNull(strategy);
		// Set parameters if they were passed
		if (!ArrayUtils.isEmpty(params)) {
			int index = 1;
			for (Object param : params) {
				query.setParameter(index++, param);
			}
		}
		// return a list or objects according to the strategy
		return strategy.getResult(query);
	}


	/**
	 *
	 */
	public void close() {
		// Close if it's not null and it is open
		if (em != null && em.isOpen()) {
			em.close();
		}
	}

	/**
	 *
	 * @author fabricio
	 *
	 */
	public static enum ResultStrategy {
		SINGLE {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T performQuery(Query query) {
				return (T) query.getSingleResult();
			}
		},
		LIST {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T performQuery(Query query) {
				return (T) query.getResultList();
			}
		};

		/**
		 *
		 * @param <T>
		 * @param query
		 * @return
		 */
		public <T> T getResult(Query query) {
			Validate.notNull(query, "El query no puede ser <null>");
			return performQuery(query);
		}

		/**
		 *
		 * @param <T>
		 * @param query
		 * @return
		 */
		protected abstract <T> T performQuery(Query query);
	}
}


package org.jpa;

import java.util.Map;

import javax.persistence.Query;

import org.jpa.learning.dao.Dao;
import org.jpa.learning.dao.Dao.ResultStrategy;
import org.jpa.learning.entity.Libro;
import org.jpa.learning.entity.User;
import org.jpa.learning.guice.JpaTestModule;
import org.log.utils.Logger;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class JpaMain
{
	private final static Logger logger = Logger.newStringFormatLogger(JpaMain.class);

	public static void main(String[] args)
	{

		try {
			// Guice creation
			Injector injector = Guice.createInjector(new JpaTestModule());
			// Asking guice for an instance of Dao object
			Dao dao = injector.getInstance(Dao.class);
			// Creating new User
			User user = new User();
			user.setName("Fabricio");
			user.setLastName("Tuosto");
			logger.info("The user is = %s", user);
			logger.info("The user is = %s", dao.persist(user));
			// Creating new User
			user = new User();
			user.setName("Lorena");
			user.setLastName("Rodriguez");
			// Persist users
			logger.info("The user is = %s", dao.persist(user));
			logger.info("The users are [%s]", dao.findAllByClass(User.class));
			// Creating a new book
			Libro libro = new Libro();
			libro.setAutor("Italo Svevo");
			libro.setIsbn("11111-222211-11");
			libro.setTitulo("La Conciencia de Zeno");
			// Before and after persistence
			System.out.println(libro);
			System.out.println(dao.persist(libro));
			// Finding all Libro
			System.out.println(dao.findAllByClass(Libro.class));
			// Finding with hash parameters
			Map<String, Object> params = Maps.newHashMap();
			params.put("id", 1L);
			System.out.println(dao.findUniqueByQuery("select libro from Libro libro where libro.id = :id",params));
			// Finding with variable arguments
			System.out.println(dao.findByQuery(ResultStrategy.SINGLE,"select libro from Libro libro where libro.titulo = ?","La Conciencia de Zeno"));
			// Finding with query created by JpaQueryBuilder
			Query query = dao.getQueryBuilder(Libro.class)
							  .equals("titulo", "La Conciencia de Zeno")
							  .equals("autor","Italo Svevo")
							  .like("titulo", "%")
							  .build();
			logger.info("Results find by query = '%s' are = [%s]", query.toString(), dao.findByQuery(query));
			// Finding with query created by JpaQueryBuilder
			query = dao.getQueryBuilder(User.class).like("name", "%").build();
			logger.info("Results find by query = '%s' are = [%s]", query.toString(), dao.findByQuery(query));
			// Removing everything before exiting
			logger.info("Removed Entities are [%s]", dao.removeAll(User.class));
			logger.info("Removed Entities are [%s]", dao.removeAll(Libro.class));
			// finally closing and exiting
			dao.close();

			throw new Exception("Saparapitame la parapitonga ");
		} catch (Exception e) {
			logger.error("Error ocurred", e);
		}
	}
}


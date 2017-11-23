package br.com.marcospcruz.gestorloja.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

public class CrudDao<T> implements Crud<T> {

	private static final String PERSISTENCE_UNITY = "controlePU";

	private EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager;

	public CrudDao() {

		super();

		inicializaEntityManager();

	}

	/**
	 * M�todo respons�vel pela inicializa��o do EntityManagerFactory e
	 * EntityManager
	 */
	private void inicializaEntityManager() {

		if (entityManager == null || !entityManager.isOpen()) {

			// if (entityManagerFactory == null)

			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNITY);

			entityManager = entityManagerFactory.createEntityManager();

		}

	}

	public T select(int id) {

		return null;
	}

	public T update(T entity) {

		inicializaEntityManager();

		try {

			entityManager.getTransaction().begin();

			entity = entityManager.merge(entity);

			entityManager.getTransaction().commit();

		} catch (PersistenceException e) {

			e.printStackTrace();

		} finally {

			entityManager.close();

		}

		return entity;

	}

	public void delete(T entity) {

		entityManager.getTransaction().begin();

		entityManager.remove(entity);

		entityManager.getTransaction().commit();

		entityManager.close();

	}

	public T busca(Class clazz, int id) {

		T entity = (T) entityManager.find(clazz, id);

		return entity;

	}

	public List<T> buscaList(String namedQuery, String parametro, String valor) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		query.setParameter(parametro, valor);

		List<T> entities = query.getResultList();

		return entities;

	}

	public T busca(String namedQuery, String parametro, Object value) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		query.setParameter(parametro, value);

		T entity = (T) query.getSingleResult();

		return entity;

	}

	public List<T> busca(String namedQuery) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		List<T> objetos = query.getResultList();

		return objetos;

	}

	@Override
	public T busca(String namedQuery, String param1, String paramValue1, String param2, String paramValue2) {
		inicializaEntityManager();
		Query query = createQuery(namedQuery);
		query.setParameter(param1, paramValue1);
		query.setParameter(param2, paramValue2);

		T entity = (T) query.getSingleResult();
		return entity;
	}

	private Query createQuery(String namedQuery) {

		return entityManager.createNamedQuery(namedQuery);
	}

}

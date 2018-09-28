package br.com.marcospcruz.gestorloja.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class CrudDao<T> implements Crud<T> {

	private static final String PERSISTENCE_UNITY = "controlePU";

	private static EntityManagerFactory entityManagerFactory;

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

			if (entityManagerFactory == null)

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
			SingletonManager.getInstance().getLogger(this.getClass()).info("Salvando entidade " + entity);
		} catch (Exception e) {

			SingletonManager.getInstance().getLogger(this.getClass()).error(e.getMessage(),e);

			throw e;

		} finally {

			closeEntityManager();

		}

		return entity;

	}

	public void delete(T entity) {

		if (!entityManager.isOpen())
			inicializaEntityManager();

		try {
			entityManager.getTransaction().begin();
			entity = entityManager.merge(entity);
			SingletonManager.getInstance().getLogger(getClass()).info("Removendo " + entity);
			entityManager.remove(entity);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			SingletonManager.getInstance().getLogger(getClass()).error(e);

			throw e;

		} finally {

			closeEntityManager();
		}

	}

	private void closeEntityManager() {
		entityManager.close();

		// entityManagerFactory.close();
	}

	public T busca(Class clazz, int id) {

		inicializaEntityManager();

		T entity = (T) entityManager.find(clazz, id);
		closeEntityManager();
		return entity;

	}

	public List<T> buscaList(String namedQuery, String parametro, String valor) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		query.setParameter(parametro, valor);

		List<T> entities = query.getResultList();
		closeEntityManager();
		return entities;

	}

	public T busca(String namedQuery, String parametro, Object value) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		query.setParameter(parametro, value);

		T entity = (T) query.getSingleResult();
		closeEntityManager();
		return entity;

	}

	public List<T> busca(String namedQuery) {

		inicializaEntityManager();

		Query query = entityManager.createNamedQuery(namedQuery);

		List<T> objetos = query.getResultList();
		closeEntityManager();
		return objetos;

	}

	@Override
	public T busca(String namedQuery, Object... params) {
		inicializaEntityManager();
		Query query = createQuery(namedQuery);
		for (int i = 0; i < params.length;)
			query.setParameter(params[i++].toString(), params[i++]);
		// query.setParameter(params[2].toString(), params[3].toString());

		T entity = (T) query.getSingleResult();
		closeEntityManager();
		return entity;
	}

	private Query createQuery(String namedQuery) {

		return entityManager.createNamedQuery(namedQuery);
	}

	@Override
	public List<T> buscaList(String namedQuery, String... params) {
		inicializaEntityManager();
		Query query = createQuery(namedQuery);
		int i = 0;
		while (i < params.length) {
			String variable = params[i++];
			Object param = params[i++];
			query.setParameter(variable, param);
		}
		List<T> results = (List<T>) query.getResultList();
		closeEntityManager();
		return results;

	}

	@Override
	public List<T> buscaList(String namedQuery, Map<String, String> paramsMap) {
		inicializaEntityManager();
		Query query = createQuery(namedQuery);

		paramsMap.keySet().stream().forEach(key -> {
			query.setParameter(key, paramsMap.get(key));
		});
		List<T> results = (List<T>) query.getResultList();
		closeEntityManager();
		return results;
	}

}

package br.com.marcospcruz.gestorloja.dao;

import java.util.List;

import br.com.marcospcruz.gestorloja.model.Usuario;

public interface Crud<T> {
	/**
	 * M�todo respons�vel em procurar entidade no banco de dados.
	 * 
	 * @param id
	 * @return
	 */
	public T select(int id);

	/**
	 * M�todo respons�vel em atualizar a entidade no banco.
	 * 
	 * @param tipoPeca
	 * @return
	 */
	public T update(T entity);

	public List<T> busca(String namedQuery);

	public T busca(String namedQuery, String parametro, Object valor);

	public void delete(T entity);

	public T busca(Class clazz, int id);

	public List<T> buscaList(String query, String parametro, String valor);

	public T busca(String namedQuery, String param1, String paramValue1, String param2, String paramValue2);

}

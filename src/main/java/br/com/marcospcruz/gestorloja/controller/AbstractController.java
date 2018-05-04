package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public interface AbstractController {

	static final String BUSCA_INVALIDA = "Busca Invïálida";

	/**
	 * 
	 * @return
	 */
	default Usuario getUsuarioLogado() {
		return SingletonManager.getInstance().getUsuarioLogado();
	}

	/**
	 * 
	 * @param parametro
	 * @return
	 */
	default boolean contemAcentuacao(String parametro) {

		String pattern = "ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½";

		for (char caractere : pattern.toCharArray())

			for (char c : parametro.toCharArray())

				if (c == caractere)

					return true;

		return false;

	}

	default AbstractController getController(String controllerClass) throws Exception {
		return SingletonManager.getInstance().getController(controllerClass);
	}

	public abstract void busca(Object id) throws Exception;

	public abstract List buscaTodos();

	public abstract List getList();

	public abstract void busca(String text) throws Exception;

	public abstract Object getItem();

	public abstract void setList(List list);

	public abstract void setItem(Object object);

	public abstract void excluir() throws Exception;

	public abstract void salva(String text, boolean b, Object object) throws Exception;

	public abstract void salva(Object object) throws Exception;

}

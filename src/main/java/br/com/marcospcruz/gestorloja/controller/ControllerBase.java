package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public abstract class ControllerBase {

	static final String BUSCA_INVALIDA = "Busca InvÔ·lida";

	/**
	 * 
	 * @return
	 */
	public Usuario getUsuarioLogado() {
		Usuario usuario = SingletonManager.getInstance().getUsuarioLogado();
		if (usuario == null) {
			usuario = new Usuario();
			usuario.setIdUsuario(1);
		}
		return usuario;
	}

	/**
	 * 
	 * @param parametro
	 * @return
	 */
	protected boolean contemAcentuacao(String parametro) {

		String pattern = "«√…Õ”⁄’";

		for (char caractere : pattern.toLowerCase().toCharArray())

			for (char c : parametro.toLowerCase().toCharArray())

				if (c == caractere) {

					return true;

				}
		return false;

	}

	protected ControllerBase getController(String controllerClass) throws Exception {
		return SingletonManager.getInstance().getController(controllerClass);
	}

	protected void reloadLista() {

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

	public abstract void salva(Object object, boolean validaDados) throws Exception;

	public abstract void salva() throws Exception;

	public abstract void registraHistoricoOperacao(Operacao operacao);

	public abstract void validaExistente(String text) throws Exception;

	public abstract void novo();

	protected void logDebug(String message) {
		final Logger logger = SingletonManager.getInstance().getLogger(this.getClass());
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

}

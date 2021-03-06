package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public abstract class ControllerBase {

	static final String BUSCA_INVALIDA = "Busca Inv�lida";

	private Map<Object, Object> cacheMap;
	
//	private List cacheList;

	public Map<Object, Object> getCacheMap() {
		return cacheMap;
	}

	public void setCacheMap(Map<Object, Object> cacheMap) {
		this.cacheMap = cacheMap;
	}

	public ControllerBase() {
		cacheMap = new HashMap<>();
//		cacheList=new ArrayList<>();
	}

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

		String pattern = "����";

		for (char caractere : pattern.toCharArray())

			for (char c : parametro.toCharArray())

				if (c == caractere)

					return true;

		return false;

	}

	protected ControllerBase getController(String controllerClass) throws Exception {
		return SingletonManager.getInstance().getController(controllerClass);
	}

	protected void reloadLista() {

	}

//	public List getCacheList() {
//		return cacheList;
//	}
//
//	public void setCacheList(List cacheList) {
//		this.cacheList = cacheList;
//	}

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

	public abstract void carregaCache();

	public abstract String validaExclusaoItem() ;
	

}

package br.com.marcospcruz.gestorloja.systemmanager;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.AbstractController;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class SingletonManager {

	private static SingletonManager instance;
	private Usuario usuarioLogado;

	private SingletonManager() {

	}

	public static SingletonManager getInstance() {
		if (instance == null)
			instance = new SingletonManager();
		return instance;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioLogado() {

		return usuarioLogado;
	}

	public AbstractController getController(String controllerClass) throws Exception {
		
		return ControllerAbstractFactory.createController(controllerClass);
	}

}

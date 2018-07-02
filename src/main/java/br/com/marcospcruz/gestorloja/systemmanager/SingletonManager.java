package br.com.marcospcruz.gestorloja.systemmanager;

import java.util.HashMap;
import java.util.Map;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class SingletonManager {

	private static SingletonManager instance;
	private Usuario usuarioLogado;
	private Map<String, ControllerBase> controllersMap;

	private SingletonManager() {

	}

	public static SingletonManager getInstance() {
		if (instance == null) {
			instance = new SingletonManager();
		}
		return instance;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioLogado() {

		return usuarioLogado;
	}

	public ControllerBase getController(String controllerClass) throws Exception {
		if (controllersMap == null)
			controllersMap = new HashMap<>();

		if (!controllersMap.containsKey(controllerClass)) {
			ControllerBase controller=ControllerAbstractFactory.createController(controllerClass);
			controller.buscaTodos();
			controllersMap.put(controllerClass, controller);
		}
		return controllersMap.get(controllerClass);
	}

	public void reloadControllers() {
		controllersMap.values().stream().forEach(controller -> {
			controller.setList(null);
			controller.buscaTodos();
		});

	}

}

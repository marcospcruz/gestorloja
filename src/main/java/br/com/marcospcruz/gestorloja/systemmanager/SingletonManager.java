package br.com.marcospcruz.gestorloja.systemmanager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class SingletonManager {

	private static SingletonManager instance;
	private Usuario usuarioLogado;
	private Map<String, ControllerBase> controllersMap;
	private LocalDate dataManutencao;

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
			ControllerBase controller = ControllerAbstractFactory.createController(controllerClass);
			controller.buscaTodos();
			controllersMap.put(controllerClass, controller);
		}
		return controllersMap.get(controllerClass);
	}

	// public void reloadControllers() {
	// controllersMap.values().stream().forEach(controller -> {
	// controller.setList(null);
	// controller.buscaTodos();
	// });
	//
	// }

	public void setDataManutencaoSistema(LocalDate dataManutencao) {
		this.dataManutencao = dataManutencao;

	}

	public LocalDate getDataManutencao() {
		return dataManutencao;
	}

	public void setDataManutencao(LocalDate dataManutencao) {
		this.dataManutencao = dataManutencao;
	}

	public Date getData() {
		if (dataManutencao == null) {
			return new Date();
		}
		Date data = Date.from(dataManutencao.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return data;
	}

	public void removeController(ControllerBase controller) {
		Set<Entry<String, ControllerBase>> entrySet = controllersMap.entrySet();
		Entry<String, ControllerBase> teste = entrySet.stream().filter(entry -> controller.equals(entry.getValue()))
				.findAny().orElse(null);
		// .filter(entry->Objects.equals(controller,entry))
		// .map(Map.Entry::getKey)
		// .collect(Collectors.toSet());
		//
		controllersMap.remove(teste.getKey());
		System.out.println(controllersMap);
	}

	public void resetControllers() {
		controllersMap.values().stream().forEach(controller -> {
			controller.setList(null);
		});

	}

}

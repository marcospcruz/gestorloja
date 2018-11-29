package br.com.marcospcruz.gestorloja.systemmanager;

import java.awt.Dimension;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.util.Util;

public class SingletonManager {
	private static final double DEZ_PORCENTO = 10d / 100d;
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
	private String getConfigurationPropertyValue(String propertyKey) throws IOException {
		Properties properties = Util.getConfigFileProperties();
		return properties.getProperty(propertyKey);
	}

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

	private Dimension getScreenSize() {

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		return screenSize;
	}

	public double getScreenWidth() {
		try {
			return Util.getDoubleValue(getConfigurationPropertyValue("width"));
		} catch (IOException e) {

			return getScreenSize().getWidth();
		}

	}

	public double getScreenHeight() {
		try {
			return Util.getDoubleValue(getConfigurationPropertyValue("height"));
		} catch (IOException e) {
			return getScreenSize().getHeight();
		}
	}

	public boolean isPermiteVendaSemControlarEstoque() {
		try {
			return Boolean.parseBoolean(getConfigurationPropertyValue("permiteVendaSemControlarEstoque"));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	public Logger getLogger(Class clazz) {

		return Logger.getLogger(clazz);
	}

	public boolean isManutencao() {

		return usuarioLogado.getIdUsuario() == 1;
	}

}

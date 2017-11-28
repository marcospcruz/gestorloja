package br.com.marcospcruz.gestorloja.abstractfactory;

import br.com.marcospcruz.gestorloja.controller.AbstractController;

public class ControllerAbstractFactory {

	private static final String PACKAGE = "br.com.marcospcruz.gestorloja.controller.";
	public static final String TIPO_PRODUTO_CONTROLLER = PACKAGE + "TipoProdutoController";
	public static final String FABRICANTE = PACKAGE + "FabricanteController";
	public static final String PRODUTO = PACKAGE + "ProdutoController";
	public static final String ESTOQUE = PACKAGE + "EstoqueController";
	public static final String CONTROLE_CAIXA = PACKAGE + "CaixaController";
	

	private ControllerAbstractFactory() {
	}

	public static AbstractController createController(String string)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class controllerClass = Class.forName(string);
		return (AbstractController) controllerClass.newInstance();

	}

}

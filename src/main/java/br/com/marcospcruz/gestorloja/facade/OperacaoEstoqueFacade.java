package br.com.marcospcruz.gestorloja.facade;

import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;

import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class OperacaoEstoqueFacade {

	private static final Operacao ENTRADA_ESTOQUE = new Operacao(1);
	public static final Operacao SAIDA_ESTOQUE = new Operacao(2);
	private ControllerBase controller;

	public OperacaoEstoqueFacade(EstoqueController controller) {
		this.controller = controller;
	}

	public void adicionaItemEstoque(ItemEstoque itemEstoque) throws Exception {
		controller.setItem(itemEstoque);
//		((EstoqueController)controller).validaProdutoExistente();
		controller.salva();

		controller.registraHistoricoOperacao(ENTRADA_ESTOQUE);
	
//		((EstoqueController)controller).anulaAtributos();
//		SingletonManager.getInstance().reloadControllers();
	}

}

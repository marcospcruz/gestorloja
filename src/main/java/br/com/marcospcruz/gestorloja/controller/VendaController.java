package br.com.marcospcruz.gestorloja.controller;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Venda;

public class VendaController {
	private EstoqueController estoqueController;

	public VendaController() {
		estoqueController = new EstoqueController();
	}

	public ItemEstoque buscaProduto(String codigoProduto) throws Exception {

		ItemEstoque itemEstoque = null;
		try {
			itemEstoque = estoqueController.buscaItemPorCodigoDeBarras(codigoProduto);
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new Exception("Produto código " + codigoProduto + " não encontrado no estoque.");
		}

		return itemEstoque;
	}

	public void subtraiEstoque(Venda venda) throws Exception {
		ItemEstoque itemEstoque = venda.getItemEstoque();
		Integer quantidadeEstoque = venda.getQuantidade();

		if (itemEstoque.getQuantidade() < quantidadeEstoque) {
			throw new Exception("Quantidade de ítens no estoque é insuficiente para a venda!");
		}
		estoqueController.setItemEstoque(itemEstoque);
		estoqueController.atualizaItem(itemEstoque.getQuantidade() - quantidadeEstoque);
	}

}

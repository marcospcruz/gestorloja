package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;

public class VendaController implements AbstractController {
	private EstoqueController estoqueController;
	private ItemVenda itemVenda;

	public VendaController() throws Exception {
		super();
		estoqueController = (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
		resetItemVenda();
	}

	public void buscaProduto(String codigoProduto) throws Exception {

		try {
			estoqueController.buscaItemPorCodigoDeBarras(codigoProduto);
			itemVenda.setItemEstoque(estoqueController.getItemEstoque());
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new Exception("Produto código " + codigoProduto + " não encontrado no estoque.");
		}

	}

	public void subtraiEstoque(ItemVenda venda) throws Exception {
		ItemEstoque itemEstoque = venda.getItemEstoque();
		Integer quantidadeEstoque = venda.getQuantidade();

		if (itemEstoque.getQuantidade() < quantidadeEstoque) {
			throw new Exception("Quantidade de ítens no estoque é insuficiente para a venda!");
		}
		estoqueController.setItemEstoque(itemEstoque);
		estoqueController.incrementaItem(itemEstoque.getQuantidade() - quantidadeEstoque);
	}

	@Override
	public void busca(Object id) throws Exception {

		buscaProduto(id.toString());

	}

	@Override
	public List buscaTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void busca(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setList(List list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;

	}

	public void resetItemVenda() {
		setItemVenda(new ItemVenda());

		itemVenda.setOperador(getUsuarioLogado());

	}

	public void devolveProduto() throws Exception {
		try {
			estoqueController.buscaItemPorCodigoDeBarras(itemVenda.getItemEstoque().getProduto().getCodigoDeBarras());
		} catch (Exception e) {

			e.printStackTrace();
		}
		estoqueController.incrementaItem(itemVenda.getQuantidade() + itemVenda.getItemEstoque().getQuantidade());
		resetItemVenda();
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

package br.com.marcospcruz.gestorloja.builder;

import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;

public class ItemEstoqueBuilder {

	private ItemEstoque itemEstoque;

	public ItemEstoqueBuilder() {
		itemEstoque = new ItemEstoque();
	}

	public ItemEstoqueBuilder setCodigoDeBarras(String string) {
		itemEstoque.setCodigoDeBarras(string.trim());
		return this;
	}

	public ItemEstoque getItemEstoque() {

		return itemEstoque;
	}

	public ItemEstoqueBuilder setCategoriaProduto(String string) {
		SubTipoProduto tipo = new SubTipoProduto();
		tipo.setDescricaoTipo(string.trim());
		itemEstoque.setTipoProduto(tipo);
		return this;
	}

	public ItemEstoqueBuilder setSuperCategoriaProduto(String string) {
		SubTipoProduto tipo = new SubTipoProduto();
		tipo.setDescricaoTipo(string.trim());
		itemEstoque.getTipoProduto().setSuperTipoProduto(tipo);
		return this;
	}

	public ItemEstoqueBuilder setFabricante(String string) {
		Fabricante fabricante = new Fabricante();
		fabricante.setNome(string.trim());
		itemEstoque.setFabricante(fabricante);
		return this;
	}

	public ItemEstoqueBuilder setProduto(String string) {
		try {
			Produto produto = new Produto();
			produto.setDescricaoProduto(string.trim());
			itemEstoque.setProduto(produto);

		} catch (NullPointerException e) {
			SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage(), e);
		}
		return this;
	}

	public ItemEstoqueBuilder setValorUnitario(String string) {
		float valorUnitario = Util.parseStringDecimalToFloat(string.trim());
		itemEstoque.setValorUnitario(valorUnitario);
		return this;
	}

	public ItemEstoqueBuilder setQuantidade(String string) {
		int qt = 0;
		if (string != null && !string.isEmpty())
			qt = Integer.parseInt(string.trim());
		itemEstoque.setQuantidade(qt);
		return this;
	}

}

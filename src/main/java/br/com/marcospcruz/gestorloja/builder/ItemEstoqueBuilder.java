package br.com.marcospcruz.gestorloja.builder;

import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.util.Util;

public class ItemEstoqueBuilder {

	private ItemEstoque itemEstoque;

	public ItemEstoqueBuilder() {
		itemEstoque = new ItemEstoque();
	}

	public ItemEstoqueBuilder setCodigoDeBarras(String string) {
		itemEstoque.setCodigoDeBarras(string);
		return this;
	}

	public ItemEstoque getItemEstoque() {

		return itemEstoque;
	}

	public ItemEstoqueBuilder setCategoriaProduto(String string) {
		SubTipoProduto tipo = new SubTipoProduto();
		tipo.setDescricaoTipo(string);
		itemEstoque.setTipoProduto(tipo);
		return this;
	}

	public ItemEstoqueBuilder setSuperCategoriaProduto(String string) {
		SubTipoProduto tipo = new SubTipoProduto();
		tipo.setDescricaoTipo(string);
		itemEstoque.getTipoProduto().setSuperTipoProduto(tipo);
		return this;
	}

	public ItemEstoqueBuilder setFabricante(String string) {
		Fabricante fabricante = new Fabricante();
		fabricante.setNome(string);
		itemEstoque.setFabricante(fabricante);
		return this;
	}

	public ItemEstoqueBuilder setProduto(String string) {
		Produto produto = new Produto();
		produto.setDescricaoProduto(string);
		itemEstoque.setProduto(produto);
		return this;
	}

	public ItemEstoqueBuilder setValorUnitario(String string) {
		float valorUnitario=Util.parseStringDecimalToFloat(string);
		itemEstoque.setValorUnitario(valorUnitario);
		return this;
	}

}

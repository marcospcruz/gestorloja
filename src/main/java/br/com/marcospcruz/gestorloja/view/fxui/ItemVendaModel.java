package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.util.Util;
import javafx.beans.property.SimpleStringProperty;

public class ItemVendaModel {

	private SimpleStringProperty codigoProduto;
	private SimpleStringProperty fabricante;
	private SimpleStringProperty categoria;
	private SimpleStringProperty produto;
	private SimpleStringProperty quantidade;
	private SimpleStringProperty valorProduto;
	private SimpleStringProperty valorVenda;
	private SimpleStringProperty valorTotal;

	public ItemVendaModel(ItemVenda item) {
		setCodigoProduto(item.getItemEstoque().getCodigoDeBarras());
		setFabricante(item.getItemEstoque().getFabricante().getNome());
		setCategoria(item.getItemEstoque().getTipoProduto().getDescricaoTipo());
		setProduto(item.getItemEstoque().getProduto().getDescricaoProduto());
		setValorProduto(item.getItemEstoque().getValorUnitario());
		setQuantidade(item.getQuantidade().toString());
		setValorVenda(Util.formataMoeda(item.getValorVendido()));
		setValorTotal(Util.formataMoeda(item.getQuantidade() * item.getValorVendido()));
	}

	public void setValorTotal(String valorTotal) {
		this.valorTotal = new SimpleStringProperty(valorTotal);
	}

	public String getValorTotal() {
		return valorTotal.get();
	}

	public void setValorVenda(String valorVenda) {
		this.valorVenda = new SimpleStringProperty(valorVenda);
	}

	public String getValorVenda() {
		return valorVenda.get();
	}

	private void setValorProduto(float valorUnitario) {
		valorProduto = new SimpleStringProperty(Util.formataMoeda(valorUnitario));

	}

	public String getValorProduto() {
		return valorProduto.get();
	}

	void setQuantidade(String quantidade) {
		this.quantidade = new SimpleStringProperty(quantidade);
	}

	public String getQuantidade() {
		return quantidade.get();
	}

	private void setProduto(String descricaoProduto) {
		produto = new SimpleStringProperty(descricaoProduto);
	}

	public String getProduto() {
		return produto.get();
	}

	private void setCategoria(String descricaoTipo) {
		categoria = new SimpleStringProperty(descricaoTipo);
	}

	public String getCategoria() {
		return categoria.get();
	}

	private void setFabricante(String nome) {
		fabricante = new SimpleStringProperty(nome);

	}

	public String getFabricante() {
		return fabricante.get();
	}

	private void setCodigoProduto(String codigoDeBarras) {
		codigoProduto = new SimpleStringProperty(codigoDeBarras);

	}

	public String getCodigoProduto() {
		return codigoProduto.get();
	}

	@Override
	public String toString() {
		return "ItemVendaModel [codigoProduto=" + codigoProduto + ", fabricante=" + fabricante + ", categoria="
				+ categoria + ", produto=" + produto + ", quantidade=" + quantidade + ", valorProduto=" + valorProduto
				+ ", valorVenda=" + valorVenda + ", valorTotal=" + valorTotal + "]";
	}
}

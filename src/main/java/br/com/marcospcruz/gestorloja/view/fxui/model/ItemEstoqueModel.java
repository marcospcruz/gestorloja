package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleStringProperty;

public class ItemEstoqueModel {
	private SimpleStringProperty cod;
	private SimpleStringProperty fabricante;
	private SimpleStringProperty categoria;
	private SimpleStringProperty subCategoria;
	private SimpleStringProperty produto;
	private SimpleStringProperty qt;
	private SimpleStringProperty vlUnitario;
	private SimpleStringProperty vlTotal;
	private int indexOf;

	public ItemEstoqueModel(int indexOf, String codigoProduto, String fabricante, String categoria, String subCategoria,
			String descricaoProduto, String quantidade, String precoFinal, String valorTotal) {
		setIndexOf(indexOf);
		setCod(codigoProduto);
		setFabricante(fabricante);
		setCategoria(categoria);
		setSubCategoria(subCategoria);
		setProduto(descricaoProduto);
		setQt(quantidade);
		setVlUnitario(precoFinal);
		setVlTotal(valorTotal);
	}

	private void setIndexOf(int indexOf) {
		this.indexOf = indexOf;

	}

	public int getIndexOf() {
		return indexOf;
	}

	public String getCod() {
		return cod.get();
	}

	public void setCod(String codigoProduto) {
		if (this.cod == null)
			this.cod = new SimpleStringProperty(codigoProduto);
		else
			this.cod.set(codigoProduto);
	}

	public String getFabricante() {
		return fabricante.get();
	}

	public void setFabricante(String fabricante) {
		if (this.fabricante == null)
			this.fabricante = new SimpleStringProperty(fabricante);
		else
			this.fabricante.set(fabricante);
	}

	public String getCategoria() {
		return categoria.get();
	}

	public void setCategoria(String categoria) {
		if (this.categoria == null)
			this.categoria = new SimpleStringProperty(categoria);
		else
			this.categoria.set(categoria);
	}

	public String getSubCategoria() {
		return subCategoria.get();
	}

	public void setSubCategoria(String subCategoria) {
		if (this.subCategoria == null)
			this.subCategoria = new SimpleStringProperty(subCategoria);
		else
			this.subCategoria.set(subCategoria);
	}

	public String getProduto() {
		return produto.get();
	}

	public void setProduto(String descricaoProduto) {
		if (this.produto == null)
			this.produto = new SimpleStringProperty(descricaoProduto);
		else
			this.produto.set(descricaoProduto);
	}

	public String getQt() {
		return qt.get();
	}

	public void setQt(String quantidade) {
		if (this.qt == null)
			this.qt = new SimpleStringProperty(quantidade);
		else
			this.qt.set(quantidade);
	}

	public String getVlUnitario() {
		return vlUnitario.get();
	}

	public void setVlUnitario(String precoFinal) {
		if (this.vlUnitario == null)
			this.vlUnitario = new SimpleStringProperty(precoFinal);
		else
			this.vlUnitario.set(precoFinal);
	}

	public String getVlTotal() {
		return vlTotal.get();
	}

	public void setVlTotal(String precoFinal) {
		if (this.vlTotal == null)
			this.vlTotal = new SimpleStringProperty(precoFinal);
		else
			this.vlTotal.set(precoFinal);
	}

}

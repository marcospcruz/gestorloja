package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.view.fxui.model.BaseModel;
import javafx.beans.property.SimpleStringProperty;

public class ProdutoModel extends BaseModel {

	private SimpleStringProperty produto;
	private SimpleStringProperty cod;

	public ProdutoModel(Integer idProduto, String descricaoProduto, Integer itensEstoque) {
		setCod(idProduto);
		setDescricaoProduto(descricaoProduto);
		setItensEstoque(itensEstoque);
	}

	private void setDescricaoProduto(String descricaoProduto) {
		this.produto = new SimpleStringProperty(descricaoProduto);

	}

	private void setCod(Integer idProduto) {
		this.cod = new SimpleStringProperty(idProduto.toString());

	}

	public String getProduto() {
		return produto.get();
	}

	public String getCod() {
		return cod.get();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod == null) ? 0 : cod.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoModel other = (ProdutoModel) obj;
		if (cod == null) {
			if (other.cod != null)
				return false;
		} else if (!cod.equals(other.cod))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProdutoModel [produto=" + produto + ", cod=" + cod + ", getItensEstoque()=" + getItensEstoque() + "]";
	}

}

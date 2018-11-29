package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleStringProperty;

public class TipoProdutoModel extends BaseModel {

	private SimpleStringProperty cod;
	private SimpleStringProperty categoria;
	private SimpleStringProperty superCategoria;

	public TipoProdutoModel(Integer idTipoItem, String descricaoTipo, String superCategoria, Integer itensEstoque) {
		setCod(idTipoItem);
		setCategoria(descricaoTipo);
		setSuperCategoria(superCategoria);
		setItensEstoque(itensEstoque);
	}

	private void setSuperCategoria(String subCategoria) {
		this.superCategoria = new SimpleStringProperty(subCategoria);

	}

	private void setCategoria(String descricaoTipo) {
		this.categoria = new SimpleStringProperty(descricaoTipo);
	}

	private void setCod(Integer idTipoItem) {
		this.cod = new SimpleStringProperty(idTipoItem.toString());

	}

	public String getCod() {
		return cod.get();
	}

	public String getCategoria() {
		return categoria.get();
	}

	public void setCategoria(SimpleStringProperty categoria) {
		this.categoria = categoria;
	}

	public String getSuperCategoria() {
		return superCategoria.get();
	}

	@Override
	public String toString() {
		return "TipoProdutoModel [cod=" + cod + ", categoria=" + categoria + ", subCategoria=" + superCategoria + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((cod == null) ? 0 : cod.hashCode());
		result = prime * result + ((superCategoria == null) ? 0 : superCategoria.hashCode());
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
		TipoProdutoModel other = (TipoProdutoModel) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (cod == null) {
			if (other.cod != null)
				return false;
		} else if (!cod.equals(other.cod))
			return false;
		if (superCategoria == null) {
			if (other.superCategoria != null)
				return false;
		} else if (!superCategoria.equals(other.superCategoria))
			return false;
		return true;
	}

}

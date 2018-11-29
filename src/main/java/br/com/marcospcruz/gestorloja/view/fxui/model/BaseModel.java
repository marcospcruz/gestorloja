package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleStringProperty;

public class BaseModel {
	private SimpleStringProperty itensEstoque;
	public String getItensEstoque() {
		return itensEstoque.get();
	}

	public void setItensEstoque(Integer itensEstoque) {
		this.itensEstoque = new SimpleStringProperty(itensEstoque.toString());
	}
}

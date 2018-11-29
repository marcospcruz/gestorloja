package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleStringProperty;

public class FabricanteModel extends BaseModel {

	private SimpleStringProperty cod;
	private SimpleStringProperty nome;

	public FabricanteModel(Integer idFabricante, String nome, Integer itensEstoque) {
		setCod(idFabricante);
		setNome(nome);
		setItensEstoque(itensEstoque);
	}

	private void setNome(String nome) {
		this.nome = new SimpleStringProperty(nome);

	}

	private void setCod(Integer idFabricante) {
		this.cod = new SimpleStringProperty(idFabricante.toString());

	}

	public String getCod() {
		return cod.get();
	}

	public void setIdFabricante(SimpleStringProperty idFabricante) {
		this.cod = idFabricante;
	}

	public String getNome() {
		return nome.get();
	}

	public void setNome(SimpleStringProperty nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "FabricanteModel [cod=" + cod + ", nome=" + nome + ", itensEstoque=" + getItensEstoque() + "]";
	}

}

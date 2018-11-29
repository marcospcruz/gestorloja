package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsuarioModel {

	private SimpleIntegerProperty codigo;
	private SimpleStringProperty nomeCompleto;
	private SimpleStringProperty nomeUsuario;
	private SimpleStringProperty status;

	public int getCodigo() {
		return codigo.get();
	}

	public void setCodigo(int codigo) {
		this.codigo = new SimpleIntegerProperty(codigo);
	}

	public String getNomeCompleto() {
		return nomeCompleto.get();
	}

	public void setNomeCompleto(String nomeUsuario) {
		this.nomeCompleto = new SimpleStringProperty(nomeUsuario);
	}

	public void setNomeUsuario(String usuario) {
		this.nomeUsuario = new SimpleStringProperty(usuario);

	}

	public String getNomeUsuario() {
		return nomeUsuario.get();
	}

	public void setStatus(String status) {
		this.status=new SimpleStringProperty(status);
		
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(SimpleStringProperty status) {
		this.status = status;
	}

}

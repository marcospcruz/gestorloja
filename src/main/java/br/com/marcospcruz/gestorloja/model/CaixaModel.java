package br.com.marcospcruz.gestorloja.model;

import javafx.beans.property.SimpleStringProperty;

public class CaixaModel {
	private SimpleStringProperty abertura, operadorAbertura, trocoInicial, fechamento, trocoFinal, status;
	private SimpleStringProperty operadorFechamento;

	public CaixaModel(String dataInicio, String usuarioAbertura, String trocoInicial, String dataHoraFechamento,
			String operadorFechamento, String trocoFinal, String status) {
		setAbertura(dataInicio);
		setOperadorAbertura(usuarioAbertura);
		setTrocoInicial(trocoInicial);
		setFechamento(dataHoraFechamento);
		setTrocoFinal(trocoFinal);
		setStatus(status);
		setOperadorFechamento(operadorFechamento);
	}

	private void setOperadorFechamento(String operadorFechamento) {
		this.operadorFechamento = new SimpleStringProperty(operadorFechamento);
	}

	public String getOperadorFechamento() {
		return operadorFechamento.get();
	}

	public String getAbertura() {
		return abertura.get();
	}

	public void setAbertura(String abertura) {
		this.abertura = new SimpleStringProperty(abertura);
	}

	public String getOperadorAbertura() {
		return operadorAbertura.get();
	}

	public void setOperadorAbertura(String operador) {
		this.operadorAbertura = new SimpleStringProperty(operador);
	}

	public String getTrocoInicial() {
		return trocoInicial.get();
	}

	public void setTrocoInicial(String trocoInicial) {
		this.trocoInicial = new SimpleStringProperty(trocoInicial);
	}

	public String getFechamento() {
		return fechamento.get();
	}

	public void setFechamento(String fechamento) {
		this.fechamento = new SimpleStringProperty(fechamento);
	}

	public String getTrocoFinal() {
		return trocoFinal.get();
	}

	public void setTrocoFinal(String trocoFinal) {
		this.trocoFinal = new SimpleStringProperty(trocoFinal);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status = new SimpleStringProperty(status);
	}

}

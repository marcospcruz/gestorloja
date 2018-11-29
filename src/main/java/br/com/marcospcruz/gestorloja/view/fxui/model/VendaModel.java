package br.com.marcospcruz.gestorloja.view.fxui.model;

import javafx.beans.property.SimpleStringProperty;

public class VendaModel {

	private SimpleStringProperty dataVenda;
	private SimpleStringProperty nomeVendedor;
	private SimpleStringProperty subTotalVendido;
	private SimpleStringProperty totalRecebido;
	private SimpleStringProperty descontoConcedido;
	private SimpleStringProperty meiosPagamento;
	private SimpleStringProperty valorVenda;
	private SimpleStringProperty id;
	private SimpleStringProperty status;
	private SimpleStringProperty motivoEstorno;

	public void setStatus(String status) {
		this.status = new SimpleStringProperty(status);

	}

	public String getStatus() {
		return status.get();
	}

	public void setDataVenda(String dataVenda) {
		this.dataVenda = new SimpleStringProperty(dataVenda);

	}

	public String getDataVenda() {
		return dataVenda.get();
	}

	public void setNomeVendedor(String nomeVendedor) {
		this.nomeVendedor = new SimpleStringProperty(nomeVendedor);

	}

	public String getNomeVendedor() {
		return nomeVendedor.get();
	}

	public void setSubTotalVendido(String subTotalVendido) {

		this.subTotalVendido = new SimpleStringProperty(subTotalVendido);
	}

	public String getSubTotalVendido() {
		return subTotalVendido.get();
	}

	public void setTotalRecebido(String totalRecebido) {
		this.totalRecebido = new SimpleStringProperty(totalRecebido);

	}

	public String getTotalRecebido() {
		return totalRecebido.get();
	}

	public void setDescontoConcedido(String descontoConcedido) {
		this.descontoConcedido = new SimpleStringProperty(descontoConcedido);

	}

	public String getDescontoConcedido() {
		return descontoConcedido.get();
	}

	public void setMeiosPagamento(String meiosPagamentos) {
		this.meiosPagamento = new SimpleStringProperty(meiosPagamentos);

	}

	public String getMeiosPagamento() {
		return meiosPagamento.get();
	}

	public void setValorVenda(String formataMoeda) {
		this.valorVenda = new SimpleStringProperty(formataMoeda);

	}

	public String getValorVenda() {
		return valorVenda.get();
	}

	public void setId(String id) {
		this.id = new SimpleStringProperty(id);

	}

	public String getId() {
		return id.get();
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = new SimpleStringProperty(motivoEstorno);

	}

	public String getMotivoEstorno() {

		String retorno = motivoEstorno != null && motivoEstorno.get() == null ? "" : motivoEstorno.get();

		return retorno;
	}
}

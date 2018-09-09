package br.com.marcospcruz.gestorloja.view.util;

import java.util.Date;

import javax.script.SimpleScriptContext;

import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.model.VendaModel;

public class VendaModelBuilder {

	private VendaModel vendaModel;

	public VendaModelBuilder() {
		vendaModel = new VendaModel();
	}

	public VendaModel getVendaModel() {

		return vendaModel;
	}

	public VendaModelBuilder setDataVenda(Date dataVenda) {
		vendaModel.setDataVenda(Util.formataDataHora(dataVenda));
		return this;
	}

	public VendaModelBuilder setNomeVendedor(String nomeCompleto) {
		vendaModel.setNomeVendedor(nomeCompleto);
		return this;
	}

	public VendaModelBuilder setSubTotalVendido(float subTotalVendido) {
		vendaModel.setSubTotalVendido(Util.formataMoeda(subTotalVendido));
		return this;
	}

	public VendaModelBuilder setTotalPagamentoRecebido(float totalRecebido) {
		vendaModel.setTotalRecebido(Util.formataMoeda(totalRecebido));
		return this;
	}

	public VendaModelBuilder setPorcentagemDescontoConcedido(float descontoConcedido) {
		vendaModel.setDescontoConcedido(Util.formataStringDecimais(descontoConcedido) + "%");
		return this;
	}

	public VendaModelBuilder setMeiosPagamentos(String meiosPagamentos) {
		vendaModel.setMeiosPagamento(meiosPagamentos);
		return this;
	}

	public VendaModelBuilder setValorBrutoVenda(float totalVendaBruto) {
		vendaModel.setValorVenda(Util.formataMoeda(totalVendaBruto));
		return this;
	}

	public VendaModelBuilder setIdVenda(int idVenda) {
		vendaModel.setId(Integer.toString(idVenda));
		return this;
	}

}

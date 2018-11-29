package br.com.marcospcruz.gestorloja.controller;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Operacao;

public enum OperacaoEnum {
	RECEITA(1), DESPESA(2);
	private int idOperacao;

	private OperacaoEnum(int idOperacao) {
		this.idOperacao = idOperacao;
	}

	public Operacao getOperacao() {
		Crud<Operacao> dao = new CrudDao<>();
		return dao.busca(Operacao.class, idOperacao);
	}

}

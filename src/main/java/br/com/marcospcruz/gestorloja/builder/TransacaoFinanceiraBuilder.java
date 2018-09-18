package br.com.marcospcruz.gestorloja.builder;

import br.com.marcospcruz.gestorloja.controller.OperacaoEnum;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.TransacaoFinanceira;

public class TransacaoFinanceiraBuilder {
	private static TransacaoFinanceiraBuilder instance;
	private static TransacaoFinanceira transacao;

	private static TransacaoFinanceiraBuilder getInstance() {
		if (instance == null) {
			instance = new TransacaoFinanceiraBuilder();
			instance.criaTransacao();
		}
		return instance;

	}

	public void criaTransacao() {
		transacao = new TransacaoFinanceira();

	}

	public TransacaoFinanceiraBuilder setMeioPagamento(MeioPagamento meioPagamento) {
		transacao.setMeioPagamento(meioPagamento);
		return this;
	}

	public TransacaoFinanceiraBuilder setCaixa(Caixa caixa) {
		transacao.setCaixa(caixa);
		return this;
	}

	public TransacaoFinanceiraBuilder setDescricao(String descricaoTransacao) {
		transacao.setDescricaoTransacao(descricaoTransacao);
		return this;
	}

	public TransacaoFinanceira getTransacaoFinanceira() {

		return transacao;
	}

	public static TransacaoFinanceiraBuilder criaTransacao(boolean isReceita) {
		if (isReceita)
			return criaReceita();
		else
			return criaDespesa();
	}

	private static TransacaoFinanceiraBuilder criaDespesa() {
		// TODO Auto-generated method stub
		return null;
	}

	public static TransacaoFinanceiraBuilder criaReceita() {
		getInstance();
		transacao.setOperacao(OperacaoEnum.RECEITA.getOperacao());
		return instance;
	}
}

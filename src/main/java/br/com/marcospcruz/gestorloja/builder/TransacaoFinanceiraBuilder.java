package br.com.marcospcruz.gestorloja.builder;

import org.apache.log4j.Logger;

import br.com.marcospcruz.gestorloja.controller.OperacaoEnum;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.TransacaoFinanceira;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;

public class TransacaoFinanceiraBuilder {
	private static TransacaoFinanceiraBuilder instance;
	private TransacaoFinanceira transacao;
	
	public void criaTransacao() {
		if (transacao == null)
			transacao = new TransacaoFinanceira();
		
	}
	private void log(String message) {
		Logger logger = SingletonManager.getInstance().getLogger(TransacaoFinanceiraBuilder.class);
		logger.info(message);
	}
	public TransacaoFinanceiraBuilder setMeioPagamento(MeioPagamento meioPagamento) {
		transacao.setMeioPagamento(meioPagamento);
		transacao.setValorTransacaoFinanceira(meioPagamento.getValorPago());
		return this;
	}

	public TransacaoFinanceiraBuilder setCaixa(Caixa caixa) {
		transacao.setCaixa(caixa);
		return this;
	}

	public TransacaoFinanceiraBuilder setMotivo(String motivo) throws Exception {
		
		if (motivo.isEmpty())
			throw new Exception("Informar o motivo desta transação.");
		log(motivo);
		transacao.setMotivo(motivo);
		return this;
	}

	public TransacaoFinanceira getTransacaoFinanceira() {

		return transacao;
	}

	public TransacaoFinanceiraBuilder criaTransacao(boolean isReceita) {
		criaTransacao();
		if (isReceita)
			return criaReceita();
		else
			return criaDespesa();
	}

	private TransacaoFinanceiraBuilder criaDespesa() {

		return criaTransacao(OperacaoEnum.DESPESA);
	}

	public TransacaoFinanceiraBuilder criaReceita() {
		criaTransacao();
		return criaTransacao(OperacaoEnum.RECEITA);
	}

	protected TransacaoFinanceiraBuilder criaTransacao(OperacaoEnum operacao) {

		transacao.setOperacao(operacao.getOperacao());
		return this;
	}

	public TransacaoFinanceiraBuilder setValorTransacaoFinanceira(String text) throws Exception {
		if (text.equals("0,00"))
			throw new Exception("Valor inválido para a Transação.");
		transacao.setValorTransacaoFinanceira(Util.parseStringDecimalToFloat(text));
		return this;
	}
}

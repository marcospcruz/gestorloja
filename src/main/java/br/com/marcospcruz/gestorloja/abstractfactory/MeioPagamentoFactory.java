package br.com.marcospcruz.gestorloja.abstractfactory;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoCartaoCredito;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoCartaoDebito;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoDinheiro;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoOutros;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;

public class MeioPagamentoFactory {

	public static MeioPagamento createMeioPagamento(String text) {
		Crud<TipoMeioPagamento> tipoPagamentoDao = new CrudDao<>();
		TipoMeioPagamento tipoMeioPagamento = tipoPagamentoDao.busca("tipoMeioPagamento.buscaPorDescricao", "descricao",
				text);
		return retornaMeioPagamento(tipoMeioPagamento);
	}

	private static MeioPagamento retornaMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {
		MeioPagamento meioPagamento;
		switch (tipoMeioPagamento.getIdTipoMeioPagamento()) {

		case 2:
			meioPagamento = new MeioPagamentoCartaoDebito();

			break;
		case 3:
			meioPagamento = new MeioPagamentoCartaoCredito();
			// return new MeioPagamentoCartaoCredito();
			break;
		case 4:
			meioPagamento = new MeioPagamentoOutros();
			// return new MeioPagamentoOutros();
			break;
		default:
			meioPagamento = new MeioPagamentoDinheiro();
			// return new MeioPagamentoDinheiro();
		}
		meioPagamento.setTipoMeioPagamento(tipoMeioPagamento);
		return meioPagamento;
	}

}

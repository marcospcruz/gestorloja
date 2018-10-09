package br.com.marcospcruz.gestorloja.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.LazyInitializationException;

import br.com.marcospcruz.gestorloja.builder.TransacaoFinanceiraBuilder;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.TransacaoFinanceira;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;

public class CaixaController extends ControllerBase {

	private static final String QUERY_BUSCA_TODOS = "caixa.findAll";
	private static final String BUSCA_CAIXA_ABERTO = "caixa.findCaixaAberto";
	private static final String MOTIVO_TRANSACAO = "Entrada da Venda ";

	private List<Caixa> caixaList;
	private Caixa caixa;
	private Crud<Caixa> dao;
	private Pagamento pagamento;

	public CaixaController() {
		super();
		dao = new CrudDao<>();
	}

	@Override
	public void busca(Object id) {

		caixa = dao.busca(Caixa.class, Integer.parseInt(id.toString()));

	}

	@Override
	public List buscaTodos() {

		caixaList = null;
		// if (caixaList == null || caixaList.isEmpty())
		caixaList = dao.busca(QUERY_BUSCA_TODOS);

		for (int index = 0; index < caixaList.size();) {
			this.caixa = caixaList.remove(index);

			atualizaSaldoCaixa();
			caixaList.add(index, this.caixa);
			index++;
		}
		return caixaList;
	}

	@Override
	public List getList() {
		return caixaList;

	}

	@Override
	public void busca(String query) throws Exception {
		// Crud<Caixa> dao = getDao();
		caixaList = dao.busca(query);

	}

	@Override
	public Object getItem() {

		try {
			if (caixa == null) {
				buscaCaixa(caixa);
				// busca(BUSCA_CAIXA_ABERTO);
				if (!caixaList.isEmpty())
					caixa = caixaList.get(0);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		// ajustaSaldoFinalZeroCaixa();
		// caixa.setSaldoFinal(atualizaSaldoCaixa());

		return caixa;
	}

	@Override
	public void setList(List list) {
		this.caixaList = list;

	}

	@Override
	public void setItem(Object object) {
		this.caixa = (Caixa) object;

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva() throws Exception {
		if (caixa.getUsuarioAbertura() == null)
			caixa.setUsuarioAbertura(SingletonManager.getInstance().getUsuarioLogado());
		// atualizaSaldoCaixa();

		caixa = dao.update(caixa);

	}

	public void validateCaixaAberto() throws Exception {
		//@formatter:off
		int size = 0;
		for(Caixa caixa:caixaList)
			if(caixa.getDataFechamento() == null)
				size++;
		//@formatter:on
		if (size > 0) {
			throw new Exception("Caixa já aberto.");
		}
	}

	public void validateCaixaFechado() throws Exception {

		busca(BUSCA_CAIXA_ABERTO);

		if (caixaList == null || caixaList.isEmpty()) {
			throw new Exception("Não há caixa aberto.");
		}

		caixa = caixaList.get(0);
	}

	public void fechaCaixa() throws Exception {
		caixa.setDataFechamento(SingletonManager.getInstance().getData());
		caixa.setUsuarioFechamento(getUsuarioLogado());

		salva();
		caixa = new Caixa();
	}

	public void abreCaixa(String saldoAbertura) throws Exception {
		// caixa = new Caixa();
		// float saldoInicial = 0f;
		// if (saldoAbertura != null) {
		//
		// saldoInicial = Float.parseFloat(saldoAbertura);
		// }
		// caixa.setSaldoInicial(saldoInicial);
		// caixa.setUsuarioAbertura(getUsuarioLogado());

		salva();

	}

	public Caixa getUltimoCaixa() {
		int ultimoIndice = getList().size() - 1;
		Caixa ultimoCaixa = null;
		if (ultimoIndice >= 0)
			ultimoCaixa = (Caixa) getList().get(ultimoIndice);
		return ultimoCaixa;
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void recebePagamento(Pagamento pagamento) throws Exception {
		// meioPagamentoDao.update(pagamento);
		Date dataVenda = pagamento.getDataVenda();
		Usuario operador = pagamento.getUsuarioLogado();
		for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
			// for (int i = 0; i < pagamento.getMeiosPagamento().size(); i++) {
			// MeioPagamento meioPagamento = pagamento.getMeiosPagamento().remove(i);

		}
		// setPagamento(pagamentoDao.update(pagamento));
		setPagamento(pagamento);
	}

	public TipoMeioPagamento buscaTipoMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {
		CrudDao<TipoMeioPagamento> tipoMeioPagamentoDao = new CrudDao<>();
		return tipoMeioPagamentoDao.busca("tipoMeioPagamento.buscaPorDescricao", "descricao",
				tipoMeioPagamento.getDescricaoTipoMeioPagamento());
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;

	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void atualizaPagamento(Pagamento pagamento) {
		Crud<Pagamento> pagamentoDao = new CrudDao<>();
		pagamento = pagamentoDao.update(pagamento);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	public Float getSubTotalVendas() {
		float subTotalVendas = 0;
		try {
			for (Venda venda : caixa.getVendas()) {
				subTotalVendas += venda.getTotalVendido();
			}
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
		}
		return subTotalVendas;
	}

	public List controcalculaSubTotalRecebido() {

		Set<Venda> vendas = caixa.getVendas();
		Map<TipoMeioPagamento, Float> subTotalRecebido = mapMeioPagamentosRecebidosCaixa(vendas);
		List linhas = new ArrayList();
		for (TipoMeioPagamento tm : subTotalRecebido.keySet()) {
			Object linha = new Object[] { tm.getDescricaoTipoMeioPagamento(),
					Util.formataMoeda(subTotalRecebido.get(tm)) };
			linhas.add(linha);
		}
		return linhas;
	}

	private Map<TipoMeioPagamento, Float> mapMeioPagamentosRecebidosCaixa(Set<Venda> vendas) {
		Map<TipoMeioPagamento, Float> subTotalRecebido = new HashMap<>();
		for (Venda venda : vendas) {
			Pagamento pagamento = venda.getPagamento();

			for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {

				TipoMeioPagamento tipoMeioPagamento = meioPagamento.getTipoMeioPagamento();
				float valorRecebido = subTotalRecebido.containsKey(tipoMeioPagamento)
						? subTotalRecebido.get(tipoMeioPagamento)
						: 0;
				valorRecebido += meioPagamento.getValorPago();

				subTotalRecebido.put(tipoMeioPagamento, valorRecebido);
			}
		}
		return subTotalRecebido;
	}

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean validaMesmoTipoPagamento(MeioPagamento mp, Pagamento pagamento) {
		if (pagamento.getMeiosPagamento().contains(mp))
			for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
				if (meioPagamento.getTipoMeioPagamento().getDescricaoTipoMeioPagamento()
						.equals(mp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento())
						&& meioPagamento.getValorPago() == mp.getValorPago()) {
					return true;
				}
			}
		return false;
	}

	@Override
	public void novoUsuario() {
		Caixa ultimoCaixa = buscaUltimoCaixa();
		caixa = new Caixa();

		float saldoInicial = 0;
		if (caixa == null)
			caixa = new Caixa();
		if (ultimoCaixa != null)
			saldoInicial = ultimoCaixa.getSaldoFinal();

		caixa.setSaldoInicial(saldoInicial);
		caixa.setSaldoFinal(saldoInicial);
	}

	public Float getTotalPagamentosVendasRecebidoCaixa() {

		float totalRecebido = 0;
		try {

			Crud<Pagamento> dao = new CrudDao<>();
			if (caixa != null && caixa.getVendas() != null)
				for (Venda venda : caixa.getVendas()) {

					if (venda.isEstornado())
						continue;
					Pagamento pagamento = dao.busca(Pagamento.class, venda.getPagamento().getIdPagamento());

					for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
						totalRecebido += meioPagamento.getValorPago();

					}
					totalRecebido -= pagamento.getTrocoPagamento();
				}

		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
		}
		return totalRecebido;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getSubTotaisMeiosPagamentoRecebidos() {
		Map<String, Float> subTotais = inicializaSubTotaisMap();
		if (caixa != null && caixa.getVendas() != null) {
			Set<Venda> vendas = caixa.getVendas();
			Crud<Pagamento> pagamentoDao = new CrudDao<>();

			// dao.update(caixa);
			try {
				for (Venda venda : vendas) {
					if (!venda.isEstornado()) {

						Pagamento pagamento = pagamentoDao.busca(Pagamento.class,
								venda.getPagamento().getIdPagamento());
						for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
							String key = meioPagamento.getTipoMeioPagamento().getDescricaoTipoMeioPagamento();
							float value = subTotais.get(key) + meioPagamento.getValorPago();

							subTotais.put(key, value);
						}
					}
				}

			} catch (Exception e) {
				SingletonManager.getInstance().getLogger(getClass()).warn(e);
			}
		}
		return subTotais;
	}

	private Map<String, Float> inicializaSubTotaisMap() {
		Map<String, Float> map = new HashMap<>();
		Crud<TipoMeioPagamento> dao = new CrudDao<>();
		for (TipoMeioPagamento tipo : dao.busca("tipoMeioPagamento.buscaTodos")) {
			map.put(tipo.getDescricaoTipoMeioPagamento(), 0f);
		}
		return map;
	}

	public int getQuantidadeVendasEstornadas() {
		int qtVendas = 0;
		try {
			Set<Venda> vendas = caixa.getVendas();

			qtVendas = vendas.stream().filter(venda -> venda.isEstornado()).collect(Collectors.toList()).size();
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
		}

		return qtVendas;
	}

	public int getQuantidadeVendasEfetivadas() {
		if (caixa == null || caixa.getVendas() == null)
			return 0;

		// if (caixa.getIdCaixa() != 0)
		// caixa = dao.update(caixa);
		int qtVendas = 0;
		try {
			qtVendas = caixa.getVendas().size() - getQuantidadeVendasEstornadas();
		} catch (LazyInitializationException e) {
			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
		}
		return qtVendas;
	}

	public List<String> getTiposMeioPagamentoOutros() {
		Crud<MeioPagamento> tipoMeioPagamentoDao = new CrudDao<>();
		List<MeioPagamento> tipos = tipoMeioPagamentoDao.busca("meioPagamento.buscaPagamentosOutrosDistinct");
		List<String> descricoes = new ArrayList<>();
		for (MeioPagamento tipo : tipos) {
			if (tipo.getDescricao() != null)
				descricoes.add(tipo.getDescricao());
		}
		return descricoes;
	}

	public void geraReceitaDeVendaCaixa(MeioPagamento meioPagamento, Date date) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
		//@formatter:off
		TransacaoFinanceiraBuilder builder=new TransacaoFinanceiraBuilder();
		TransacaoFinanceira tf=builder.criaReceita()
		.setMeioPagamento(meioPagamento)
		.setCaixa(caixa)
		.setMotivo(MOTIVO_TRANSACAO +(caixa.getVendas().size() + 1)+" em "+ sdf.format(date))
		.getTransacaoFinanceira();

		//@formatter:on
		meioPagamento.setTransacaoFinanceira(tf);
		// salva();
		caixa.getTransacoesCaixa().add(tf);
		atualizaSaldoCaixa();

	}

	public void atualizaSaldoCaixa() {
		if (caixa.getIdCaixa() != 0)
			buscaCaixa(caixa);
		float saldoCaixa = caixa.getSaldoInicial();

		for (TransacaoFinanceira transacaoFinanceira : caixa.getTransacoesCaixa()) {

			if (transacaoFinanceira.getOperacao().getIdOperacao() == 1) {
				saldoCaixa += transacaoFinanceira.getValorTransacaoFinanceira();
			} else {
				saldoCaixa -= transacaoFinanceira.getValorTransacaoFinanceira();
			}
		}
		caixa.setSaldoFinal(saldoCaixa);
	}

	public void buscaCaixa(Caixa caixa) {
		this.caixa = dao.busca("caixa.findCaixa", "id", caixa.getIdCaixa());

	}

	public Float getTotalTransacoesCaixa(int idOperacao) {
		float total = 0;

		//@formatter:off
		
		Set<TransacaoFinanceira> transacoes = caixa.getTransacoesCaixa()
				.stream()
				.filter(tf->tf.getOperacao().getIdOperacao()==idOperacao)
				.collect(Collectors.toSet());
		//@formatter:on
		for (TransacaoFinanceira tf : transacoes) {
			total += tf.getValorTransacaoFinanceira();
		}
		return total;
	}

	public void adicionaTransacao(TransacaoFinanceira transacao) {

		caixa.getTransacoesCaixa().add(transacao);
		atualizaSaldoCaixa();

	}

	public Caixa buscaUltimoCaixa() {
		buscaTodos();
		if (!caixaList.isEmpty()) {
			return caixaList.get(caixaList.size() - 1);

		}

		return null;

	}

	public float getTotalVendasCaixa() {
		float totalVendas = 0;
		buscaCaixa(caixa);
		for (Venda venda : caixa.getVendas()) {
			totalVendas += venda.getTotalVendido();
		}

		return totalVendas;
	}

}
